package com.example.presintation.fragment.registerfragment

import android.content.Context
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core.Constants.PASSWORD_REGEX
import com.example.core.State
import com.example.core.base.BaseBaseFragmentFragment
import com.example.domain.models.UserModel
import com.example.presintation.databinding.FragmentRegisterBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterBottomSheetFragment : BaseBaseFragmentFragment<FragmentRegisterBottomSheetBinding>() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBottomSheetBinding.inflate(inflater, container, false)

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
                    binding.llFields.visibility = View.INVISIBLE
                    binding.btnNext.visibility = View.INVISIBLE
                }

                is State.Success -> {
                    val uri = "notelist://verify_email".toUri()
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

    override fun initFields() = with(binding) {
        var passwordIsConfirm = false
        etEnterFirstName.addTextChangedListener {
            if (tltEnterFirstName.isErrorEnabled)
                tltEnterFirstName.isErrorEnabled = false
        }
        etEnterLastName.addTextChangedListener {
            if (tltEnterLastName.isErrorEnabled)
                tltEnterLastName.isErrorEnabled = false
        }
        etEnterEmail.addTextChangedListener {
            if (tltEnterEmail.isErrorEnabled)
                tltEnterEmail.isErrorEnabled = false
        }
        etEnterPassword.addTextChangedListener {
            if (tltEnterPassword.isErrorEnabled)
                tltEnterPassword.isErrorEnabled = false
        }
        etConfirmPassword.addTextChangedListener {
            passwordIsConfirm = etConfirmPassword.text.toString().trim() ==
                    etEnterPassword.text.toString().trim()
            if (tltConfirmPassword.isErrorEnabled)
                tltConfirmPassword.isErrorEnabled = false
        }
        btnNext.setOnClickListener {
            if (checkFields()) {
                if (passwordIsConfirm) {
                    val imm =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)

                    val firstName = etEnterFirstName.text.toString().trim()
                    val lastName = etEnterLastName.text.toString().trim()
                    val email = etEnterEmail.text.toString().trim()
                    val password = etEnterPassword.text.toString().trim()
                    lifecycleScope.launch {
                        viewModel.registerUser(
                            userModel = UserModel(
                                firstName = firstName,
                                lastName = lastName,
                                email = email
                            ), password = password
                        )
                    }
                } else
                    tltConfirmPassword.error = "Пароли не совпадают"
            }
        }
    }

    private fun checkFields(): Boolean = with(binding) {
        val firstName = etEnterFirstName.text.toString().trim()
        val lastName = etEnterLastName.text.toString().trim()
        val email = etEnterEmail.text.toString().trim()
        val password = etEnterPassword.text.toString().trim()
        if (firstName.isEmpty()) {
            tltEnterFirstName.error = "Введите Ваше имя"
            return false
        } else if (lastName.isEmpty()) {
            tltEnterLastName.error = "Введите Вашу фамилию"
            return false
        } else if (email.isEmpty()) {
            tltEnterEmail.error = "Введите email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tltEnterEmail.error = "Некорректный email"
            return false
        }
        if (password.isEmpty()) {
            tltEnterPassword.error = "Введите пароль"
            return false
        } else if (!PASSWORD_REGEX.matches(password)) {
            tltEnterPassword.error =
                "Минимум 8 символов: 1 заглавная, 1 строчная, 1 цифра и 1 символ \"_\" или \"-\". Разрешены только латинские буквы, цифры, \"_\" и \"-\"."
            return false
        }
        return true
    }
}