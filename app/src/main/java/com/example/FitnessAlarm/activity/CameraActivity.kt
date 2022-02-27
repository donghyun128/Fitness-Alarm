
package com.example.FitnessAlarm.activity

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
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
    private lateinit var surfaceView: SurfaceView
    private var device = Device.GPU
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
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

    private fun openCamera() {
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

                    val visualizeCoroutine = lifecycleScope.launch {
                        cameraSource?.initCamera()
                    }



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
    }

    private fun createPoseEstimator() {
        Log.i("CameraActivity","createPoseEstimator")

        //val poseDetector = MoveNet.create(this, device, ModelType.Lightning)
        val poseDetector = MoveNet.create(this, device, ModelType.Thunder)
        poseDetector?.let { detector ->
            cameraSource?.setDetector(detector)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        surfaceView = findViewById(R.id.surfaceView)

        if (!isCameraPermissionGranted()) {
            requestPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        openCamera()
    }

    override fun onResume() {
        cameraSource?.resume()
        super.onResume()

        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }

    override fun onPause() {
        cameraSource?.close()
        cameraSource = null
        super.onPause()
    }

    override fun onBackPressed() {
    }

}


