package com.example.profile_presentation.fragment.cropavatarfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core.base.BaseFragment
import com.example.core.showToast
import com.example.profile_presentation.databinding.FragmentCropAvatarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropAvatarFragment : BaseFragment<FragmentCropAvatarBinding>() {
    private val args: CropAvatarFragmentArgs by navArgs()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCropAvatarBinding.inflate(inflater, container, false)

    override fun initFields() {
        binding.cropImageView.post {
            binding.cropImageView.setImageUriAsync(args.imageUri.toUri())
        }
        binding.btnConfirm.setOnClickListener {
            binding.cropImageView.croppedImageAsync()
        }
        binding.cropImageView.setOnCropImageCompleteListener { view, result ->
            binding.btnConfirm.visibility = View.GONE
            view.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            if (result.isSuccessful) {
                setFragmentResult(
                    "CROP_AVATAR_REQUEST",
                    bundleOf("CROPPED_AVATAR_URI" to result.uriContent.toString())
                )
                findNavController().popBackStack()
            } else {
                binding.btnConfirm.visibility = View.VISIBLE
                view.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                result.error?.message?.let { message -> showToast(message) }
            }
        }
    }

    override fun initFlow() = Unit
}