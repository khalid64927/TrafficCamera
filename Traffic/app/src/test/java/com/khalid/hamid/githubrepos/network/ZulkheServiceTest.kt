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

package com.khalid.hamid.githubrepos.network

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class ZulkheServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var zulkheService: ZulkheService

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        zulkheService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZulkheService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchLTATrafficData() {
        enqueueResponse("traffic-fetch-success.json")
        val value: Response<GetTrafficResponse> = runBlocking {
            zulkheService.fetchLTATrafficData()
        }
        val data: GetTrafficResponse = value.body() ?: GetTrafficResponse(emptyList(), Api_info(""))
        val size: Int = data.items.size
        val camListSize: Int = data.items[0].cameras.size
        assertThat(size, `is`(1))
        assertThat(camListSize, `is`(87))
        Assert.assertTrue(value.isSuccessful)
        assertThat(
            data.api_info.status, `is`("healthy")
        )
        assertThat(
            data.items[0].timestamp, `is`("2020-07-07T12:39:20+08:00")
        )
        assertThat(data.items[0].cameras[0].location.latitude, `is`(1.323604823))
        assertThat(data.items[0].cameras[0].location.longitude, `is`(103.8587802))
        assertThat(data.items[0].cameras[0].image, `is`("https://images.data.gov.sg/api/traffic-images/2020/07/ff2551c3-e569-4cc6-a99d-27bd583202b4.jpg"))
        assertThat(data.items[0].cameras[0].image_metadata.height, `is`(480))
        assertThat(data.items[0].cameras[0].image_metadata.width, `is`(640))
        assertThat(data.items[0].cameras[0].image_metadata.md5, `is`("c237e1221b13ffb40f5d038fd91b65d0"))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val name = "api-response/$fileName"
        val inputStream = javaClass.classLoader?.getResourceAsStream(name)
        val source = inputStream?.let { Okio.source(it) }?.let { Okio.buffer(it) }
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source?.readString(Charsets.UTF_8))
        )
    }
}
