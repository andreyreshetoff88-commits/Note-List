package com.example.profile_presentation.fragment.changeuserphotobottomsheetdialogfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.profile_domain.models.GalleryImage
import com.example.profile_presentation.databinding.ImageItemBinding

class ChangeUserPhotoAdapter(
    private val userPhotoChecked: (galleryImage: GalleryImage) -> Unit
) : Adapter<ChangeUserPhotoViewHolder>() {
    private var photos = mutableListOf<GalleryImage>()

    fun addData(listPhotos: List<GalleryImage>) {
        photos.clear()
        photos.addAll(listPhotos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ChangeUserPhotoViewHolder(
        binding = ImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        userPhotoChecked = userPhotoChecked
    )

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(
        holder: ChangeUserPhotoViewHolder,
        position: Int
    ) {
        holder.bind(galleryImage = photos[position])
    }
}