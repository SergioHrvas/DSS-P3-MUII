package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.Manifest
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.Serializable

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    // Definir el código de solicitud
    companion object {
        private const val LOCATION_REQUEST_CODE = 100
    }

    private var userMarker: Marker? = null  // Variable para almacenar el marcador del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view)


        val storeLocations = listOf(
            mapOf("loc" to GeoPoint(37.19554309563983, -3.6268487987321323), "name" to "Almacén Bellas Artes", "color" to "red"),
            mapOf("loc" to GeoPoint(37.19671719732779, -3.6244666179139444), "name" to "Almacén ETSIIT", "color" to "purple"),
            mapOf("loc" to GeoPoint(37.17970832110029, -3.6095428055522327), "name" to "Almacén Facultad de Ciencias", "color" to "blye"),
            mapOf("loc" to GeoPoint(37.19830677588794, -3.629498442349833), "name" to "Almacén Edif Aux ETSIIT", "color" to "green")

        )

        // Verificar si el permiso de ubicación está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Si no está concedido, solicitar el permiso
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }

        // Inicializar el MapView
        mapView = findViewById<MapView>(R.id.mapa)
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Usar el estilo predeterminado de OSM
        mapView.setMultiTouchControls(true) // Habilitar gestos multitáctiles

        addStoreMarkers(storeLocations)

        getUserLocation()

        // Configurar zoom y posición inicial
        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(37.19, -3.62)
        mapController.setCenter(startPoint)
    }

    private fun addStoreMarkers(locations: List<Map<String, Serializable>>) {
        for (location in locations) {
            val storeMarker = Marker(mapView)
            storeMarker.position = location["loc"] as GeoPoint
            storeMarker.title = location["name"] as String
            storeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            var markerIcon = resources.getDrawable(R.drawable.ic_marker_red)

            if(location["color"] as String == "red")
                markerIcon = resources.getDrawable(R.drawable.ic_marker_red)
            if(location["color"] as String == "green")
                markerIcon = resources.getDrawable(R.drawable.ic_marker_green)
            if(location["color"] as String == "blue")
                markerIcon = resources.getDrawable(R.drawable.ic_marker_blue)
            if(location["color"] as String == "purple")
                markerIcon = resources.getDrawable(R.drawable.ic_marker_purple)

            storeMarker.icon = markerIcon
            mapView.overlays.add(storeMarker)
        }
    }

    private fun updateMarker(userLocation: GeoPoint) {
        if (userMarker != null) {
            mapView.overlays.remove(userMarker)
        }

        userMarker = Marker(mapView).apply {
            position = userLocation
            title = "Tu ubicación"
        }
        mapView.overlays.add(userMarker)
    }

    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    mapView.controller.setCenter(userLocation)
                    updateMarker(userLocation)
                }
            }

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    mapView.controller.setCenter(userLocation)
                    updateMarker(userLocation)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
