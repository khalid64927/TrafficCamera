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

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.khalid.hamid.githubrepos.R
import com.khalid.hamid.githubrepos.vo.lta.Cameras
import timber.log.Timber

class CustomInfoWindowOfMarker : GoogleMap.InfoWindowAdapter {
    var context: Fragment
    constructor(fragment: LTAFragment) {
        context = fragment
    }
    lateinit var markerItem: Marker
    lateinit var item: Cameras
    var isImageNotLoaded = true
    var map: MutableMap<String, Boolean> = HashMap()

    override fun getInfoContents(marker: Marker): View {
        val imageView = context.layoutInflater.inflate(R.layout.view_marker_window, null)
        val cameras = marker.tag as Cameras
        item = cameras
        markerItem = marker
        if (map.get(cameras.image) == null) {
            Timber.d(" start image loading")
            Glide.with(context)
                .load(cameras.image)
                .override(600, 200)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Timber.d("onLoadFailed")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Timber.d("onResourceReady image is now available %s", item.image)
                        map.put(cameras.image, true)
                        markerItem.showInfoWindow()
                        return false
                    }
                }).into(imageView as ImageView)
        } else {
            var options = RequestOptions()
            options.onlyRetrieveFromCache(true)
            Timber.d("Image is already loaded")
            Glide.with(context)
                .apply {
                    options
                }
                .load(cameras.image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .override(600, 200)
                .centerCrop()
                .into(imageView as ImageView)
        }

        return imageView
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}
