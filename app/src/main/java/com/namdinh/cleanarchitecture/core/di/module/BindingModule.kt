package com.namdinh.cleanarchitecture.core.di.module

import com.namdinh.cleanarchitecture.core.binding.adapter.ImageViewBindingAdapter
import com.namdinh.cleanarchitecture.core.di.scope.DataBinding
import dagger.Module
import dagger.Provides

@Module
class BindingModule {
    @DataBinding
    @Provides
    fun provideImageViewBindingAdapter(): ImageViewBindingAdapter {
        return ImageViewBindingAdapter()
    }
}
