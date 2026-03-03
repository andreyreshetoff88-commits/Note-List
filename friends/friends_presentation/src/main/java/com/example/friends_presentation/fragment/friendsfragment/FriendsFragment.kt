package com.example.friends_presentation.fragment.friendsfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.core.base.BaseFragment
import com.example.core.showToast
import com.example.friends_presentation.R
import com.example.friends_presentation.databinding.FragmentFriendsBinding
import com.example.friends_presentation.fragment.friendsfragment.adapter.FriendsAdapter
import com.example.friends_presentation.fragment.utils.FriendState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.core.view.isGone

@AndroidEntryPoint
class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {
    private val viewModel: FriendsViewModel by viewModels()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        FriendsAdapter(userFriends = viewModel.getUserFriends())
    }

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFriendsBinding.inflate(inflater, container, false)

    override fun initFlow() {
        viewModel.userFriendsState.onEach {
            when (it) {
                is FriendState.Empty -> Unit
                is FriendState.Loading -> Unit
                is FriendState.Added -> adapter.addFriend(it.data!!)
                is FriendState.Updated -> adapter.updateFriend(it.data!!)
                is FriendState.Deleted -> adapter.deleteFriend(it.data!!)
                is FriendState.Error -> showToast(it.message!!)
            }
        }.launchIn(lifecycleScope)
    }

    override fun initFields() {
        initRecycler()

        binding.btnAddNewFriend.setOnClickListener {
            findNavController().navigate(R.id.action_friendsFragment_to_addFriendFragment)
        }
        binding.notificationContainer.setOnClickListener {
            if (binding.badge.isGone)
                showBadge(5)
            else
                hideBadge()
        }
    }

    private fun initRecycler() {
        binding.rvUserFriends.adapter = adapter

        if (adapter.friendsListIsEmpty())
            binding.tvListIsEmpty.visibility = View.VISIBLE
        else
            binding.tvListIsEmpty.visibility = View.GONE
    }

    fun showBadge(count: Int) {
        binding.badge.apply {
            text = count.toString()
            visibility = View.VISIBLE
        }
        binding.imgNotifications.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                com.example.core.R.color.box_error_color
            )
        )
    }

    fun hideBadge() {
        binding.badge.visibility = View.GONE
        binding.imgNotifications.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                com.example.core.R.color.gray
            )
        )
    }
}