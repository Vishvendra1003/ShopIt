package com.torrex.shopit.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.ImplementationMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.qrcode.encoder.QRCode
import com.torrex.shopit.R
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.UPIImageAnalysis
import kotlinx.android.synthetic.main.activity_bar_code_scanner.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarCodeScannerActivity : AppCompatActivity() {

    private val qrCode:String=""
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code_scanner)


        btn_scan_bar_code.setOnClickListener{
            Toast.makeText(this,"QR Code", Toast.LENGTH_SHORT).show()
            Log.i("QR Code Error", "QR Code Found: $qrCode")
            requestCamera()
        }

    }


    //Camera permission for the upi scan---
    private fun requestCamera(){
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
           startCamera()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),Constants.REQUEST_CAMERA_PERMISSION)
            Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }



    //function for the Camera---
    private fun startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider:ProcessCameraProvider=cameraProviderFuture.get()
                bindCameraPreview(cameraProvider)
            }
            catch (e:ExecutionException){
                Toast.makeText(this, "Error starting camera " + e.message, Toast.LENGTH_SHORT).show()
            }
        },ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraPreview(cameraProvider:ProcessCameraProvider){
        //pv_camera_scanner.implementationMode=ImplementationMode.COMPATIBLE
        //pv_camera_scanner.scaleType=PreviewView.ScaleType.FILL_CENTER
        val preview:Preview=Preview.Builder().build()

        val cameraSelector:CameraSelector=CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        preview.setSurfaceProvider(pv_camera_scanner.surfaceProvider)

        val imageAnalysis=ImageAnalysis.Builder().setTargetResolution(Size(512,512))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()

        imageAnalysis.setAnalyzer(cameraExecutor, UPIImageAnalysis())
        cameraProvider.bindToLifecycle(this as LifecycleOwner,cameraSelector,imageAnalysis,preview)

    }
}