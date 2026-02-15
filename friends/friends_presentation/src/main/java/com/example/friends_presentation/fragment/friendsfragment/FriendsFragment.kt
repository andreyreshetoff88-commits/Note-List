package com.example.friends_presentation.fragment.friendsfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.friends_presentation.databinding.FragmentFriendsBinding
import dagger.hilt.android.AndroidEntryPoint
import kg.reshetoff.core.base.BaseFragment

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {
    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFriendsBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {

    }
}