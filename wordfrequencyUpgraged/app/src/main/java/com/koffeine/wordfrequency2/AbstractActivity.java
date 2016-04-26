package com.koffeine.wordfrequency2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public abstract class AbstractActivity extends AppCompatActivity {
    public static final String USE_DICTIONARY = "useDict";
    private Logger logger = Logger.getLogger(AbstractActivity.class.getSimpleName());

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        boolean useDictionary = isUseDictionary(this);
        MenuItem itemDict = menu.getItem(0);
        itemDict.setChecked(useDictionary);
        itemDict.setTitle(getDictMenuTitle(useDictionary));
        return true;
    }

    @NonNull
    private String getDictMenuTitle(boolean useDictionary) {
        return useDictionary ? "Using dictionary" : "Without using dictionary";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean useDict = !item.isChecked();
        item.setChecked(useDict);
        getSharedPrefferencesForMenu(this).edit().putBoolean(USE_DICTIONARY, useDict).commit();
        item.setTitle(getDictMenuTitle(useDict));
        logger.debug("onOptionsItemSelected useDict: " + useDict);

        return super.onOptionsItemSelected(item);
    }

    public static boolean isUseDictionary(Context context) {
        return getSharedPrefferencesForMenu(context).getBoolean(USE_DICTIONARY, true);
    }

    private static SharedPreferences getSharedPrefferencesForMenu(Context context) {
        return context.getSharedPreferences("menu", 0);
    }
}
