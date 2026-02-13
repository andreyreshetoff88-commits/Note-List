package com.example.profile_presentation.fragment.profilefragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.profile_presentation.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kg.reshetoff.core.base.BaseFragment

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {

    }

}