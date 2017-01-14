package com.clement.tvscheduler.activity;

import android.content.res.AssetManager;

/**
 * Created by cleme on 14/01/2017.
 */

public interface ConnectedActivity {


    public AssetManager getAssets();

    public Object getSystemService(String name);

    public void showMessage(String message);

}
