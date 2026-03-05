package com.example.verify_email_presentation.fragment.verifyemailfragment

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.core.base.BaseFragment
import com.example.verify_email_presentation.R
import com.example.verify_email_presentation.databinding.FragmentVerifyEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailFragment : BaseFragment<FragmentVerifyEmailBinding>() {
    private var timer: CountDownTimer? = null
    private val viewModel: VerifyEmailViewModel by viewModels()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVerifyEmailBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun onStart() {
        super.onStart()
        if (viewModel.checkVerifyEmail()) {
            val uri = "notelist://groups".toUri()
            findNavController().navigate(uri)
        }
    }

    override fun initFields() {
        binding.btnResend.setOnClickListener {
            startResendTimer()
            viewModel.sendVerifyEmail()
        }
    }

    private fun startResendTimer() {
        binding.btnResend.isEnabled = false
        timer?.cancel()

        timer = object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.btnResend.text = getString(R.string.resend_in_string) + " $seconds с"
            }

            override fun onFinish() {
                binding.btnResend.isEnabled = true
                binding.btnResend.text = getString(R.string.resend_string)
            }
        }.start()
    }
}