package com.phong.googlemap_1;

import com.phong.googlemap_1.Activity_map.Camera;
import com.phong.googlemap_1.Activity_map.MapsActivity;

/**
 * Created by huynhducthanhphong on 12/28/15.
 */
public final class DemoDetailsList {
    private DemoDetailsList() {

    }

    public static final DemoDetails[] DEMOS = {
            new DemoDetails(R.string.basic_map_demo_label, R.string.basic_map_demo_description, MapsActivity.class),
            new DemoDetails(R.string.basic_camera_demo_label, R.string.basic_camera_demo_description, Camera.class)
    };
}
