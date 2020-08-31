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

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.khalid.hamid.githubrepos.R
import com.khalid.hamid.githubrepos.databinding.FragmentLtaBinding
import com.khalid.hamid.githubrepos.di.Injectable
import com.khalid.hamid.githubrepos.network.Resource
import com.khalid.hamid.githubrepos.network.Status
import com.khalid.hamid.githubrepos.utilities.*
import com.khalid.hamid.githubrepos.vo.lta.Api_info
import com.khalid.hamid.githubrepos.vo.lta.Cameras
import com.khalid.hamid.githubrepos.vo.lta.GetTrafficResponse
import com.khalid.hamid.githubrepos.vo.lta.Location
import timber.log.Timber
import javax.inject.Inject

open class LTAFragment:
    Fragment(),
    Injectable,
    OnMapReadyCallback{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var app: Application

    private lateinit var mMap: GoogleMap

    val markerMap : MutableMap<MarkerOptions, Cameras> = HashMap()

    var binding by autoCleared<FragmentLtaBinding>()
    val ltaViewModel: LTAViewModel by viewModels {
        viewModelFactory
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val ltsLiveData = ltaViewModel._items
        ltsLiveData.observe(viewLifecycleOwner, Observer { ltaResponse ->
            run {
                checkReadyThen {
                    when (ltaResponse.status) {
                        Status.SUCCESS -> stateSuccess(ltaResponse)
                        Status.ERROR -> stateError(ltaResponse)
                        Status.LOADING -> stateLoading(ltaResponse)
                    }
                }
            }
        })
    }

    private fun stateSuccess( ltaResponse : Resource<GetTrafficResponse>){
        Timber.d("success %s", ltaResponse.data.toString())
        setMarkerMap(ltaResponse.data ?: GetTrafficResponse(emptyList(), Api_info("")))
        addMarkers(markerMap)
        ltaResponse.data?.let {
            // animating on Map only first time on first pin
            if(!isZoomedIn){
                zoomToPin(it.items[0].cameras[0].location)
                isZoomedIn = true
            }
        }
        val timestamp = ltaResponse.data?.let { it.items[0].timestamp }
        if (binding.timestamp.visibility == View.INVISIBLE) {
            binding.timestamp.visibility = View.VISIBLE
        }
        val prefix = "Last updated : "
        if(!binding.timestamp.text.equals(prefix + timestamp)){
            binding.timestamp.text = prefix + timestamp
        }
        Toast.makeText(activity, "refresh of traffic data completed", Toast.LENGTH_SHORT).show()
    }

    private fun stateError( ltaResponse : Resource<GetTrafficResponse>){
        Timber.d("error ")
        Toast.makeText(activity, "Failed to get latest traffic data", Toast.LENGTH_SHORT).show()
        binding.timestamp.visibility = View.INVISIBLE
    }

    private fun stateLoading( ltaResponse : Resource<GetTrafficResponse>){
        Timber.d("loading ")
        Toast.makeText(activity, "updating traffic data", Toast.LENGTH_SHORT).show()
        if (binding.timestamp.visibility == View.INVISIBLE) {
            binding.timestamp.visibility = View.VISIBLE
        }
        binding.timestamp.text = "updating..."
    }


    private var isZoomedIn = false;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Timber.d("onCreateView")

        val dataBinding = DataBindingUtil.inflate<FragmentLtaBinding>(
            inflater,
            R.layout.fragment_lta,
            container,
            false
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding = dataBinding

        Timber.d("onCreateView")
        setHasOptionsMenu(true)
        return binding.repoRoot
    }



    val runnable: Runnable = run {
        Runnable {
            // hit api service
            ltaViewModel.refreshMap()
            handler.postDelayed(runnable, ONE_MINUTE)

        }
    }

    private val handler = Handler(Looper.getMainLooper())

    /**
     * LTA data is refreshed on the map every 1 minute
    */
    val ONE_MINUTE = 1000 * 60L

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, ONE_MINUTE )
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("onMapReady")
        mMap = googleMap
        mMap.setInfoWindowAdapter(CustomInfoWindowOfMarker(this))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        // hit api service
        ltaViewModel.refreshMap()

    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_sort_name -> true
            // sort adapter by stars
            R.id.menu_sort_star -> true
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Timber.d("onCreateOptionsMenu")
        inflater.inflate(R.menu.arrange_list, menu)
    }

    /**
     * Created to be able to override in tests
     */
    open fun navController() = findNavController()

    /**
     * Checks if the map is ready, the executes the provided lambda function
     *
     * @param stuffToDo the code to be executed if the map is ready
     */
    private fun checkReadyThen(stuffToDo : () -> Unit) {
        if (!::mMap.isInitialized) {
            Toast.makeText(activity, "Map is not ready!", Toast.LENGTH_SHORT).show()
        } else {
            stuffToDo()
        }
    }

    private fun addMarkers(map : Map<MarkerOptions, Cameras>){
        mMap.clear()
        //mMap.setInfoWindowAdapter(CustomInfoWindowOfMarker(this))
        map.forEach{(markerOptions, camera) ->
            run {
                val marker = mMap.addMarker(markerOptions)
                marker.tag = camera

            }
        }
    }
    private fun setMarkerMap(ltaData : GetTrafficResponse){
        val cameraList = ltaData.items.get(0).cameras;
        for ( camera in cameraList){
            val options = getMarkerOptions(camera)
            markerMap[options] = camera
        }
    }

    private fun getMarkerOptions(cameras: Cameras): MarkerOptions{
        val gps = LatLng(cameras.location.latitude, cameras.location.longitude)
        val options = MarkerOptions()
        options.title(cameras.timestamp)
        options.position(gps)
        return options
    }

    private fun zoomToPin(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
    }
}