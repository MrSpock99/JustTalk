package itis.ru.justtalk.ui.words.groups

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.justtalk.interactor.WordsInteractor
import itis.ru.justtalk.models.db.Group
import itis.ru.justtalk.ui.base.BaseViewModel
import itis.ru.justtalk.utils.Response
import javax.inject.Inject

class GroupsViewModel @Inject constructor(
    private val interactor: WordsInteractor
) : BaseViewModel() {
    val groupOperationsLiveData = MutableLiveData<Response<Boolean>>()
    val allGroupsLiveData = MutableLiveData<Response<List<Group>>>()

    fun addGroup(data: Intent?) {
        val group = Group(
            name = data?.getStringExtra(ARG_GROUP_NAME).toString(),
            imageUrl = data?.getStringExtra(ARG_IMAGE_URL).toString()
        )
        disposables.add(
            interactor.addGroup(group)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    groupOperationsLiveData.value = Response.success(true)
                }, { error ->
                    groupOperationsLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }

    fun getGroups() {
        disposables.add(
            interactor.getGroups()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    allGroupsLiveData.value = Response.success(it)
                }, { error ->
                    allGroupsLiveData.value = Response.error(error)
                    error.printStackTrace()
                })
        )
    }

    fun deleteGroup(group: Group) {
        disposables.add(
            interactor.getGroupWithWords(group.id)
                .map {
                    interactor.deleteGroup(it)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            groupOperationsLiveData.value = Response.success(true)
                        }, { error ->
                            groupOperationsLiveData.value = Response.error(error)
                            error.printStackTrace()
                        })
                    it
                }.subscribe()
        )
    }
}
