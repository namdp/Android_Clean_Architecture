package com.namdinh.cleanarchitecture.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.namdinh.cleanarchitecture.core.di.helper.Injectable
import com.namdinh.cleanarchitecture.core.helper.autoCleared
import com.namdinh.cleanarchitecture.presentation.base.viewmodel.BaseViewModel
import javax.inject.Inject

abstract class BaseFragment<View : ViewDataBinding, Model : BaseViewModel> : Fragment(), Injectable {
    protected var binding by autoCleared<View>()
    protected lateinit var viewModel: Model
    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var navController: NavController

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View {
        binding = bindingView(inflater, container, layoutId)
        navController = findNavController(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = initViewModel()
    }

    protected abstract fun initViewModel(): Model

    private fun bindingView(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layoutId: Int): View {
        return DataBindingUtil.inflate(inflater, layoutId, container, false)
    }
}
