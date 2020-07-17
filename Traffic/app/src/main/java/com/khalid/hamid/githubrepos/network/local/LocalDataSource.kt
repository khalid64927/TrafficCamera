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

package com.khalid.hamid.githubrepos.network.local

import com.khalid.hamid.githubrepos.network.BaseDataSource
import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.network.Result.Success
import com.khalid.hamid.githubrepos.utilities.EspressoIdlingResource
import com.khalid.hamid.githubrepos.utilities.Prefs
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import javax.inject.Inject
import timber.log.Timber

/**
 * This will always fetch data from database
 *
 */
open class LocalDataSource @Inject constructor(
    private val pref: Prefs
) : BaseDataSource {

    val TWOHOURS: Long = 2 * 60 * 60 * 1000

    fun hasCacheExpired(): Boolean {
        Timber.d("hasCacheExpired")
        if (pref.cachedTime.isEmpty()) {
            Timber.d("no last saved time ${EspressoIdlingResource.countingIdlingResource.getCounterVal()}")
            return true
        }

        val currentTime = System.currentTimeMillis()
        val prevTime = pref.cachedTime.toLong()
        val elapsedTime = currentTime - prevTime
        if (elapsedTime >= TWOHOURS) {
            Timber.d("cache expired ${EspressoIdlingResource.countingIdlingResource.getCounterVal()}")
            return true
        }
        return false
    }

    override suspend fun fetchTrafficDetails(): Result<GetTrafficResponse> {
        /**
         *  for now just sending dummy success instead from DB as this is out of scope
         *  Success(GetTrafficResponse(emptyList(), Api_info("")))
         *  */
        return Success(GetTrafficResponse(emptyList(), Api_info("")))
    }
}
