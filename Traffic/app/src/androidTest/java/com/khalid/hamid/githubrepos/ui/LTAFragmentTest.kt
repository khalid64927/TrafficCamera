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
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.khalid.hamid.githubrepos.R
import com.khalid.hamid.githubrepos.network.Resource
import com.khalid.hamid.githubrepos.network.Status
import com.khalid.hamid.githubrepos.testing.SingleFragmentActivity
import com.khalid.hamid.githubrepos.utilities.*
import com.khalid.hamid.githubrepos.vo.lta.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock

/**
 * Large End-to-End test for the RepoFragment module.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class LTAFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    private lateinit var viewModel: LTAViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private val testFragment = TestUserFragment()

    private val error_item = Resource(Status.ERROR, GetTrafficResponse(emptyList(), Api_info("")), " error happened")
    private val loading_item = Resource(Status.LOADING, GetTrafficResponse(emptyList(), Api_info("")), " error happened")

    val mld = MutableLiveData<Resource<GetTrafficResponse>>()

    @Before
    fun init() {
        viewModel = mock(LTAViewModel::class.java)
        Mockito.`when`(viewModel._items).thenReturn(mld)
        mockBindingAdapter = mock(FragmentBindingAdapters::class.java)

        testFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        activityRule.activity.setFragment(testFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun success() {
        val location = Location(1.28569398886979, 103.837524510188)
        val img_meta = Image_metadata(480, 640, "md5")
        val camera = Cameras("2020-07-07T12:39:20+08:00", "https://images.data.gov.sg/api/traffic-images/2020/07/ea3879b1-8318-405a-b787-5a03f196cc3f.jpg", location, 1704, img_meta)

        val cameralist: MutableList<Cameras> = mutableListOf()
        cameralist.add(camera)

        val item = Items("2020-07-07T12:39:20+08:00", cameralist)
        val itemList = listOf(item)

        mld.postValue(Resource.success(GetTrafficResponse(itemList, Api_info(""))))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
    }

    class TestUserFragment : LTAFragment()
}
