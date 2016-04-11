package com.koffeine.wordfrequency2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class WordFreqActivity extends FragmentActivity {

    private Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onsCreate");
        setContentView(R.layout.main);

//        if (findViewById(R.id.frame_with_fragment_container) != null) {
//            FragmentManager fragMgr = getSupportFragmentManager();
//            FragmentTransaction xact = fragMgr.beginTransaction();
//            if (null == fragMgr.findFragmentByTag(FRAG_MAIN_TAG)) {
//                xact.add(R.id.frame_with_fragment_container, new MainFragment(), FRAG_MAIN_TAG);
//            }
//            xact.commit();
//        }

    }
}