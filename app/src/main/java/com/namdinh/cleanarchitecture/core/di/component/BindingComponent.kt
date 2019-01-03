package com.namdinh.cleanarchitecture.core.di.component

import androidx.databinding.DataBindingComponent
import com.namdinh.cleanarchitecture.core.binding.adapter.ImageViewBindingAdapter
import com.namdinh.cleanarchitecture.core.di.module.BindingModule
import com.namdinh.cleanarchitecture.core.di.scope.DataBinding
import dagger.Component

@DataBinding
@Component(dependencies = [AppComponent::class], modules = [BindingModule::class])
interface BindingComponent : DataBindingComponent {
    override fun getImageViewBindingAdapter(): ImageViewBindingAdapter
}
