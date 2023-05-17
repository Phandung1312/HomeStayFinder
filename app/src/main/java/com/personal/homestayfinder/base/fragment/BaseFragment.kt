package com.personal.homestayfinder.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.personal.homestayfinder.base.activities.BaseActivity
import com.personal.homestayfinder.R
import com.personal.homestayfinder.activities.main.MainActivity
import com.personal.homestayfinder.base.dialogs.ConfirmDialog
import com.personal.homestayfinder.base.viewmodel.BaseViewModel
import com.personal.homestayfinder.common.Constant
import com.personal.homestayfinder.common.EventObserver

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewDataBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _dataBinding: VB? = null
    val dataBinding get() = _dataBinding!!
    lateinit var currentUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dataBinding = inflate.invoke(inflater, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        val auth = Firebase.auth
        auth.currentUser?.let {
            currentUser = it
        } ?: {
            showErrorMessage(Constant.AUTH_ERROR)
        }
        initView()
        initListeners()
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    abstract fun initView()
    abstract fun initListeners()
    abstract fun initData()
    protected fun navigateToPage(actionId: Int) {
        findNavController().navigate(actionId)
    }

    open fun showFragmentLoading(isShow: Boolean){
        val activity = requireActivity()
        if(activity is MainActivity){
            activity.showWhiteScreenLoading(isShow)
        }
    }

    open fun showSmallLoading(isShow: Boolean) {
        dataBinding.run {
            setVariable(BR.isShowLoading, isShow)
            executePendingBindings()
        }
    }

    open fun showBottomNavView() {
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.binding.bottomNavigation.visibility = View.VISIBLE
        }
    }

    open fun hideBottomNavView() {
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.binding.bottomNavigation.visibility = View.GONE
        }
    }

    open fun showScreenLoading(isShow: Boolean) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showScreenLoading(isShow)
        }
    }

    protected fun showErrorMessage(e: FirebaseNetworkException) {
        showErrorMessage(Constant.NETWORK_ERROR)
    }

    protected fun showErrorMessage(e: FirebaseAuthException) {
        showErrorMessage(Constant.AUTH_ERROR)
    }

    protected fun showErrorMessage(messageId: Int) {
        val message = requireContext().getString(messageId)
        showErrorMessage(message)
    }

    protected fun showConfirmMessage(
        title: Int, message: Int = -1, positiveTitleResourceId: Int,
        negativeTitleResourceId: Int, callback: ConfirmDialog.ConfirmCallback
    ) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showConfirmDialog(
                titleResourceId = title,
                messageResourceId = message,
                positiveTitleResourceId = positiveTitleResourceId,
                negativeTitleResourceId = negativeTitleResourceId,
                callback
            )
        }
    }

    protected fun showErrorMessage(message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showErrorDialog(message)
        }
    }

    protected fun showNotify(title: String?, message: String) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showNotifyDialog(title ?: getDefaultNotifyTitle(), message)
        }
    }

    protected fun showNotify(titleId: Int = R.string.default_notify_title, messageId: Int) {
        val activity = requireActivity()
        if (activity is BaseActivity) {
            activity.showNotifyDialog(titleId, messageId)
        }
    }

    protected fun registerObserverFragmentLoadingEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.isFragmentLoading.observe(viewLifecycleOwner, EventObserver{
            showFragmentLoading(it)
        })
    }

    protected fun registerObserverNetworkExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.firebaseNetworkException.observe(viewLifecycleOwner, EventObserver {
            showErrorMessage(it)
        })
    }

    protected fun registerObserverAuthExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.firebaseAuthException.observe(viewLifecycleOwner, EventObserver {
            showErrorMessage(it)
        })
    }

    protected fun registerObserverUnknownExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.unknownExceptionString.observe(viewLifecycleOwner, EventObserver {
            showErrorMessage(it)
        })
    }

    protected fun registerObserverMessageEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.errorMessageResourceId.observe(viewLifecycleOwner, EventObserver { message ->
            showErrorMessage(message)
        })
    }

    protected fun registerObserverLoadingMoreEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.isLoadingMore.observe(viewLifecycleOwner, EventObserver { isShow ->
            showLoadingMore(isShow)
        })
    }

    protected fun showLoadingMore(isShow: Boolean) {

    }


    private fun getDefaultNotifyTitle(): String {
        return getString(R.string.default_notify_title)
    }

    protected fun registerAllExceptionEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        registerObserverAuthExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverNetworkExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverUnknownExceptionEvent(viewModel, viewLifecycleOwner)
        registerObserverMessageEvent(viewModel, viewLifecycleOwner)
    }

    protected fun registerObserverSmallLoadingEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.isSmallLoading.observe(viewLifecycleOwner, EventObserver { isShow ->
            showSmallLoading(isShow)
        })
    }

    protected fun registerObserverScreenLoading(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.isScreenLoading.observe(viewLifecycleOwner, EventObserver { isShow ->
            showScreenLoading(isShow)
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}