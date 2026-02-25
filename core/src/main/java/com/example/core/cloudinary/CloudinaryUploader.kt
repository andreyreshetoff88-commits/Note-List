package com.example.core.cloudinary

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.collections.get
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloudinaryUploader @Inject constructor() {
    suspend fun uploadImage(
        uri: Uri,
        preset: String,
        folderName: String
    ): String =
        suspendCancellableCoroutine { cont ->
            MediaManager.get().upload(uri).unsigned(preset)
                .option("folder", folderName)
                .callback(object : UploadCallback {
                    override fun onSuccess(
                        requestId: String?,
                        resultData: Map<*, *>
                    ) {
                        val url = resultData["secure_url"] as? String
                        if (url != null) {
                            cont.resume(url)
                        } else {
                            cont.resumeWithException(
                                Exception("Cloudinary вернул пустой url")
                            )
                        }
                    }

                    override fun onError(
                        requestId: String?,
                        error: ErrorInfo?
                    ) {
                        cont.resumeWithException(Exception(error?.description ?: "Ошибка загрузки"))
                    }

                    override fun onStart(requestId: String?) = Unit

                    override fun onProgress(
                        requestId: String?,
                        bytes: Long,
                        totalBytes: Long
                    ) = Unit

                    override fun onReschedule(
                        requestId: String?,
                        error: ErrorInfo?
                    ) = Unit

                }).dispatch()
        }
}