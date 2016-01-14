package com.phong.googlemap_1;

import com.phong.googlemap_1.Activity_map.Basic_Location_Sample;
import com.phong.googlemap_1.Activity_map.Camera;
import com.phong.googlemap_1.Activity_map.Circles;
import com.phong.googlemap_1.Activity_map.EventsDemoActivity;
import com.phong.googlemap_1.Activity_map.Geocode_API;
import com.phong.googlemap_1.Activity_map.GroundOverlayDemoActivity;
import com.phong.googlemap_1.Activity_map.LayerActivity;
import com.phong.googlemap_1.Activity_map.Location_Updates;
import com.phong.googlemap_1.Activity_map.MapsActivity;
import com.phong.googlemap_1.Activity_map.Reconize_user_activity;
import com.phong.googlemap_1.Activity_map.Test_event;
import com.phong.googlemap_1.Activity_map.map_11.Lite_demo;

/**
 * Created by huynhducthanhphong on 12/28/15.
 */
public final class DemoDetailsList {
    private DemoDetailsList() {

    }

    public static final DemoDetails[] DEMOS = {
            new DemoDetails(R.string.basic_map_demo_label, R.string.basic_map_demo_description, MapsActivity.class),
            new DemoDetails(R.string.basic_camera_demo_label, R.string.basic_camera_demo_description, Camera.class),
            new DemoDetails(R.string.basic_circle_demo_label, R.string.basic_circle_demo_description, Circles.class),
            new DemoDetails(R.string.basic_event_demo_label, R.string.basic_event_demo_description, EventsDemoActivity.class),
            new DemoDetails(R.string.basic_ground_demo_label, R.string.basic_ground_demo_description, GroundOverlayDemoActivity.class),
            new DemoDetails(R.string.basic_layer_demo_label, R.string.basic_layer_demo_description, LayerActivity.class),
            new DemoDetails(R.string.basic_location_demo_label, R.string.basic_location_demo_description, Basic_Location_Sample.class),
            new DemoDetails(R.string.basic_location_update_demo_label, R.string.basic_location_update_demo_description, Location_Updates.class),
            new DemoDetails(R.string.basic_geocoder_update_demo_label, R.string.basic_geocoder_update_demo_description, Geocode_API.class),
            new DemoDetails(R.string.basic_reconize_user_demo_label, R.string.basic_reconize_user_demo_description, Reconize_user_activity.class),
            new DemoDetails(R.string.basic_lite_demo_label, R.string.basic_lite_demo_description, Lite_demo.class),
    };
}
