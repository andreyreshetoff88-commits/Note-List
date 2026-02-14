package com.example.profile_presentation.fragment.profilefragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.core.State
import com.example.core.showToast
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
        lifecycleScope.launch {
            viewModel.getUserInfo()
        }
        btnExit.setOnClickListener {
            showLogoutDialog()
        }

        btnChangePassword.setOnClickListener {
            showToast("Функция в разработке")
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
        viewModel.userInfoState.onEach {
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
        }.launchIn(lifecycleScope)
    }

    private fun showLogoutDialog() {
        val dialogBinding = DialogSignOutBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnLogoutConfirm.setOnClickListener {
            lifecycleScope.launch {
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
                    viewModel.editFirstName(dialogBinding.etEnterDisplayName.text.toString())
                }
                dialog.dismiss()
            } else
                dialogBinding.tltEnterDisplayName.error = "Поле должно быть заполнено"
        }

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
                showToast("Фамилия изменена")
                dialog.dismiss()
            } else
                dialogBinding.tltEnterDisplayName.error = "Поле должно быть заполнено"
        }

        dialogBinding.btnEditDisplayNameCancel.setOnClickListener {
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