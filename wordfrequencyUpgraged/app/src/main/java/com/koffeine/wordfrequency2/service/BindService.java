package com.koffeine.wordfrequency2.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mKoffeine on 11.05.2016.
 */
public class BindService extends Service {
    MyBinder binder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public BindService getService() {
            return BindService.this;
        }
    }

    public String test(String s) {
        return "double " + s + " " + s;
    }

}
