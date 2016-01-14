package com.phong.googlemap_1.Activity_map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.phong.googlemap_1.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class GroundOverlayDemoActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener, GoogleMap.OnGroundOverlayClickListener {

    private static final int TRANSPARENCY_MAX = 100;
    private static final LatLng NEWARK = new LatLng(40.714086, -74.228697);
    private static final LatLng NEAR_NEWARK = new LatLng(NEWARK.latitude - 0.001, NEWARK.longitude - 0.025);
    private SeekBar mTransparencyBar;
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private GroundOverlay mGroundOverlayRotated;
    private int mCurrentEntry = 0;
    private GroundOverlay mGroundOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_overlay_demo);

        mTransparencyBar = (SeekBar) findViewById(R.id.transparencySeekBar);
        mTransparencyBar.setMax(TRANSPARENCY_MAX);
        mTransparencyBar.setProgress(100);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ground_overlay_demo, menu);
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

    public void switchImage(View view) {
        mCurrentEntry = (mCurrentEntry + 1) % mImages.size();
        mGroundOverlay.setImage(mImages.get(mCurrentEntry));
    }

    public void toggleClickability(View view) {
        if (mGroundOverlayRotated != null) {
            mGroundOverlayRotated.setClickable(((CheckBox)view).isChecked());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnGroundOverlayClickListener(this);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, 11));
        
        mImages.clear();
        mImages.add(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map));
        mImages.add(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_b));
        
        mGroundOverlayRotated  = googleMap.addGroundOverlay(new GroundOverlayOptions()
                .image(mImages.get(1)).anchor(0, 1)
                .position(NEAR_NEWARK, 4300f, 3025f)
                .bearing(30)
                .clickable(((CheckBox) findViewById(R.id.toggleClickability)).isChecked()));

        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
        .image(mImages.get(mCurrentEntry)).anchor(0, 1)
        .position(NEWARK, 8600f, 6500f));

        mTransparencyBar.setOnSeekBarChangeListener(this);

        googleMap.setContentDescription("Google Map with ground overlay.");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mGroundOverlay != null) {
            mGroundOverlay.setTransparency((float) progress / (float) TRANSPARENCY_MAX);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        mGroundOverlayRotated.setTransparency(0.5f - mGroundOverlayRotated.getTransparency());
    }
}
