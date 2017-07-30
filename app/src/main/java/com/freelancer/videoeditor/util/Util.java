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

    static boolean isDebug(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationContext().getApplicationInfo();
        int i = applicationInfo.flags & 2;
        applicationInfo.flags = i;
        return i != 0;
    }

    static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    static String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String str = r.readLine();
                if (str == null) {
                    return sb.toString();
                }
                sb.append(str);
            }
        } catch (IOException e) {
            Timber.e("error converting input stream to string", e);
            return null;
        }
    }

    static void destroyProcess(Process process) {
        if (process != null) {
            process.destroy();
        }
    }

    static boolean killAsync(AsyncTask asyncTask) {
        return !(asyncTask == null || asyncTask.isCancelled() || !asyncTask.cancel(true));
    }

    static boolean isProcessCompleted(Process process) {
        if (process == null) {
            return true;
        }
        try {
            process.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }
    }

    public static String formatDuration(long totalMilis) {
        int seconds = ((int) (totalMilis / 1000)) % 60;
        int minutes = (int) ((totalMilis / 60000) % 60);
        int hours = (int) ((totalMilis / 3600000) % 24);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
