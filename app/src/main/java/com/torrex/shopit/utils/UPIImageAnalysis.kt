package com.torrex.shopit.utils

import android.annotation.SuppressLint
import android.util.Log
import android.view.textclassifier.TextClassifier.TYPE_URL
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData
import com.google.zxing.qrcode.encoder.QRCode
import org.checkerframework.checker.formatter.qual.Format


class UPIImageAnalysis:ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        scanBarcode(image)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarcode(imageProxy: ImageProxy){
        imageProxy.image?.let { image->
            val inputImage=InputImage.fromMediaImage(image,imageProxy.imageInfo.rotationDegrees)
            val scanner=BarcodeScanning.getClient()

            scanner.process(inputImage).addOnCompleteListener {
                imageProxy.close()
                if (it.isSuccessful){
                    readBarCodeData(it.result as List<Barcode>)

                }
                else{
                    it.exception?.printStackTrace()
                }
            }

        }
    }

    private fun readBarCodeData(barcodes:List<Barcode>){
        for (barcode in barcodes){
            when(barcode.valueType){
                Barcode.FORMAT_QR_CODE->{
                    val data=barcode.format
                    Log.i("Data UPI",data.toString())

                }
                //TODO("Get data from bar code and process")
                Barcode.TYPE_URL->{
                    val url = barcode.url?.url.toString()
                    Log.i("Data UPI",url)
                }

            }

        }
    }
}