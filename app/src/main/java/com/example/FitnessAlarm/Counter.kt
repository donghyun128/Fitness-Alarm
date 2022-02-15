
package com.example.FitnessAlarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.FitnessAlarm.CountAlgorithm.SquatCounter
import com.example.FitnessAlarm.CountAlgorithm.WorkoutCounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.FitnessAlarm.camera.CameraSource
import com.example.FitnessAlarm.data.Device
import com.example.FitnessAlarm.data.Person
import com.example.FitnessAlarm.movenet.*


public class Counter : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_DIALOG = "dialog"
        var personForCount : MutableList<Person> = mutableListOf()
        var workoutCounter : WorkoutCounter = SquatCounter()
    }

    private var cameraSource: CameraSource? = null
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
        if (isCameraPermissionGranted()) {
            if (cameraSource == null) {
                cameraSource =
                    CameraSource(surfaceView, object : CameraSource.CameraSourceListener {
                        override fun onFPSListener(fps: Int) {
                            //tvFPS.text = getString(R.string.tfe_pe_tv_fps, fps)
                        }

                        override fun onDetectedInfo(
                            personScore: Float?,
                            poseLabels: List<Pair<String, Float>>?
                        ) {
                            //tvScore.text = getString(R.string.tfe_pe_tv_score, personScore ?: 0f)
                            /*
                            poseLabels?.sortedByDescending { it.second }?.let {
                                tvClassificationValue1.text = getString(
                                    R.string.tfe_pe_tv_classification_value,
                                    convertPoseLabels(if (it.isNotEmpty()) it[0] else null)
                                )
                                tvClassificationValue2.text = getString(
                                    R.string.tfe_pe_tv_classification_value,
                                    convertPoseLabels(if (it.size >= 2) it[1] else null)
                                )
                                tvClassificationValue3.text = getString(
                                    R.string.tfe_pe_tv_classification_value,
                                    convertPoseLabels(if (it.size >= 3) it[2] else null)
                                )
                            }
                            */
                        }

                    }).apply {
                        prepareCamera()
                    }
                //isPoseClassifier()
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.i("test_log","initCamera in lifecycleScope.launch in Counter")
                    cameraSource?.initCamera(this)
                }
            }
            createPoseEstimator()

        }
        Log.i("test_log","open_camera")
    }

    // check if permission is granted or not.
    private fun isCameraPermissionGranted(): Boolean {
        return checkPermission(
            Manifest.permission.CAMERA,
            Process.myPid(),
            Process.myUid()
        ) == PackageManager.PERMISSION_GRANTED
        Log.i("test_log","isCameraPermissonGranted")

    }

    private fun requestPermission() {
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
        Log.i("test_log","requestPermission")

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
        // For MoveNet MultiPose, hide score and disable pose classifier as the model returns
        // multiple Person instances.
        val poseDetector = MoveNet.create(this, device, ModelType.Thunder)
        /*
        val poseDetector = when (modelPos) {
            0 -> {
                // MoveNet Lightning (SinglePose)
                showPoseClassifier(true)
                //showDetectionScore(true)
                //showTracker(false)
                MoveNet.create(this, device, ModelType.Lightning)
            }
            1 -> {
                // MoveNet Thunder (SinglePose)
                showPoseClassifier(true)
                //showDetectionScore(true)
                //showTracker(false)
                MoveNet.create(this, device, ModelType.Thunder)
            }
            2 -> {
                // MoveNet (Lightning) MultiPose
                showPoseClassifier(false)
                //showDetectionScore(false)
                // Movenet MultiPose Dynamic does not support GPUDelegate
                /*
                if (device == Device.GPU) {
                   // showToast(getString(R.string.tfe_pe_gpu_error))
                }
                */

                //showTracker(true)
                /*
                MoveNetMultiPose.create(
                    this,
                    device,
                    Type.Dynamic
                )
                */

            }
            3 -> {
                // PoseNet (SinglePose)
                showPoseClassifier(true)
                //showDetectionScore(true)
                //showTracker(false)
                //PoseNet.create(this, device)
            }
            else -> {
                null
            }
        }
        */

        poseDetector?.let { detector ->
            cameraSource?.setDetector(detector)
            Log.i("test_log","setDetector")

        }
        Log.i("test_log","createPoseEstimator")

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




    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("alarm confirm", "alarm confirm")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.counter)


        surfaceView = findViewById(R.id.surfaceView)
        if (!isCameraPermissionGranted()) {
            requestPermission()
        }
        Log.i("test_log","onCreate")

    }

    override fun onStart() {
        super.onStart()
        openCamera()
        Log.i("test_log","openCamera in onStart")

    }

    override fun onResume() {
        cameraSource?.resume()
        super.onResume()
        Log.i("test_log","onResume")

    }

    override fun onPause() {
       cameraSource?.close()
       cameraSource = null
        super.onPause()
        Log.i("test_log","onPause")

    }



}


