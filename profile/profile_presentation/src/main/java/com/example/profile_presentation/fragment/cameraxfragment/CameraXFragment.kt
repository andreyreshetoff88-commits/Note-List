package com.example.profile_presentation.fragment.cameraxfragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.core.base.BaseFragment
import com.example.core.showToast
import com.example.profile_presentation.R
import com.example.profile_presentation.databinding.DialogWhyWeNeedPermissionBinding
import com.example.profile_presentation.databinding.FragmentCameraXBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CameraXFragment : BaseFragment<FragmentCameraXBinding>() {
    private var imageCapture: ImageCapture? = null
    private val cameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted)
                initCamera()
            else
                requestCameraPermissions()
        }

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCameraXBinding.inflate(inflater, container, false)

    override fun initFlow() = Unit

    override fun initFields() {
        requestCameraPermissions()
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnMakePhoto.setOnClickListener { makePhoto() }
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(binding.pvCamera.surfaceProvider) }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                exc.localizedMessage?.let { showToast(it) }
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun makePhoto() {
        val imageCapture = imageCapture ?: return

        val file = File(
            requireContext().cacheDir,
            "photo_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showToast("Не удалось сделать снимок: ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = file.toUri()
                    setFragmentResult(
                        "SELECTED_IMAGE_REQUEST",
                        bundleOf("SELECTED_IMAGE_URI" to uri.toString())
                    )
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun requestCameraPermissions() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                initCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showWhyWeNeedCameraPermission()
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showWhyWeNeedCameraPermission() {
        val dialogBinding = DialogWhyWeNeedPermissionBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()
        dialogBinding.tvNeedPermissionMessage.text =
            getString(R.string.wy_we_need_camera_permission_message_string)
        dialogBinding.btnNeedPermissionConfirm.setOnClickListener {
            openAppSettings()
            dialog.dismiss()
        }
        dialogBinding.btnNeedPermissionCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        startActivity(intent)
    }
}