package eu.unibuc.ro;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sasGym = new LatLng(44.422740, 26.133040);
        googleMap.addMarker(new MarkerOptions().position(sasGym).title("SAS GYM"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sasGym));

        LatLng worldClass = new LatLng(44.425748, 26.077091);
        googleMap.addMarker(new MarkerOptions().position(worldClass).title("WORLD CLASS"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(worldClass));
    }
}
