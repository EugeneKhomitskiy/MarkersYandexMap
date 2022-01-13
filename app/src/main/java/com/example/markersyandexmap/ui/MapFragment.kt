package com.example.markersyandexmap.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.dto.Place
import com.example.markersyandexmap.viewmodel.PlaceViewModel
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationManager
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.ui_view.ViewProvider

class MapFragment : Fragment(), LocationListener, InputListener {

    private val placeViewModel: PlaceViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var listPlaces = emptyList<Place>()

    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var marksObject: MapObjectCollection
    private lateinit var locationManager: LocationManager

    private var position: Point? = null

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                mapView.apply {
                    userLocationLayer.isVisible = true
                    userLocationLayer.isHeadingEnabled = false
                }
            } else {
                Toast.makeText(context, R.string.permission_deny, Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.map)
        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
        locationManager = MapKitFactory.getInstance().createLocationManager()
        marksObject = mapView.map.mapObjects.addCollection()
        mapView.map.addInputListener(this)

        view.findViewById<View>(R.id.user_location).setOnClickListener {
            moveCamera(userLocationLayer.cameraPosition()?.target!!)
        }

        placeViewModel.data.observe(viewLifecycleOwner) {
            listPlaces = it
            addMakers()
        }

        position = Point(
            arguments?.getDouble("lat") ?: 55.75222,
            arguments?.getDouble("lng") ?: 37.61556
        )

        checkPermission()
        moveCamera(position!!)

        return view
    }

    private fun moveCamera(point: Point) {
        mapView.map.move(CameraPosition(point, 16F, 0F, 0F))
    }

    private fun addMakers(){
        for (i in listPlaces) {
            addMarker(Point(i.latitude, i.longitude))
        }
    }

    private fun addMarker(point: Point) {
        val marker = View(context).apply {
            background = context.getDrawable(R.drawable.ic_baseline_place_24)
        }
        mapView.map.mapObjects.addPlacemark(
            point,
            ViewProvider(marker)
        )
    }

    private fun checkPermission() {
        when {
            // 1. Проверяем есть ли уже права
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                mapView.apply {
                    userLocationLayer.isVisible = true
                    userLocationLayer.isHeadingEnabled = false
                }

                val fusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(requireActivity())

                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    println(it)
                }
            }
            // 2. Должны показать обоснование необходимости прав
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // TODO: show rationale dialog
            }
            // 3. Запрашиваем права
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onMapTap(map: Map, point: Point) {
        mapView.map.deselectGeoObject()
    }

    override fun onMapLongTap(map: Map, point: Point) {
        val bundle = Bundle().apply {
            putDouble("latitude", point.latitude)
            putDouble("longitude", point.longitude)
        }
        findNavController().navigate(R.id.action_mapFragment_to_newPlaceFragment, bundle)
    }

    override fun onLocationUpdated(p0: Location) {}

    override fun onLocationStatusUpdated(p0: LocationStatus) {}
}