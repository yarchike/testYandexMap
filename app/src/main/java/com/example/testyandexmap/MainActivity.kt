package com.example.testyandexmap

import android.Manifest
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var userLocationLayer: UserLocationLayer
    private var initializerUserPosition: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapview.map.move(
            CameraPosition(Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        requestLocationPermission()
        mapview.map.addCameraListener(cameraListener)
    }

    override fun onStop() {
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapview.onStart()
    }

    private fun initUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapview.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(listenerLocationObjectListener)
    }

    private val listenerLocationObjectListener = object : UserLocationObjectListener {
        override fun onObjectAdded(userLocationView: UserLocationView) {
            userLocationLayer.setAnchor(
                PointF(
                    (mapview.width * 0.5).toFloat(),
                    (mapview.height * 0.5).toFloat()
                ),
                PointF(
                    (mapview.width * 0.5).toFloat(),
                    (mapview.height * 0.83).toFloat()
                )
            )
            initializerUserPosition = true
        }

        override fun onObjectRemoved(p0: UserLocationView) {
        }

        override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        }
    }


    private fun requestLocationPermission() {
        resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initUserLocation()
            }
        }

    private val cameraListener: CameraListener =
        CameraListener { map, cameraPosition, cameraUpdateReason, b ->
            if (b) {
                if (initializerUserPosition) {
                    userLocationLayer.resetAnchor()
                    userLocationLayer.isVisible = false
                    userLocationLayer.isHeadingEnabled = false
                }
            }
        }

}