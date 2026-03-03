package com.example.friends_presentation.fragment.friendsfragment.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.core.R
import com.example.friends_domain.models.UserModel
import com.example.friends_presentation.databinding.UserFriendItemBinding

class FriendsViewHolder(
    private val binding: UserFriendItemBinding
) : ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(userFriend: UserModel) {
        binding.imgUserFriendPhoto.load(userFriend.userPhoto ?: R.drawable.default_user_photo)
        binding.tvUserFriendName.text = "${userFriend.firstName} ${userFriend.lastName}"
        binding.tvUserFriendEmail.text = userFriend.email
    }
}