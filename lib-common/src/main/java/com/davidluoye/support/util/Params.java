package com.davidluoye.support.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Params {
    private final JSONObject params = new JSONObject();
    public Params() {}

    @Override
    public String toString() {
        return this.params.toString();
    }

    public Params addParams(String name, int value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Params addParams(String name, boolean value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Params addParams(String name, long value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Params addParams(String name, double value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Params addParams(String name, String value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Params addParams(String name, Object value) {
        try {
            params.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}
