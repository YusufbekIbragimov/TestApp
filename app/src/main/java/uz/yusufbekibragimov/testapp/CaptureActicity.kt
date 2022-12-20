package com.invan.salamatina.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import org.jmrtd.lds.icao.MRZInfo
import uz.yusufbekibragimov.testapp.R
import uz.yusufbekibragimov.testapp.util.camera.CameraSource
import uz.yusufbekibragimov.testapp.util.camera.CameraSourcePreview
import uz.yusufbekibragimov.testapp.util.other.GraphicOverlay
import uz.yusufbekibragimov.testapp.util.text.TextRecognitionProcessor
import java.io.IOException

enum class DocType {
    PASSPORT, ID_CARD, OTHER
}

class CaptureActivity : AppCompatActivity(), TextRecognitionProcessor.ResultListener {
    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var view: View? = null
    private var actionBar: ActionBar? = null
    private var docType: DocType? = DocType.OTHER
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.passport_read_camera_fragment)
        view = window.decorView
        actionBar = supportActionBar
        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar!!.hide()
        if (intent.hasExtra(DOC_TYPE)) {
            docType = intent.getSerializableExtra(DOC_TYPE) as DocType?
            if (docType === DocType.PASSPORT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
        preview = findViewById(R.id.camera_source_preview)
        if (preview == null) {
            Log.d(TAG, "Preview is null")
        }
        graphicOverlay = findViewById(R.id.graphics_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }
        createCameraSource()
        startCameraSource()
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        //        preview.stop();
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    private fun createCameraSource() {
        if (cameraSource == null) {
                cameraSource = CameraSource(this, graphicOverlay)
            cameraSource!!.setFacing(CameraSource.CAMERA_FACING_BACK)
        }
        cameraSource!!.setMachineLearningFrameProcessor(TextRecognitionProcessor(docType, this))
    }

    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                preview?.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    override fun onSuccess(mrzInfo: MRZInfo) {
        val returnIntent = Intent()
        returnIntent.putExtra(MRZ_RESULT, mrzInfo)
        cameraSource?.takePhoto()
        Log.d("MRX-307", "BIRTH DATE : " + mrzInfo.dateOfBirth)
        Log.d("MRX-307", "EXPIRY DATE : " + mrzInfo.dateOfExpiry)
        Log.d("MRX-307", "PASSPORT NO : " + mrzInfo.documentNumber)
        setResult(RESULT_OK, returnIntent)
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                finish()
            }
        }.start()
    }

    override fun onError(exp: Exception?) {
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {
        const val MRZ_RESULT = "MRZ_RESULT"
        const val DOC_TYPE = "DOC_TYPE"
        private val TAG = CaptureActivity::class.java.simpleName
        fun getScreenShot(view: View): Bitmap {
            val screenView = view.rootView
            screenView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(screenView.drawingCache)
            screenView.isDrawingCacheEnabled = false
            return bitmap
        }
    }
}