package com.namdinh.cleanarchitecture.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.namdinh.cleanarchitecture.core.helper.AbsentLiveData
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import com.namdinh.cleanarchitecture.domain.repository.UserRepository
import com.namdinh.cleanarchitecture.domain.vo.Repo
import com.namdinh.cleanarchitecture.domain.vo.User
import com.namdinh.cleanarchitecture.presentation.base.viewmodel.BaseViewModel
import javax.inject.Inject

class UserViewModel
@Inject constructor(userRepository: UserRepository, repoRepository: RepoRepository) : BaseViewModel() {
    private val login = MutableLiveData<String>()

    val repositories: LiveData<Resource<List<Repo>>> = Transformations
        .switchMap(login) { login ->
            if (login == null) {
                AbsentLiveData.create()
            } else {
                LiveDataReactiveStreams.fromPublisher(repoRepository.loadRepos(login))
            }
        }

    val user: LiveData<Resource<List<User>>> = Transformations
        .switchMap(login) { login ->
            if (login == null) {
                AbsentLiveData.create()
            } else {
                LiveDataReactiveStreams.fromPublisher(userRepository.loadUser(login))
            }
        }

    fun setLogin(login: String?) {
        if (this.login.value != login) {
            this.login.value = login
        }
    }

    fun retry() {
        login.value?.let {
            login.value = it
        }
    }
}
