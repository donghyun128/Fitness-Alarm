
package com.example.FitnessAlarm.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.FitnessAlarm.R
import com.example.FitnessAlarm.camera.CameraSource
import com.example.FitnessAlarm.data.Device
import com.example.FitnessAlarm.data.Person
import com.example.FitnessAlarm.data.SharedPreferenceUtils
import com.example.FitnessAlarm.movenet.*
import kotlinx.coroutines.*


public class CameraActivity : AppCompatActivity() {

    companion object {
        var personForCount : MutableList<Person> = mutableListOf()
    }


    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
    private var cameraSource: CameraSource? = null
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    private lateinit var surfaceView: SurfaceView
    private var device = Device.GPU
    private var isClassifyPose = false
    private lateinit var spnTracker: Spinner
    private lateinit var vTrackerOption: View
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                openCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                //ErrorDialog.newInstance(getString(R.string.tfe_pe_request_permission))
                //    .show(supportFragmentManager, FRAGMENT_DIALOG)
            }
        }

    private var modelPos = 1

    // open camera
    private fun openCamera() {
        Log.i("CameraActivity","openCamera")
        if (isCameraPermissionGranted()) {
            if (cameraSource == null) {
                cameraSource =
                    CameraSource(surfaceView, object : CameraSource.CameraSourceListener {
                        override fun onFPSListener(fps: Int) {
                        }

                        override fun onDetectedInfo(
                            personScore: Float?,
                            poseLabels: List<Pair<String, Float>>?
                        ) {
                        }
                    },applicationContext).apply {
                        prepareCamera()
                    }

                // 코루틴 실행

                    var isFinished : Int = 0
                    val visualizeCoroutine = lifecycleScope.launch {

                        Log.i("test_log","initCamera in lifecycleScope.launch in Counter")
                        cameraSource?.initCamera()

                        Log.d("initCamera 종료","initCamera 종료")
                    }



                // count와 complete 값이 같아지면 initCamera를 종료
                Log.d("Out of Launch Block","Out of Launch Block")
            }
            createPoseEstimator()


        }

        Log.i("openCamera End","openCamera End")
    }

    // check if permission is granted or not.
    private fun isCameraPermissionGranted(): Boolean {
        Log.i("CameraActivity","isCameraPermissionGranted")
        return checkPermission(
            Manifest.permission.CAMERA,
            Process.myPid(),
            Process.myUid()
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        Log.i("CameraActivity : ","requestPermission")
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // You can use the API that requires the permission.
                openCamera()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
        Log.i("CameraActivity","requestPermission End")
    }


    // Show/hide the pose classification option.
    private fun showPoseClassifier(isVisible: Boolean) {
       //vClassificationOption.visibility = if (isVisible) View.VISIBLE else View.GONE
        if (!isVisible) {
            //swClassification.isChecked = false
        }
    }

    private fun isPoseClassifier() {
        cameraSource?.setClassifier(if (isClassifyPose) PoseClassifier.create(this) else null)
    }

    private fun createPoseEstimator() {
        Log.i("CameraActivity","createPoseEstimator")
        // For MoveNet MultiPose, hide score and disable pose classifier as the model returns
        // multiple Person instances.
        val poseDetector = MoveNet.create(this, device, ModelType.Thunder)

        poseDetector?.let { detector ->
            Log.i("CameraActivity","setDetector")
            cameraSource?.setDetector(detector)
            Log.i("CameraActivity","createPoseEstimator End")
        }
    }


    // Show/hide the tracking options.
    private fun showTracker(isVisible: Boolean) {
        if (isVisible) {
            // Show tracker options and enable Bounding Box tracker.
            vTrackerOption.visibility = View.VISIBLE
            spnTracker.setSelection(1)
        } else {
            // Set tracker type to off and hide tracker option.
            vTrackerOption.visibility = View.GONE
            spnTracker.setSelection(0)
        }
    }

    private fun finishActivity()
    {
        if (MainActivity.workoutCounter.count == MainActivity.workoutCounter.completeGoal)
        {
            Log.d("finish","finish")
            Log.d("completeGoal", MainActivity.workoutCounter.completeGoal.toString())
            finish()
            val quitIntent : Intent = Intent(this, MainActivity::class.java)
            startActivity(quitIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("CameraActivity","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter)

        surfaceView = findViewById(R.id.surfaceView)
        if (!isCameraPermissionGranted()) {
            requestPermission()
        }
        Log.i("CameraActivity","onCreate End")
    }

    override fun onStart() {
        Log.i("CameraActivity","onStart")
        super.onStart()
        openCamera()
        //finishActivity()
        Log.i("CameraActivity","onStart End")
    }

    override fun onResume() {
        Log.i("CameraActivity","onResume")
        cameraSource?.resume()
        super.onResume()
        Log.i("CameraActivity","onResume End")
        //finishActivity()
    }

    override fun onPause() {
        Log.i("CameraActivity","onPause")
        cameraSource?.close()
        cameraSource = null
        super.onPause()
        Log.i("CameraActivity","onPause End")
    }


}


