/*
 * Copyright 2020 Mohammed Khalid Hamid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * My github profile link : https://github.com/khalid64927
 *
 */
package com.khalid.hamid.githubrepos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalid.hamid.githubrepos.network.BaseRepository
import com.khalid.hamid.githubrepos.network.Resource
import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.network.Status
import com.khalid.hamid.githubrepos.utilities.EspressoIdlingResource
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse

import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LTAViewModel @Inject constructor(val baseRepository: BaseRepository) : ViewModel() {

    val _items = MutableLiveData<Resource<GetTrafficResponse>>().apply { value = Resource(
        Status.LOADING, GetTrafficResponse(emptyList(), Api_info("")), "wait") }
    private val error_item = Resource(Status.ERROR, GetTrafficResponse(emptyList(), Api_info("")), " error happened")

    fun refreshMap(){
        // update map
        Timber.d("refreshMap")
        // notify to show loading indicator
        _items.value = Resource(
            Status.LOADING, GetTrafficResponse(emptyList(), Api_info("")), "wait")
        EspressoIdlingResource.increment()
        viewModelScope.launch {
           val data = baseRepository.fetchTrafficDetails()
            EspressoIdlingResource.decrement()
            if(data is Result.Success){
                Timber.d("data %s" , data.data.toString())
                _items.value = Resource(Status.SUCCESS, data.data, "success yay !")
            } else {
                Timber.d("data %s" , (data as Error).localizedMessage)
                _items.value = Resource(Status.ERROR, GetTrafficResponse(emptyList(), Api_info("")), data.toString())
            }
        }

    }
}