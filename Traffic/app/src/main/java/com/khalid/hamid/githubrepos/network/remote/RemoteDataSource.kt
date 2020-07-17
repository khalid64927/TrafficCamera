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

package com.khalid.hamid.githubrepos.network.remote

import com.khalid.hamid.githubrepos.network.ZulkheService
import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.network.Result.Error_
import com.khalid.hamid.githubrepos.network.Result.Success
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import javax.inject.Inject
import timber.log.Timber

class RemoteDataSource @Inject constructor(
    private val zulkheService: ZulkheService
) {

    suspend fun fetchLTAData():Result<GetTrafficResponse> {
        Timber.d("fetchLTAData")
        try{
            val ltaData = zulkheService.fetchLTATrafficData()
            if(ltaData.isSuccessful){
                return Success(ltaData?.body() ?: GetTrafficResponse(emptyList(), Api_info("")))
            }
            return Error_(Exception(ltaData?.message()))
        }catch (error : Exception){
            Timber.e(error, "failed in remote fetch")
        }
        return Error_(Exception("failed"))
    }

}
