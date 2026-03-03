package com.example.friends_presentation.fragment.friendsfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.friends_domain.models.UserModel
import com.example.friends_presentation.databinding.UserFriendItemBinding

class FriendsAdapter(userFriends: List<UserModel>) : Adapter<FriendsViewHolder>() {
    private val _userFriends = mutableListOf<UserModel>()

    init {
        _userFriends.addAll(userFriends)
    }

    fun addFriend(userFriend: UserModel) {
        _userFriends.add(userFriend)
        notifyItemInserted(_userFriends.size - 1)
    }

    fun updateFriend(userFriend: UserModel) {
        val index = _userFriends.indexOfFirst { it.id == userFriend.id }
        if (index == -1) return

        _userFriends[index] = userFriend
        notifyItemChanged(index)
    }

    fun deleteFriend(userFriend: UserModel) {
        val index = _userFriends.indexOfFirst { it.id == userFriend.id }
        if (index == -1) return

        _userFriends.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, _userFriends.size - index)
    }

    fun friendsListIsEmpty() = _userFriends.isEmpty()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FriendsViewHolder(
        binding = UserFriendItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: FriendsViewHolder,
        position: Int
    ) {
        holder.bind(_userFriends[position])
    }

    override fun getItemCount() = _userFriends.size
}