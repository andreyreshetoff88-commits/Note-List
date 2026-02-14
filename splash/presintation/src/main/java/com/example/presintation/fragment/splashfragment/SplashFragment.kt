package com.example.presintation.fragment.splashfragment

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presintation.R
import com.example.presintation.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kg.reshetoff.core.base.BaseFragment

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    private val viewModel: SplashViewModel by viewModels()
    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSplashBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {
        val fabeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.imgLogo.startAnimation(fabeIn)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().popBackStack()
            if (!viewModel.checkUser()) {
                val uri = "notelist://auth".toUri()
                findNavController().navigate(uri)
            } else {
                if (viewModel.checkVerifyEmail()) {
                    val uri = "notelist://groups".toUri()
                    findNavController().navigate(uri)
                } else {
                    val uri = "notelist://verify_email".toUri()
                    findNavController().navigate(uri)
                }
            }
        }, 3500)
    }
}