package com.personal.homestayfinder.activities.main


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.activities.BaseActivity
import com.personal.homestayfinder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var navController: NavController
    lateinit var  binding : ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        changeUserStatus(true)
    }
    override fun onResume() {
        super.onResume()
        changeUserStatus(true)
    }
    private  fun changeUserStatus(online : Boolean){
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val id = currentUser.uid
            viewModel.changeUserStatus(id, online)
        }
    }
    private fun setNavController(){
        try{
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as NavHostFragment
            navController = navHostFragment.navController
            binding.bottomNavigation.setupWithNavController(navController)
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build()
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                val currentFragment = navController.currentDestination?.id
                val selectedFragment = when (item.itemId) {
                    R.id.home -> R.id.homeFragment
                    R.id.chat -> R.id.userChatsFragment
                    R.id.account -> R.id.accountFragment
                    R.id.favorite -> R.id.favoriteFragment
                    else -> null
                }

                if (selectedFragment != null && currentFragment != selectedFragment) {
                    navController.navigate(selectedFragment, null, navOptions)
                    true
                } else {
                    false
                }
            }
        }catch(e : Exception){
            Log.d("Error in set nav main activity","${e.message}")
        }
    }
    open fun showWhiteScreenLoading(isShow : Boolean){
       if(isShow){
           binding.apply {
               animationView.progress = 0f
               whiteLoadingLayout.visibility = View.VISIBLE
               animationView.playAnimation()
           }
       }
        else{
            binding.apply {
                animationView.cancelAnimation()
                whiteLoadingLayout.visibility = View.GONE
            }
       }
    }
    override fun onStop() {
        super.onStop()
            changeUserStatus(false)
    }
}