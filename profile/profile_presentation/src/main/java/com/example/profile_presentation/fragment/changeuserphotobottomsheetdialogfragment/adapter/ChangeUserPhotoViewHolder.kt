package com.example.profile_presentation.fragment.changeuserphotobottomsheetdialogfragment.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.profile_domain.models.GalleryImage
import com.example.profile_presentation.databinding.ImageItemBinding

class ChangeUserPhotoViewHolder(
    private val binding: ImageItemBinding,
    private val userPhotoChecked: (galleryImage: GalleryImage) -> Unit
) : ViewHolder(binding.root) {
    fun bind(galleryImage: GalleryImage) {
        binding.imgUserPhoto.load(galleryImage.uri)
        itemView.setOnClickListener {
            userPhotoChecked(galleryImage)
        }
    }
}