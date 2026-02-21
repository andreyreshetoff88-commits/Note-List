package com.example.groups_presentation.fragment.grouplistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.groups_presentation.databinding.FragmentGroupListBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.core.base.BaseFragment

@AndroidEntryPoint
class GroupListFragment : BaseFragment<FragmentGroupListBinding>() {
    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGroupListBinding.inflate(inflater, container, false)

    override fun initFlow() {

    }

    override fun initFields() {

    }

}