package com.freelancer.videoeditor.util;

import android.os.AsyncTask;

public class AsyncTaskLoader extends AsyncTask<AsyncCallBack, Integer, Boolean> {
    AsyncCallBack[] _params;

    protected Boolean doInBackground(AsyncCallBack... params) {
        this._params = params;
        for (AsyncCallBack workToDo : params) {
            workToDo.workToDo();
        }
        return Boolean.valueOf(true);
    }

    protected void onPostExecute(Boolean result) {
        for (AsyncCallBack onComplete : this._params) {
            onComplete.onComplete();
        }
    }

    protected void onCancelled() {
        super.onCancelled();
        for (AsyncCallBack onCancelled : this._params) {
            onCancelled.onCancelled();
        }
    }

    protected void onCancelled(Boolean result) {
        super.onCancelled(result);
        for (AsyncCallBack onCancelled : this._params) {
            onCancelled.onCancelled(result.booleanValue());
        }
    }
}
