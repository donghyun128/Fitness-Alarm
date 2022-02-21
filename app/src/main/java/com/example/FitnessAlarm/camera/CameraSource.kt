
/* Copyright 2021 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================
*/

package com.example.FitnessAlarm.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.text.TextPaint
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import androidx.core.content.ContextCompat.startActivity
import com.example.FitnessAlarm.AlarmSetting
import com.example.FitnessAlarm.CountAlgorithm.SquatCounter
import com.example.FitnessAlarm.CameraActivity
import com.example.FitnessAlarm.MainActivity
import com.example.FitnessAlarm.Visualization.VisualizationUtils
import com.example.FitnessAlarm.Visualization.YuvToRgbConverter
import com.example.FitnessAlarm.movenet.PoseClassifier
import com.example.FitnessAlarm.movenet.PoseDetector
import com.example.FitnessAlarm.data.Person
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.properties.Delegates

class CameraSource (
    private val surfaceView: SurfaceView,
    private var listener: CameraSourceListener? = null,
    val context: Context
) {

    companion object {
        private const val PREVIEW_WIDTH = 640
        private const val PREVIEW_HEIGHT = 480

        /** Threshold for confidence score. */
        private const val MIN_CONFIDENCE = .2f
        private const val TAG = "Camera Source"
    }
    private val lock = Any()
    private var detector: PoseDetector? = null
    private var classifier: PoseClassifier? = null
    private var isTrackerEnabled = false
    private var yuvConverter: YuvToRgbConverter = YuvToRgbConverter(surfaceView.context)
    private lateinit var imageBitmap: Bitmap

    /** Frame count that have been processed so far in an one second interval to calculate FPS. */
    private var fpsTimer: Timer? = null
    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

    /** Detects, characterizes, and connects to a CameraDevice (used for all camera operations) */
    private val cameraManager: CameraManager by lazy {
        val context = surfaceView.context
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /** Readers used as buffers for camera still shots */
    private var imageReader: ImageReader? = null

    /** The [CameraDevice] that will be opened in this fragment */
    private var camera: CameraDevice? = null

    /** Internal reference to the ongoing [CameraCaptureSession] configured with our parameters */
    private var session: CameraCaptureSession? = null

    /** [HandlerThread] where all buffer reading operations run */
    private var imageReaderThread: HandlerThread? = null

    /** [Handler] corresponding to [imageReaderThread] */
    private var imageReaderHandler: Handler? = null
    private var cameraId: String = ""

    suspend fun initCamera() {


        Log.i("CameraSource","initCamera")

        var isFinished = 0

            camera = openCamera(cameraManager, cameraId)
            imageReader =
                ImageReader.newInstance(PREVIEW_WIDTH, PREVIEW_HEIGHT, ImageFormat.YUV_420_888, 3)

            imageReader?.setOnImageAvailableListener({ reader ->
                Log.d("CameraSource", "setOnImageAvailableListener")

                if (MainActivity.workoutCounter.count != MainActivity.workoutCounter.completeGoal) {
                    val image = reader.acquireLatestImage()
                    if (!::imageBitmap.isInitialized) {
                        imageBitmap =
                            Bitmap.createBitmap(
                                PREVIEW_WIDTH,
                                PREVIEW_HEIGHT,
                                Bitmap.Config.ARGB_8888
                            )
                    }
                    if (image != null) {
                        yuvConverter.yuvToRgb(image, imageBitmap)
                        // Create rotated version for portrait display
                        val rotateMatrix = Matrix()
                        rotateMatrix.postRotate(90.0f)

                        val rotatedBitmap = Bitmap.createBitmap(
                            imageBitmap, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT,
                            rotateMatrix, false
                        )
                        Log.d("CameraSource", "Before execute processImage")
                        processImage(rotatedBitmap)
                        Log.d("CameraSource", "After execute processImage")
                        image.close()
                        Log.d("CameraSource", "After execute image.close()")

                    }

                    /*
                    if (MainActivity.workoutCounter.count == MainActivity.workoutCounter.completeGoal) {
                        isFinished = 1
                        imageReaderHandler!!.removeCallbacksAndMessages(reader)
                        imageReader = null
                        session?.stopRepeating()
                        listener = null
                        camera?.close()
                        imageReaderThread?.quit()
                        close()
                        val quitIntent = Intent(context,MainActivity::class.java)
                        context.startActivity(quitIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
                    }
                    */
                }
                else
                {
                    //val quitIntent = Intent(context,MainActivity::class.java)
                    //context.startActivity(quitIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))
                    finishAlarm(reader)
                }
            }, imageReaderHandler)


            imageReader?.surface?.let { surface ->
                session = createSession(listOf(surface))
                val cameraRequest = camera?.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                )?.apply {
                    addTarget(surface)
                }
                cameraRequest?.build()?.let {
                    session?.setRepeatingRequest(it, null, null)
                }
            }
            Log.d("CameraSource", "ImageAvailableListener End")


        Log.d("CameraSource","initCamera End")
    }
    private suspend fun createSession(targets: List<Surface>): CameraCaptureSession =
        suspendCancellableCoroutine { cont ->
            Log.d("CameraSource","createSession")
            camera?.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(captureSession: CameraCaptureSession) =
                    cont.resume(captureSession)

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    cont.resumeWithException(Exception("Session error"))
                }
            }, null)
            Log.d("CameraSource","createSession End")
        }

    @SuppressLint("MissingPermission")
    private suspend fun openCamera(manager: CameraManager, cameraId: String): CameraDevice =
        suspendCancellableCoroutine { cont ->
            Log.d("CameraSource","openCamera")
            manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) = cont.resume(camera)

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    if (cont.isActive) cont.resumeWithException(Exception("Camera error"))
                }
            }, imageReaderHandler)
            Log.d("CameraSource","openCamera End")
        }

    fun prepareCamera() {
        Log.d("CameraSource","prepareCamera")
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)

            // We don't use a front facing camera in this sample.
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (cameraDirection != null &&
                cameraDirection == CameraCharacteristics.LENS_FACING_FRONT
            ) {
                continue
            }
            this.cameraId = cameraId
        }
        Log.d("CameraSource","prepareCamera End")
    }

    fun setDetector(detector: PoseDetector) {
        Log.d("CameraSource","setDetector")
        synchronized(lock) {
            if (this.detector != null) {
                this.detector?.close()
                this.detector = null
                Log.i("aa", CameraActivity.personForCount[1].keyPoints[0].coordinate.x.toString())
            }
            this.detector = detector
        }
        Log.d("CameraSource","setDetector End")
    }

    fun setClassifier(classifier: PoseClassifier?) {
        Log.d("CameraSource","setClassifier")
        synchronized(lock) {
            if (this.classifier != null) {
                this.classifier?.close()
                this.classifier = null
            }
            this.classifier = classifier
        }
        Log.d("CameraSource","setClassifier End")
    }
/*
    /**
     * Set Tracker for Movenet MuiltiPose model.
     */
    fun setTracker(trackerType: TrackerType) {
        isTrackerEnabled = trackerType != TrackerType.OFF
        (this.detector as? MoveNetMultiPose)?.setTracker(trackerType)
    }
*/
    fun resume() {
    Log.d("CameraSource","resume")
    imageReaderThread = HandlerThread("imageReaderThread").apply { start() }
        imageReaderHandler = Handler(imageReaderThread!!.looper)
        fpsTimer = Timer()
        fpsTimer?.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    framesPerSecond = frameProcessedInOneSecondInterval
                    frameProcessedInOneSecondInterval = 0
                }
            },
            0,
            1000
        )
    Log.d("CameraSource","resume End")
}

    fun close() {
        Log.d("CameraSource","close")
        session?.close()
        session = null
        camera?.close()
        camera = null
        imageReader?.close()
        imageReader = null
        //imageReaderHandler!!.removeMessages(100)
        //imageReaderHandler = null
        stopImageReaderThread()
        detector?.close()
        detector = null
        classifier?.close()
        classifier = null
        fpsTimer?.cancel()
        fpsTimer = null
        frameProcessedInOneSecondInterval = 0
        framesPerSecond = 0
        Log.d("CameraSource","close End")
    }

    // process image
    private fun processImage(bitmap: Bitmap) {
        Log.d("CameraSource","processImage")
        CameraActivity.personForCount = mutableListOf<Person>()

        val persons = mutableListOf<Person>()
        var classificationResult: List<Pair<String, Float>>? = null

        synchronized(lock) {
            Log.d("CameraSource : ", "estimatePoses")
            detector?.estimatePoses(bitmap)?.let {

                persons.addAll(it)

            }
        }
        CameraActivity.personForCount = persons
        Log.d("nose coordinate_x",CameraActivity.personForCount[0].keyPoints[0].coordinate.x.toString())
        //Counter.workoutCounter.countAlgorithm(Counter.personForCount[0])
        Log.d("rep : ",MainActivity.workoutCounter.countAlgorithm(CameraActivity.personForCount[0]).toString())
        frameProcessedInOneSecondInterval++
        if (frameProcessedInOneSecondInterval == 1) {
            // send fps to view
            listener?.onFPSListener(framesPerSecond)
        }

        // if the model returns only one item, show that item's score.
        if (persons.isNotEmpty()) {
            listener?.onDetectedInfo(persons[0].score, classificationResult)
        }
        visualize(persons, bitmap, MainActivity.workoutCounter as SquatCounter)
        Log.d("CameraSource","processImage End")
    }

    private fun visualize(persons: List<Person>, bitmap: Bitmap,counter : SquatCounter) {
        Log.d("CameraSource","visualize")
        // outputBitmap : camera에서 얻은 bitmap에 BodyKeyPoint를 그린 bitmap
        val outputBitmap = VisualizationUtils.drawBodyKeypoints(
            bitmap,
            persons.filter { it.score > MIN_CONFIDENCE }, isTrackerEnabled
        )

        val holder = surfaceView.holder

        // surfaceView 스크린에 그리기 시작
        val surfaceCanvas = holder.lockCanvas()
        surfaceCanvas?.let { canvas ->
            val screenWidth: Int
            val screenHeight: Int
            val left: Int
            val top: Int

            if (canvas.height > canvas.width) {
                val ratio = outputBitmap.height.toFloat() / outputBitmap.width
                screenWidth = canvas.width
                left = 0
                screenHeight = (canvas.width * ratio).toInt()
                top = (canvas.height - screenHeight) / 2
            } else {
                val ratio = outputBitmap.width.toFloat() / outputBitmap.height
                screenHeight = canvas.height
                top = 0
                screenWidth = (canvas.height * ratio).toInt()
                left = (canvas.width - screenWidth) / 2
            }
            val right: Int = left + screenWidth
            val bottom: Int = top + screenHeight

            canvas.drawBitmap(
                outputBitmap, Rect(0, 0, outputBitmap.width, outputBitmap.height),
                Rect(left, top, right, bottom), null
            )

            val textPaint = TextPaint()

            //textPaint.setARGB(100,50,30,20)
            // text 크기 설정
            textPaint.textSize = 80F
            //textPaint.textAlign = Paint.Align.CENTER
            // text 색 설정
            textPaint.color = Color.rgb(153,204,255)
            // text 굵기 설정
            textPaint.strokeWidth = 20.toFloat()

            textPaint.isAntiAlias = true



            val xPos = (canvas.width / 8).toFloat()
            val yPos = (bottom - canvas.height / 8).toFloat()
            Log.i("draw_text","Count : " + counter.count.toString())
            canvas.drawText("Count : " + MainActivity.workoutCounter.count.toString() + " / " + MainActivity.workoutCounter.completeGoal.toString(),
                xPos,
                yPos,
                textPaint)

            // 수정된 surfaceView를 unlock
            surfaceView.holder.unlockCanvasAndPost(canvas)
        }
        Log.d("CameraSource","visualize End")
    }


    private fun stopImageReaderThread() {
        Log.d("CameraSource","stopImageReaderThread")
        imageReaderThread?.quitSafely()
        try {
            imageReaderThread?.join()
            imageReaderThread = null
            imageReaderHandler = null
        } catch (e: InterruptedException) {
            Log.d(TAG, e.message.toString())
        }
        Log.d("CameraSource","stopImageReaderThread End")
    }

    // 운동을 전부 마쳤으면, 알람 창을 종료한다.
    private fun finishAlarm(reader : ImageReader)
    {
            imageReaderHandler!!.removeCallbacksAndMessages(reader)
            imageReader = null
            //session?.stopRepeating()
            listener = null
            camera?.close()
            val quitIntent = Intent(context,MainActivity::class.java)
            context.startActivity(quitIntent.addFlags(FLAG_ACTIVITY_NEW_TASK))

    }

    interface CameraSourceListener {
        fun onFPSListener(fps: Int)

        fun onDetectedInfo(personScore: Float?, poseLabels: List<Pair<String, Float>>?)
    }


}
