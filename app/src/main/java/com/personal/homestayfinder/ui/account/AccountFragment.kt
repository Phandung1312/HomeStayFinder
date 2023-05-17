package com.personal.homestayfinder.ui.account

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.Constant
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.databinding.AccountClass
import com.personal.homestayfinder.activities.login.LoginActivity
import com.personal.homestayfinder.base.dialogs.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment<AccountClass>(AccountClass::inflate) {
    @Inject
    lateinit var gsc: GoogleSignInClient
    @Inject
    lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<AccountViewModel>()
    override fun initView() {
        showBottomNavView()
    }

    override fun initListeners() {
        dataBinding.ivbRoomPosted.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections
                .actionAccountFragmentToFragmentRoomsPosted(currentUser.uid))
        }
        dataBinding.layoutLogout.setOnClickListener {
            showConfirmMessage(
                title = R.string.confirm_note,
                message = R.string.confirm_logout,
                positiveTitleResourceId = R.string.confirm_agree,
                negativeTitleResourceId = R.string.confirm_disagree,
                callback = object : ConfirmDialog.ConfirmCallback{
                    override fun negativeAction() {

                    }

                    override fun positiveAction() {
                        logout()
                    }

                }
            )
        }
        registerObserverSmallLoadingEvent(viewModel, viewLifecycleOwner)
        registerAllExceptionEvent(viewModel,viewLifecycleOwner)
    }

    override fun initData() {
        // Get local user info
        dataBinding.user = User(
            currentUser.uid,
            currentUser.email,
            currentUser.displayName,
            currentUser.photoUrl.toString()
        )
        // Update user info
        viewModel.getUserById(currentUser.uid).observe(viewLifecycleOwner){ user ->
            dataBinding.user = user
            dataBinding.executePendingBindings()
        }
    }
    private fun logout() {
        lifecycleScope.launch {
            val job = async { viewModel.signOut(currentUser.uid) }
            job.await()
            auth.signOut()
            gsc.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    showErrorMessage(Constant.SIGN_OUT_ERROR)
                }
            }
        }
    }
}