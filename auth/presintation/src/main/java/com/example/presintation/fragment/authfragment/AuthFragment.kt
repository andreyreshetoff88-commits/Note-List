package com.example.presintation.fragment.authfragment

import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core.Constants.PASSWORD_REGEX
import com.example.core.State
import com.example.presintation.R
import com.example.presintation.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kg.reshetoff.core.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>() {
    private val viewModel: AuthViewModel by viewModels()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAuthBinding.inflate(inflater, container, false)

    override fun initFlow() {
        viewModel.viewState.onEach {
            when (it) {
                is State.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.llFields.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                }

                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.llFields.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                }

                is State.Success -> {
                    val uri = "notelist://groups".toUri()
                    findNavController().popBackStack()
                    findNavController().navigate(uri)
                }

                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.llFields.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE

                    binding.tltEnterEmail.error = it.message
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun initFields() {
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.registerBottomSheetFragment)
        }
        binding.etEnterEmail.addTextChangedListener {
            if (binding.tltEnterEmail.isErrorEnabled)
                binding.tltEnterEmail.isErrorEnabled = false
        }
        binding.etEnterPassword.addTextChangedListener {
            if (binding.tltEnterPassword.isErrorEnabled)
                binding.tltEnterPassword.isErrorEnabled = false
        }
        binding.btnNext.setOnClickListener {
            val email = binding.etEnterEmail.text.toString().trim()
            val password = binding.etEnterPassword.text.toString().trim()
            if (email.isEmpty()) {
                binding.tltEnterEmail.error = "Введите email"
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tltEnterEmail.error = "Некорректный email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.tltEnterPassword.error = "Введите пароль"
                return@setOnClickListener
            } else if (!PASSWORD_REGEX.matches(password)) {
                binding.tltEnterPassword.error =
                    "Минимум 8 символов: 1 заглавная, 1 строчная, 1 цифра и 1 символ \"_\" или \"-\". Разрешены только латинские буквы, цифры, \"_\" и \"-\"."
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.loginUser(email = email, password = password)
            }
        }
    }

}