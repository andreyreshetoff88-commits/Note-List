package com.example.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflaterViewBinding(inflater, container)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFlow()
    }

    protected abstract fun initFlow()

    protected abstract fun initFields()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}