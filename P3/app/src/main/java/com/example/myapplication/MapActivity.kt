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
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.Serializable
class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private var locationCallback: LocationCallback? = null

    companion object {
        private const val LOCATION_REQUEST_CODE = 100
    }

    private var userMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view)

        val storeLocations = listOf(
            mapOf("loc" to GeoPoint(37.19554309563983, -3.6268487987321323), "name" to "Almacén Bellas Artes", "color" to "red"),
            mapOf("loc" to GeoPoint(37.19671719732779, -3.6244666179139444), "name" to "Almacén ETSIIT", "color" to "purple"),
            mapOf("loc" to GeoPoint(37.17970832110029, -3.6095428055522327), "name" to "Almacén Facultad de Ciencias", "color" to "blue"),
            mapOf("loc" to GeoPoint(37.19830677588794, -3.629498442349833), "name" to "Almacén Edif Aux ETSIIT", "color" to "green")
        )

        // Verificar permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }

        // Encontrar el botón de retroceso
        val buttonBack: ImageButton = findViewById(R.id.buttonBack)

        // Hacer visible el botón de retroceso
        buttonBack.visibility = View.VISIBLE

        // Configurar la acción de retroceso
        buttonBack.setOnClickListener {
            onBackPressed()  // Llamada para volver a la actividad anterior
        }

        // Inicializar el MapView
        mapView = findViewById(R.id.mapa)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        addStoreMarkers(storeLocations)
        getUserLocation()

        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(37.19, -3.62)
        mapController.setCenter(startPoint)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()

        // Detener actualizaciones de ubicación para evitar conflictos
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    private fun addStoreMarkers(locations: List<Map<String, Serializable>>) {
        for (location in locations) {
            val storeMarker = Marker(mapView)
            storeMarker.position = location["loc"] as GeoPoint
            storeMarker.title = location["name"] as String
            storeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            val markerIcon = when (location["color"] as String) {
                "red" -> resources.getDrawable(R.drawable.ic_marker_red)
                "green" -> resources.getDrawable(R.drawable.ic_marker_green)
                "blue" -> resources.getDrawable(R.drawable.ic_marker_blue)
                "purple" -> resources.getDrawable(R.drawable.ic_marker_purple)
                else -> resources.getDrawable(R.drawable.ic_marker_red)
            }

            storeMarker.icon = markerIcon
            mapView.overlays.add(storeMarker)
        }
    }

    private fun updateMarker(userLocation: GeoPoint) {
        userMarker?.let { mapView.overlays.remove(it) }
        userMarker = Marker(mapView).apply {
            position = userLocation
            title = "Tu ubicación"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(userMarker)
    }

    private fun getUserLocation() {
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

        // Obtener última ubicación conocida
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    mapView.controller.setCenter(userLocation)
                    updateMarker(userLocation)
                }
            }

        // Configurar actualizaciones en tiempo real
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    updateMarker(userLocation)
                }
            }
        }

        // Solicitar actualizaciones de ubicación
        locationCallback?.let {
            fusedLocationClient.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
        }
    }
}
