package com.torrex.shopit.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Order
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_order_placed.*
import java.io.File
import java.io.FileOutputStream

class OrderPlacedActivity : BaseActivity() {

    private var orderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)


        if (intent.hasExtra(Constants.ORDER_PLACED_INVOICE)) {
            orderId = intent.getStringExtra(Constants.ORDER_PLACED_INVOICE)!!
            loadDataInView(orderId)
        }
        btn_order_placed_download_invoice.setOnClickListener {
            createPDF()
            //TODO("Create bit map for the Pdf to download")
        }

    }

    //Load data in View
    private fun loadDataInView(orderId: String) {
        showProgressDialog("Loading..")
        FireStoreClass().getOrderDetails(this, orderId)

    }

    //Function get order Details Success-
    fun getOrderDetailsSuccess(order: Order) {
        //Product Details---------------------------

        if (order.image!=null){
            GlideLoader(this).loadUserPicture(order.image,iv_order_placed_product_image)
        }

        tv_order_placed_product_name.text = order.title
        tv_order_placed_product_price.text = order.product.productPrice.toString()
        tv_order_placed_product_quantity.text = order.productQuantity.toString()
        tv_order_placed_product_shipping_charge.text = order.shippingCharge.toString()
        tv_order_placed_product_total_amount.text = order.totalAmount.toString()

        val user = Constants.getCurrentProfileUser()!!

        //Address Details -------------------------------------
        tv_order_placed_address_user_details.text = "${user.firstName} ${user.lastName}"
        tv_order_placed_address.text =
            "${order.address.addressLine1} ${order.address.addressLine2} ${order.address.addressLine3} "
        tv_order_placed_address_city.text = order.address.addressCity
        tv_order_placed_address_State.text = order.address.addressState
        tv_order_placed_address_postal_code.text = order.address.addressPostalCode.toString()
        tv_order_placed_address_mobile.text = order.address.addressMobileNO.toString()

        //Order Details---------------------------------------
        tv_order_placed_order_id.text = order.id
        tv_order_placed_order_date.text = order.orderDatetime
        if (order.orderPlaced) {
            tv_order_placed_order_status.text = "Placed"
        } else {
            tv_order_placed_order_status.text = "Failed"
        }
        tv_order_placed_order_total_amount_paid.text = order.totalAmount.toString()
        tv_order_placed_order_transaction_id.text = order.orderPaymentTransactionId

        hideProgressDialog()
    }


    //Function to create PDF file----------------------------------------

    @SuppressLint("ResourceType")
    private fun createPDF(){
        val inflater=LayoutInflater.from(this)
        val view=inflater.inflate(R.layout.activity_order_placed,null)

        //fetch the dimension from the viewport--
        val displayMetrics=DisplayMetrics()
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            this.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        }
        else{
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        view.measure(
            View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels,View.MeasureSpec.EXACTLY)
            , View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels,View.MeasureSpec.EXACTLY))

        //creating a bitmap with the measured width and height----------------------
        view.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels)
        val bitmap=Bitmap.createBitmap(view.measuredWidth,view.measuredHeight,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        view.draw(canvas)

        //Scaled down version for A4 size---
        //Bitmap.createScaledBitmap(bitmap,595,842,true)

        //creating pdf file----
        val pdfDocument=PdfDocument()
        val pageInfo=PdfDocument.PageInfo.Builder(1024,1580,1).create()

        val page=pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap,0F,0F,null)
        pdfDocument.finishPage(page)


        //creating the file path----
        val date=System.currentTimeMillis()
        val fileName= "$date.pdf"
        val filePath=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName)
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        showErrorSnackBar("PDF Saved to Downloads ",false)

        //TODO("Size issue in the pdf file creation..........")
    }


    override fun onBackPressed() {
        finishAffinity()
        startActivity(Intent(this, DashboardActivity::class.java))
        super.onBackPressed()
    }

}