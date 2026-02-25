package com.example.profile_presentation.fragment.changeuserphotobottomsheetdialogfragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.core.base.BaseBottomSheetDialogFragment
import com.example.profile_domain.models.GalleryImage
import com.example.profile_presentation.databinding.DialogWhyWeNeedPermissionBinding
import com.example.profile_presentation.databinding.FragmentChangeUserPhotoBottomSheetDialogBinding
import com.example.profile_presentation.fragment.changeuserphotobottomsheetdialogfragment.adapter.ChangeUserPhotoAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeUserPhotoBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentChangeUserPhotoBottomSheetDialogBinding>() {
    private val viewModel: ChangeUserPhotoViewModel by viewModels()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ChangeUserPhotoAdapter(
            userPhotoChecked = this::userPhotoChecked
        )
    }
    private val readImagesPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.addData(viewModel.getImages())
                }
            } else {
                requestGalleryPermission()
            }
        }

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChangeUserPhotoBottomSheetDialogBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {
        requestGalleryPermission()
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvUsersPhotos.adapter = adapter
        val layoutManager = FlexboxLayoutManager(requireContext()).apply {
            justifyContent = JustifyContent.SPACE_BETWEEN
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.rvUsersPhotos.layoutManager = layoutManager
    }

    private fun userPhotoChecked(galleryImage: GalleryImage) {
        setFragmentResult(
            "SELECTED_IMAGE_REQUEST",
            bundleOf("SELECTED_IMAGE_URI" to galleryImage.uri)
        )
        dismiss()
    }

    private fun requestGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission)
                    == PackageManager.PERMISSION_GRANTED -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.addData(viewModel.getImages())
                }
            }

            shouldShowRequestPermissionRationale(permission) -> {
                showWhyWeNeedPermission()
            }

            else -> {
                readImagesPermissionLauncher.launch(permission)
            }
        }
    }

    private fun showWhyWeNeedPermission() {
        val dialogBinding = DialogWhyWeNeedPermissionBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnNeedPermissionConfirm.setOnClickListener {
            openAppSettings()
            dialog.dismiss()
        }

        dialogBinding.btnNeedPermissionCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        startActivity(intent)
    }
}