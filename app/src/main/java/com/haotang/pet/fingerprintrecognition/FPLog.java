package com.haotang.pet.fingerprintrecognition;

import android.util.Log;

import com.haotang.pet.util.Utils;

/**
 * Created by 77423 on 2016/11/7.
 */

public class FPLog {

    public static void log(String message) {
        if (Utils.isLog) {
            Log.i("FPLog", message);
        }
    }
}
