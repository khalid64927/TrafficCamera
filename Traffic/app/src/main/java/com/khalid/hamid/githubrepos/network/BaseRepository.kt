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
 */

package com.khalid.hamid.githubrepos.network

import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class BaseRepository(private val baseDataSource: BaseDataSource) : BaseDataSource {

    override suspend fun fetchTrafficDetails(): Result<GetTrafficResponse> {
        Timber.d("fetchTraffic")
        return baseDataSource.fetchTrafficDetails()
    }
}