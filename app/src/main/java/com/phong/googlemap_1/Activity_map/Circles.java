package com.phong.googlemap_1.Activity_map;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.phong.googlemap_1.R;

import java.util.ArrayList;
import java.util.List;


public class Circles extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;
    private static final int WIDTH_MAX = 50;
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final double DEFAULT_RADIUS = 1000000;
    private static final double RADIUS_OF_EARTH_METERS = 6371009;

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;
    private GoogleMap mMap;
    private int mFillColor;
    private int mStrokeColor;
    private List<DraggableCircle> mCircles = new ArrayList<DraggableCircle>(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circles);

        // 3 thanh search bar
        mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(127);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(10);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_circles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setContentDescription(getString(R.string.map_circle_description));

        mColorBar.setOnSeekBarChangeListener(this);
        mAlphaBar.setOnSeekBarChangeListener(this);
        mWidthBar.setOnSeekBarChangeListener(this);

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

        // chuyển giá trị thanh kéo sang màu sắc
        mFillColor = Color.HSVToColor(mAlphaBar.getProgress(), new float[]{mColorBar.getProgress(), 1, 1});
        // chuyển giá trị thanh kéo sang độ rộng
        mStrokeColor = Color.BLACK;

        DraggableCircle circle = new DraggableCircle(SYDNEY, DEFAULT_RADIUS);
        mCircles.add(circle);

        // move map giữa vòng tròn 
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 4.0f));


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mColorBar) {
            mFillColor = Color.HSVToColor(Color.alpha(mFillColor), new float[]{progress, 1, 1});
        }
        else if (seekBar == mAlphaBar){
            mFillColor = Color.argb(progress, Color.red(mFillColor), Color.green(mFillColor), Color.blue(mFillColor));
        }

        for (DraggableCircle draggableCircle : mCircles) {
            draggableCircle.onStyleChange();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);

    }

    private void onMarkerMoved(Marker marker) {
        for (DraggableCircle draggableCircle : mCircles) {
            if (draggableCircle.onMarkerMoved(marker)) {
                break;
            }
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);

    }

    @Override
    public void onMapLongClick(LatLng point) {
        View view = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getView();
        LatLng radiusLatLng = mMap.getProjection().fromScreenLocation(new Point(view.getHeight() * 3 / 4, view.getWidth() * 3 / 4));
        DraggableCircle circle = new DraggableCircle(point, radiusLatLng);
        mCircles.add(circle);
    }

    private class DraggableCircle {
        private double radius;
        private final Marker centerMarker;
        private final Marker radiusMarker;
        private final Circle circle;

        public DraggableCircle(LatLng center, double radius) {
            this.radius = radius;
            // add 2 marker trong vòng tròn 
            centerMarker = mMap.addMarker(new MarkerOptions()
            .position(center)
            .draggable(true));
            
            radiusMarker = mMap.addMarker(new MarkerOptions()
            .position(toRadiusLatLng(center, radius))
            .draggable(true)
            .icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE
            )));
            circle = mMap.addCircle(new CircleOptions()
            .center(center)
            .radius(radius)
            .strokeWidth(mWidthBar.getProgress())
            .strokeColor(mStrokeColor)
            .fillColor(mFillColor));
        }

        public DraggableCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }

        public void onStyleChange() {
            circle.setStrokeWidth(mWidthBar.getProgress());
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
        }

        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }

            if (marker.equals(radiusMarker)) {
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                circle.setRadius(radius);
                return true;
            }
            return false;
        }

       
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return result[0];
    }


    // gọi thường xuyên thì cần static class để nó ko tạo object hoài
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }
}
