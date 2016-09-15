package com.koffeine.wordfrequency2.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.koffeine.wordfrequency2.Logger;
import com.koffeine.wordfrequency2.model.TranslatedWord;
import com.koffeine.wordfrequency2.rest.Translate;

import java.util.Date;

import io.realm.Realm;


public class TranslateIntentService extends Service {
    private static Logger logger = Logger.getLogger();
    public static final String EXTRA_PI = "Extra pi";
    public static final String EXTRA_WORD = "Extra word";
    public static final int TRANSLATE_MAIN_CODE = 0;

    private TranslateTask translateTask;


    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String word = intent.getStringExtra(EXTRA_WORD);
        PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PI);
        synchronized (this) {
            if (translateTask != null) {
                translateTask.cancel(true);
                translateTask = null;
            }
            if (word != null && word.length() > 2) {
                translateTask = new TranslateTask();
                translateTask.execute(word, pendingIntent);
            }
        }
        logger.debug("onStartCommand word: " + word);
        return START_NOT_STICKY;
    }

    @NonNull
    public static Intent createTranslationIntent(Activity activity, String s) {
        PendingIntent pendingIntent = activity.createPendingResult(
                TranslateIntentService.TRANSLATE_MAIN_CODE, new Intent(), 0);
        Intent intent = new Intent(activity, TranslateIntentService.class);
        intent.putExtra(TranslateIntentService.EXTRA_PI, pendingIntent);
        intent.putExtra(TranslateIntentService.EXTRA_WORD, s);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TranslateTask extends AsyncTask<Object, Void, String> {
        private PendingIntent pendingIntent;

        @Override
        protected String doInBackground(Object... params) {
            pendingIntent = (PendingIntent) params[1];
            return getTranslationAndCache((String) params[0]);
        }

        @Override
        protected void onPostExecute(String word) {
            if (!isCancelled()) {
                Intent intent = new Intent().putExtra(EXTRA_WORD, word);
                try {
                    pendingIntent.send(getApplicationContext(), TRANSLATE_MAIN_CODE, intent);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                synchronized (TranslateIntentService.this) {
                    translateTask = null;
                }
            }
        }
    }

    public static String getTranslationAndCache(String incomeWord) {
        Realm realm = Realm.getDefaultInstance();
        String translatedWord;
        try {
            TranslatedWord word = realm.where(TranslatedWord.class).equalTo("word", incomeWord).findFirst();
            if (word != null) {
                translatedWord = word.getTranslation();
            } else {
                translatedWord = new Translate().translate(incomeWord);
                if (translatedWord != null) {
                    realm.beginTransaction();
                    word = realm.createObject(TranslatedWord.class);
                    word.setWord(incomeWord);
                    word.setTranslation(translatedWord);
                    word.setAddingDate(new Date());
                    realm.commitTransaction();
                }
            }
        } finally {
            realm.close();
        }
        return translatedWord;
    }
}
