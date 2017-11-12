package com.shiz.android.actuator.model;

/**
 * Created by OldMan on 07.11.2016.
 */

public class OrientationValue {
    private float[] value;

    public OrientationValue(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }
}
