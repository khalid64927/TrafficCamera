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

import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.utilities.Prefs
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class LocalDataSourceTest {

    lateinit var pref: Prefs

    lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        pref = Mockito.mock(Prefs::class.java)
        localDataSource = LocalDataSource(pref)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun fetchLTAData_Success() {
        val data = runBlocking {
            localDataSource.fetchTrafficDetails()
        }
        Assert.assertEquals(data, Result.Success(GetTrafficResponse(emptyList(), Api_info(""))))
    }

    @Test
    fun fetchLTAData_Failed() {
        val data = runBlocking {
            localDataSource.fetchTrafficDetails()
        }
        Assert.assertEquals(data, Result.Success(GetTrafficResponse(emptyList(), Api_info(""))))
    }
}
