package com.example.profile_presentation.fragment.profilefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.core.Constants.PASSWORD_REGEX
import com.example.core.State
import com.example.core.showToast
import com.example.profile_presentation.databinding.DialogChangePasswordBinding
import com.example.profile_presentation.databinding.DialogEditDisplayNameBinding
import com.example.profile_presentation.databinding.DialogSignOutBinding
import com.example.profile_presentation.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kg.reshetoff.core.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel: ProfileViewModel by viewModels()

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun initFlow() {
        initSignOutFlow()
        initUserInfoFlow()
    }

    override fun initFields() = with(binding) {
        btnExit.setOnClickListener {
            showLogoutDialog()
        }

        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        btnEditFirstNameProfile.setOnClickListener {
            showEditFirstNameDialog()
        }

        btnEditLastNameProfile.setOnClickListener {
            showEditLastNameDialog()
        }
    }

    private fun initSignOutFlow() {
        viewModel.signOutState.onEach {
            when (it) {
                is State.Empty -> {
                    showUI()
                }

                is State.Loading -> {
                    showProgressBar()
                }

                is State.Success -> {
                    val uri = "notelist://auth".toUri()
                    findNavController().navigate(uri)
                }

                is State.Error -> {
                    showUI()
                    showToast(message = it.message.toString())
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun initUserInfoFlow() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoState.collect {
                    when (it) {
                        is State.Empty -> {
                            showUI()
                        }

                        is State.Loading -> {
                            showProgressBar()
                        }

                        is State.Success -> {
                            showUI()

                            it.data?.let { userModel ->
                                tvFirstNameProfile.text = userModel.firstName
                                tvLastNameProfile.text = userModel.lastName
                                tvEmailProfile.text = userModel.email
                                imgUserPhoto.load(
                                    data =
                                        if (userModel.userPhoto.equals("null"))
                                            com.example.core.R.drawable.default_user_photo
                                        else
                                            userModel.userPhoto
                                )
                            }
                        }

                        is State.Error -> {
                            showUI()
                            showToast(message = it.message.toString())
                        }
                    }
                }
            }
        }
    }

    private fun showLogoutDialog() {
        val dialogBinding = DialogSignOutBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnLogoutConfirm.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signOut()
            }
            dialog.dismiss()
        }

        dialogBinding.btnLogoutCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditFirstNameDialog() {
        val dialogBinding = DialogEditDisplayNameBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.tvEditDisplayNameTitle.text = "Изменить имя?"
        dialogBinding.tltEnterDisplayName.hint = "Введите имя"

        dialogBinding.btnEditDisplayNameConfirm.setOnClickListener {
            if (dialogBinding.etEnterDisplayName.text.toString().isNotEmpty()) {
                lifecycleScope.launch {
                    viewModel.editFirstName(dialogBinding.etEnterDisplayName.text.toString().trim())
                }
            } else
                dialogBinding.tltEnterDisplayName.error = "Поле должно быть заполнено"
        }

        viewModel.editUserInfoState.onEach {
            when (it) {
                is State.Empty -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                }

                is State.Loading -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.INVISIBLE
                    dialogBinding.progressBar.visibility = View.VISIBLE
                }

                is State.Success -> {
                    dialog.dismiss()
                }

                is State.Error -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                    showToast(it.message.toString())
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dialogBinding.btnEditDisplayNameCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditLastNameDialog() {
        val dialogBinding = DialogEditDisplayNameBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.tvEditDisplayNameTitle.text = "Изменить фамилию?"
        dialogBinding.tltEnterDisplayName.hint = "Введите фамилию"

        dialogBinding.btnEditDisplayNameConfirm.setOnClickListener {
            if (dialogBinding.etEnterDisplayName.text.toString().isNotEmpty()) {
                if (dialogBinding.etEnterDisplayName.text.toString().isNotEmpty()) {
                    lifecycleScope.launch {
                        viewModel.editLastName(
                            dialogBinding.etEnterDisplayName.text.toString().trim()
                        )
                    }
                } else
                    dialogBinding.tltEnterDisplayName.error = "Поле должно быть заполнено"
            } else
                dialogBinding.tltEnterDisplayName.error = "Поле должно быть заполнено"
        }

        viewModel.editUserInfoState.onEach {
            when (it) {
                is State.Empty -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                }

                is State.Loading -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.INVISIBLE
                    dialogBinding.progressBar.visibility = View.VISIBLE
                }

                is State.Success -> {
                    dialog.dismiss()
                }

                is State.Error -> {
                    dialogBinding.tltEnterDisplayName.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                    showToast(it.message.toString())
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dialogBinding.btnEditDisplayNameCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showChangePasswordDialog() {
        val dialogBinding = DialogChangePasswordBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.etEnterOldPassword.addTextChangedListener {
            if (dialogBinding.tltEnterOldPassword.isErrorEnabled)
                dialogBinding.tltEnterOldPassword.isErrorEnabled = false
        }

        dialogBinding.etEnterNewPassword.addTextChangedListener {
            if (dialogBinding.tltEnterNewPassword.isErrorEnabled)
                dialogBinding.tltEnterNewPassword.isErrorEnabled = false
        }

        dialogBinding.etEnterConfirmNewPassword.addTextChangedListener {
            if (dialogBinding.tltEnterConfirmNewPassword.isErrorEnabled)
                dialogBinding.tltEnterConfirmNewPassword.isErrorEnabled = false
        }

        dialogBinding.btnChangePasswordConfirm.setOnClickListener {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            val oldPassword = dialogBinding.etEnterOldPassword.text.toString()
            val newPassword = dialogBinding.etEnterNewPassword.text.toString()
            val confirmNewPassword = dialogBinding.etEnterConfirmNewPassword.text.toString()
            if (oldPassword.isEmpty()) {
                dialogBinding.tltEnterOldPassword.error = "Введите старый пароль"
                return@setOnClickListener
            } else if (newPassword.isEmpty()) {
                dialogBinding.tltEnterNewPassword.error = "Введите новый пароль"
                return@setOnClickListener
            } else if (!PASSWORD_REGEX.matches(newPassword)) {
                dialogBinding.tltEnterNewPassword.error =
                    "Минимум 8 символов: 1 заглавная, 1 строчная, 1 цифра и 1 символ \"_\" или " +
                            "\"-\". Разрешены только латинские буквы, цифры, \"_\" и \"-\"."
                return@setOnClickListener
            } else if (confirmNewPassword.isEmpty()) {
                dialogBinding.tltEnterConfirmNewPassword.error = "Введите новый пароль еще раз"
                return@setOnClickListener
            } else if (dialogBinding.etEnterConfirmNewPassword.text.toString().trim() !=
                dialogBinding.etEnterNewPassword.text.toString().trim()
            ) {
                dialogBinding.tltEnterConfirmNewPassword.error = "Пароли не совпадают"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.changePassword(oldPassword = oldPassword, newPassword = newPassword)
            }
        }

        viewModel.changePasswordState.onEach {
            when(it) {
                is State.Empty -> {
                    dialogBinding.llFields.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                }
                is State.Loading -> {
                    dialogBinding.llFields.visibility = View.INVISIBLE
                    dialogBinding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    showToast("Пароль успешно изменен")
                    val uri = "notelist://auth".toUri()
                    findNavController().navigate(uri)
                    dialog.dismiss()
                }
                is State.Error -> {
                    dialogBinding.llFields.visibility = View.VISIBLE
                    dialogBinding.progressBar.visibility = View.GONE
                    showToast(it.message ?: "Что-то пошло не так")
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        dialogBinding.btnChangePasswordCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showProgressBar() = with(binding) {
        progressBar.visibility = View.VISIBLE
        cvUserImageProfile.visibility = View.GONE
        cvUserInfoProfile.visibility = View.GONE
        btnChangePassword.visibility = View.GONE
        btnExit.visibility = View.GONE
    }

    private fun showUI() = with(binding) {
        progressBar.visibility = View.GONE
        cvUserImageProfile.visibility = View.VISIBLE
        cvUserInfoProfile.visibility = View.VISIBLE
        btnChangePassword.visibility = View.VISIBLE
        btnExit.visibility = View.VISIBLE
    }
}