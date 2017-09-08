package com.freelancer.videoeditor.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class Util {
    Util() {
    }

    public static String formatDuration(long totalMilis) {
        int seconds = ((int) (totalMilis / 1000)) % 60;
        int minutes = (int) ((totalMilis / 60000) % 60);
        int hours = (int) ((totalMilis / 3600000) % 24);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
