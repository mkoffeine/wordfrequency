package com.koffeine.wordfrequency2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.koffeine.wordfrequency2.fragment.MainFragment;
import com.koffeine.wordfrequency2.fragment.WordsListFragment;


public class WordFreqActivity extends FragmentActivity {

    private final String FRAG_MAIN_TAG = "FRAG_MAIN_TAG";
    private final String FRAG_LIST_TAG = "FRAG_LIST_TAG";
    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onsCreate");
        setContentView(R.layout.main);

        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction xact = fragMgr.beginTransaction();
        Fragment fragmentMain = fragMgr.findFragmentByTag(FRAG_MAIN_TAG);
        if (null == fragmentMain) {
            xact.add(R.id.main_fragment_cont, new MainFragment(), FRAG_MAIN_TAG);
        }
        if (findViewById(R.id.list_fragment_cont) != null) {//2 fragment
            Fragment fragmentList = fragMgr.findFragmentByTag(FRAG_LIST_TAG);
            if (null == fragmentList) {
                xact.add(R.id.list_fragment_cont, new WordsListFragment(), FRAG_LIST_TAG);
            }
        }
        xact.commit();

    }
}