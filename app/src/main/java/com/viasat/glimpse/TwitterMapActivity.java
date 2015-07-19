package com.viasat.glimpse;

import android.animation.ValueAnimator;
import android.content.res.Resources;
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

    private static final int NUM_TWEETS = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_map);
        setUpMapIfNeeded();

        Location myLocation = getMyLocation();

//        for( int i = 0; i < NUM_TWEETS; i++ ) {
//            String username = "User" + i;
//            String message = "Current value of \"i\" is: " + i;
//
//            double newLatitude = myLocation.getLatitude() + Math.random();
//            double newLongitutde = myLocation.getLongitude() + Math.random();
//            LatLng latLng = new LatLng(newLatitude, newLongitutde);
//
//            addTweetToMap(username, message, latLng, 240);
//        }
        TwitterGetter twitterGetter = new TwitterGetter(this);
        twitterGetter.start();

        String username = "Christopher Chapline";
        String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus a venenatis leo. Nunc massa mauris, maximus nec odio a, condimentum nullam.";
        addTweetToMap(username, message, myLocation, 120);

        LatLng sec = new LatLng(myLocation.getLatitude() + 1, myLocation.getLongitude());
        addTweetToMap("Testing again", "Testing", sec, 120);
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

    public void addTweetToMap(final String user, final String tweet, final LatLng location,
                              final int fadeOut) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                        if (roundedAlpha == 0) {
                            marker.remove();
                        }
                    }
                });
                animator.start();
            }
        });
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

        // Enable location checking
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap.setMyLocationEnabled(true);

        // Move the map to my location
        moveMapTo(getMyLocation());

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
