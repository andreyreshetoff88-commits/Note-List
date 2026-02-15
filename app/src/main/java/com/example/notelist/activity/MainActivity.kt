package com.example.notelist.activity

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.notelist.R
import com.example.notelist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainNavView: BottomNavigationView = binding.mainBottomNavMenu
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = host.navController
        mainNavView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.syncData()
        }
        initFields()
    }

    private fun initFields() {
        val mainBottomMenuDestinations = setOf(
            com.example.profile_presentation.R.id.profileFragment,
            com.example.groups_presentation.R.id.groupListFragment,
            com.example.friends_presentation.R.id.friendsFragment
        )
        onBackPressedDispatcher.addCallback(this) {
            val currentFragmentId = navController.currentDestination?.id
            if (currentFragmentId in mainBottomMenuDestinations) {
                finish()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
        initListenerDestination(mainBottomMenuDestinations)
    }

    private fun initListenerDestination(mainBottomMenuDestinations: Set<Int>) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in mainBottomMenuDestinations) {
                binding.mainBottomNavMenu.visibility = View.VISIBLE
            } else
                binding.mainBottomNavMenu.visibility = View.GONE
        }
    }
}