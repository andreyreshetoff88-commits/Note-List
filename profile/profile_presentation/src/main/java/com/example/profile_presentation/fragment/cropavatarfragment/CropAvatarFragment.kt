package com.example.profile_presentation.fragment.cropavatarfragment

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core.base.BaseFragment
import com.example.profile_presentation.databinding.FragmentCropAvatarBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CropAvatarFragment : BaseFragment<FragmentCropAvatarBinding>() {
    private val args: CropAvatarFragmentArgs by navArgs()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCropAvatarBinding.inflate(inflater, container, false)

    override fun initFields() {
        binding.avatarCropView.post {
            binding.avatarCropView.setImageUri(uri = args.imageUri.toUri())
        }

        binding.btnConfirm.setOnClickListener {
            val bitmap = binding.avatarCropView.crop()
            val uri = saveBitmapToCache(bitmap)

            setFragmentResult(
                "CROP_AVATAR_REQUEST",
                bundleOf("CROPPED_AVATAR_URI" to uri.toString())
            )
            findNavController().popBackStack()
        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(
            requireContext().cacheDir,
            "avatar_${System.currentTimeMillis()}.png"
        )

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return file.toUri()
    }

    override fun initFlow() = Unit
}