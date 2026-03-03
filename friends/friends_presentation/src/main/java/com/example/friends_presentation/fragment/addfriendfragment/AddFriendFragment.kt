package com.example.friends_presentation.fragment.addfriendfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.core.base.BaseFragment
import com.example.friends_presentation.databinding.FragmentAddFriendBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendFragment : BaseFragment<FragmentAddFriendBinding>() {
    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddFriendBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {

    }
}