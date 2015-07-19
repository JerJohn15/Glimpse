package com.viasat.glimpse;

import android.animation.ValueAnimator;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TwitterMapActivity extends FragmentActivity {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location startingLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_map);
        setUpMapIfNeeded();

        moveMapTo(getMyLocation());
        String username = "Christopher Chapline";
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus a venenatis leo. Nunc massa mauris, maximus nec odio a, condimentum nullam.";
        addTweetToMap(username, message, getMyLocation(), 60);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public Location getMyLocation() {
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public void moveMapTo(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));
    }

    public void addTweetToMap(String user, String tweet, LatLng location, int fadeOut) {
        // Create the marker
        final Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(user)
                        .snippet(tweet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bird))
        );

        // Make it fade out
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(fadeOut * 1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                marker.setAlpha(alpha);

                int roundedAlpha = (int) Math.round(alpha);
                if ( roundedAlpha == 0 ){
                    marker.remove();
                }
            }
        });
        animator.start();
    }

    public void addTweetToMap(String user, String tweet, Location location, int fadeOut) {
        addTweetToMap(user, tweet, new LatLng(location.getLatitude(), location.getLongitude()), fadeOut);
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        if (mMap == null) {
            Log.e("TwitterMapActivity", "Error initializing TwitterMapActivity");
            return;
        }

        // Handle locations
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap.setMyLocationEnabled(true);

        // Custom Info Windows
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            public View getInfoWindow(Marker marker) {
                return null;
            }

            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_infowindow, null);

                TextView title = (TextView) v.findViewById(R.id.title);
                TextView snippet = (TextView) v.findViewById(R.id.snippet);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return v;

            }
        });

        // Initialize map options. For example:
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
