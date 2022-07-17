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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.markersyandexmap.R
import com.example.markersyandexmap.databinding.FragmentMapBinding
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), LocationListener, InputListener {

    private val placeViewModel: PlaceViewModel by viewModels()

    private var listPlaces = emptyList<Place>()
    private var zoom = 16f

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
            : View {
        val binding = FragmentMapBinding.inflate(
            layoutInflater,
            container,
            false
        )

        mapView = binding.map
        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
        locationManager = MapKitFactory.getInstance().createLocationManager()
        marksObject = mapView.map.mapObjects.addCollection()
        mapView.map.addInputListener(this)

        binding.userLocation.setOnClickListener {
            try {
                moveCamera(userLocationLayer.cameraPosition()?.target!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Повторите попытку", Toast.LENGTH_SHORT).show()
            }
        }

        binding.places.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_placesFragment)
        }

        binding.fabPlus.setOnClickListener {
            zoom += 0.5f
            val point = mapView.mapWindow.map.cameraPosition.target
            mapView.map.move(CameraPosition(point, zoom, 0F, 0F))
        }

        binding.fabMinus.setOnClickListener {
            zoom -= 0.5f
            val point = mapView.mapWindow.map.cameraPosition.target
            mapView.map.move(CameraPosition(point, zoom, 0F, 0F))
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

        return binding.root
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
            background = AppCompatResources.getDrawable(context, R.drawable.ic_baseline_place_24)
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
        placeViewModel.edit(Place.empty)
        findNavController().navigate(R.id.action_mapFragment_to_newPlaceFragment, bundle)
    }

    override fun onLocationUpdated(p0: Location) {}

    override fun onLocationStatusUpdated(p0: LocationStatus) {}
}