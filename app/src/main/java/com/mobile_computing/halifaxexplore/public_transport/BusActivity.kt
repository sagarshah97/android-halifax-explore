package com.mobile_computing.halifaxexplore.public_transport




//
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import com.mobile_computing.halifaxexplore.R

class BusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        val imageView = findViewById<ImageViewTouch>(R.id.zoomableImageView)
        imageView.setImageResource(R.drawable.bus_map)
    }
}




//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import it.sephiroth.android.library.imagezoom.ImageViewTouch
//import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
//import com.mobile_computing.halifaxexplore.R
//
//class BusActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bus)
//
//        val imageView = findViewById<ImageViewTouch>(R.id.zoomableImageView).apply {
//            displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
//        }
//
//        imageView.setImageResource(R.drawable.bus_map) // Replace with your image resource
//    }
//}

//
//import android.graphics.Matrix
//import android.os.Bundle
//import android.view.ViewTreeObserver
//import androidx.appcompat.app.AppCompatActivity
//import it.sephiroth.android.library.imagezoom.ImageViewTouch
//import com.mobile_computing.halifaxexplore.R
//
//class BusActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bus)
//
//        val imageView = findViewById<ImageViewTouch>(R.id.zoomableImageView)
//        imageView.setImageResource(R.drawable.bus_map) // Replace with your image resource
//
//        val observer = imageView.viewTreeObserver
//        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                adjustImageScale(imageView)
//            }
//        })
//    }
//
//    private fun adjustImageScale(imageView: ImageViewTouch) {
//        val drawable = imageView.drawable ?: return
//
//        val drawableWidth = drawable.intrinsicWidth
//        val drawableHeight = drawable.intrinsicHeight
//        val imageViewWidth = imageView.width
//        val imageViewHeight = imageView.height
//
//        val scaleX = imageViewWidth.toFloat() / drawableWidth.toFloat()
//        val scaleY = imageViewHeight.toFloat() / drawableHeight.toFloat()
//        val scale = Math.min(scaleX, scaleY)
//
//        val matrix = Matrix()
//        matrix.postScale(scale, scale)
//        matrix.postTranslate(
//            (imageViewWidth - drawableWidth * scale) / 2f,
//            (imageViewHeight - drawableHeight * scale) / 2f
//        )
//
//        imageView.imageMatrix = matrix
//    }
//}
