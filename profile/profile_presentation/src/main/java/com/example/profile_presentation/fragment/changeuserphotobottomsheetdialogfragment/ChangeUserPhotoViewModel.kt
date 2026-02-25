package com.example.profile_presentation.fragment.changeuserphotobottomsheetdialogfragment

import androidx.lifecycle.ViewModel
import com.example.profile_domain.usecase.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeUserPhotoViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase
) : ViewModel() {
    suspend fun getImages() = getImagesUseCase.execute()
}