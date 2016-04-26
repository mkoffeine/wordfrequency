package com.koffeine.wordfrequency2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.koffeine.wordfrequency2.fragment.WordsListFragment;

public class WordListActivity extends AbstractActivity implements WordsListFragment.WordSelectedInList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_word_list);
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction xact = fragMgr.beginTransaction();
        Fragment fragmentList = fragMgr.findFragmentById(R.id.list_activity_cont);
        if (fragmentList == null) {
            fragmentList = new WordsListFragment();
            xact.add(R.id.list_activity_cont, fragmentList);
        }
        xact.commit();
    }

    @Override
    public void wordSelectedInList(String word) {

    }
}
