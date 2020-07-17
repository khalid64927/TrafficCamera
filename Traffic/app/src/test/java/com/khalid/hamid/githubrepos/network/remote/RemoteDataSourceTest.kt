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

package com.khalid.hamid.githubrepos.network.remote

import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.network.ZulkheService
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response

class RemoteDataSourceTest {

    lateinit var remoteDataSource: RemoteDataSource
    lateinit var service: ZulkheService

    @Before
    fun setUp() {
        service = Mockito.mock(ZulkheService::class.java)
        remoteDataSource = RemoteDataSource(service)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun fetchLTAData_Success() {
        var data = GetTrafficResponse(emptyList(), Api_info(""))
        var response: Response<GetTrafficResponse> = Mockito.mock(Response::class.java) as Response<GetTrafficResponse>
        runBlocking {
            Mockito.`when`(service.fetchLTATrafficData()).thenReturn(response)
        }

        Mockito.`when`(response.isSuccessful).thenReturn(true)
        Mockito.`when`(response.body()).thenReturn(data)

        val result = runBlocking {
            remoteDataSource.fetchLTAData()
        }
        assert(result is Result.Success)
    }

    @Test
    fun fetchLTAData_Failed() {
        val data = Result.Error_(Exception("failed"))
        var response = Mockito.mock(Response::class.java)
        runBlocking {
            Mockito.`when`(service.fetchLTATrafficData()).thenReturn(response as Response<GetTrafficResponse>)
        }

        Mockito.`when`(response.isSuccessful).thenReturn(false)
        Mockito.`when`(response.message()).thenReturn("failed")

        val result = runBlocking {
            remoteDataSource.fetchLTAData()
        }
        assert(result is Result.Error_)
    }
}
