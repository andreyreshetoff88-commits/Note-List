package com.example.profile_presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AvatarCropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private val matrix = Matrix()
    private val paintImage = Paint(Paint.ANTI_ALIAS_FLAG)

    private val overlayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x99000000.toInt() }
    private val clearPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastX = 0f
    private var lastY = 0f

    private var loadJob: Job? = null
    private var isLoading = false
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                val dy = event.y - lastY
                matrix.postTranslate(dx, dy)
                lastX = event.x
                lastY = event.y
                invalidate()
            }
        }
        return true
    }

    fun setImageUri(uri: Uri) {
        loadJob?.cancel()
        isLoading = true
        invalidate() // показать "Загрузка..."
        loadJob = CoroutineScope(Dispatchers.Main).launch {
            val bm = withContext(Dispatchers.IO) { loadBitmapFast(uri, width, height) }
            bitmap = bm
            centerImage()
            isLoading = false
            invalidate() // обновляем view без текста
        }
    }

    private fun loadBitmapFast(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (reqWidth == 0 || reqHeight == 0) return null

        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inJustDecodeBounds = false

        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        } ?: return null

        val rotation = getExifRotation(uri)
        if (rotation == 0f) return bitmap
        val matrix = Matrix().apply { postRotate(rotation) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getExifRotation(uri: Uri): Float {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val exif = ExifInterface(input)
                when (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        } catch (e: Exception) { 0f }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun centerImage() {
        bitmap?.let {
            val scale = width.toFloat() / it.width
            matrix.reset()
            matrix.postScale(scale, scale)
            matrix.postTranslate((width - it.width * scale) / 2f, (height - it.height * scale) / 2f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { canvas.drawBitmap(it, matrix, paintImage) }

        val save = canvas.saveLayer(null, null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)
        val radius = width.coerceAtMost(height) / 2f * 0.85f
        canvas.drawCircle(width / 2f, height / 2f, radius, clearPaint)
        canvas.restoreToCount(save)

        if (isLoading) {
            canvas.drawText("Загрузка...", width / 2f, height / 2f, paintText)
        }
    }

    fun crop(): Bitmap {
        bitmap?.let { src ->
            val size = 512
            val output = createBitmap(size, size)
            val canvas = Canvas(output)

            val centerX = width / 2f
            val centerY = height / 2f
            val radius = width.coerceAtMost(height) / 2f * 0.85f

            val points = floatArrayOf(centerX, centerY)
            val inverse = Matrix()
            matrix.invert(inverse)
            inverse.mapPoints(points)
            val srcCenterX = points[0]
            val srcCenterY = points[1]
            val srcRadius = radius / (matrix.mapRadius(1f))

            val left = (srcCenterX - srcRadius).coerceIn(0f, src.width.toFloat())
            val top = (srcCenterY - srcRadius).coerceIn(0f, src.height.toFloat())
            val right = (srcCenterX + srcRadius).coerceIn(0f, src.width.toFloat())
            val bottom = (srcCenterY + srcRadius).coerceIn(0f, src.height.toFloat())

            val srcRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            val dstRect = Rect(0, 0, size, size)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawBitmap(src, srcRect, dstRect, paint)

            val path = Path().apply { addCircle(size / 2f, size / 2f, size / 2f, Path.Direction.CW) }
            canvas.clipPath(path)

            return output
        } ?: throw IllegalStateException("Bitmap is null")
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            matrix.postScale(detector.scaleFactor, detector.scaleFactor, detector.focusX, detector.focusY)
            invalidate()
            return true
        }
    }
}
