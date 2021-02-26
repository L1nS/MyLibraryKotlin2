package com.lins.mykotlinlibrary.ui.hencoder.animate

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.R

/**
 *@CreateTime 2021/1/26 16:44
 *@Describe
 */
class CameraView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val IMAGE_SIZE = 200.dp
    private val IMAGE_PADDING = 100.dp
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var bottomFoldAngle = 0f
        set(value) {
            field = value
            invalidate()
        }
    var topFoldAngle = 0f
        set(value) {
            field = value
            invalidate()
        }
    var canvasRotate = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val camera = Camera().apply {
        setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas) {

        canvas.save()
        canvas.translate(IMAGE_PADDING + IMAGE_SIZE / 2, IMAGE_PADDING + IMAGE_SIZE / 2)
        canvas.rotate(-canvasRotate)
        camera.save()
        camera.rotateX(topFoldAngle)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-IMAGE_SIZE, -IMAGE_SIZE, IMAGE_SIZE, 0f)
        canvas.rotate(canvasRotate)
        canvas.translate(-(IMAGE_PADDING + IMAGE_SIZE / 2), -(IMAGE_PADDING + IMAGE_SIZE / 2))
        canvas.drawBitmap(getBitmap(IMAGE_SIZE.toInt()), IMAGE_PADDING, IMAGE_PADDING, paint)
        canvas.restore()

        canvas.save()
        canvas.translate(IMAGE_PADDING + IMAGE_SIZE / 2, IMAGE_PADDING + IMAGE_SIZE / 2)
        canvas.rotate(-canvasRotate)
        camera.save()
        camera.rotateX(bottomFoldAngle)
        camera.applyToCanvas(canvas)
        camera.restore()
        canvas.clipRect(-IMAGE_SIZE, 0f, IMAGE_SIZE, IMAGE_SIZE)
        canvas.rotate(canvasRotate)
        canvas.translate(-(IMAGE_PADDING + IMAGE_SIZE / 2), -(IMAGE_PADDING + IMAGE_SIZE / 2))
        canvas.drawBitmap(getBitmap(IMAGE_SIZE.toInt()), IMAGE_PADDING, IMAGE_PADDING, paint)
        canvas.restore()
    }

    private fun getBitmap(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)
    }
}