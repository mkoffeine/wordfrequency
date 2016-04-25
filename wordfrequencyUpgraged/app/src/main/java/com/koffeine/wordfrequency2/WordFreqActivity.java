package com.koffeine.wordfrequency2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.koffeine.wordfrequency2.fragment.MainFragment;
import com.koffeine.wordfrequency2.fragment.WordsListFragment;
import com.koffeine.wordfrequency2.service.TranslateIntentService;


public class WordFreqActivity extends FragmentActivity implements WordsListFragment.WordSelectedInList {

    private final static String FRAG_MAIN_TAG = "FRAG_MAIN_TAG";
    private final static String FRAG_LIST_TAG = "FRAG_LIST_TAG";
    private static Logger logger = Logger.getLogger(WordFreqActivity.class.getSimpleName());

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
        if (fragMgr.getBackStackEntryCount() == 1) {
            fragMgr.popBackStack();
        }
        if (null == fragmentMain) {
            xact.add(R.id.main_fragment_cont, new MainFragment(), FRAG_MAIN_TAG);
        }
        if (findViewById(R.id.list_fragment_cont) != null) {//2 fragment
            Fragment fragmentList = fragMgr.findFragmentByTag(FRAG_LIST_TAG);
            if (null == fragmentList) {
                fragmentList = new WordsListFragment();
                xact.add(R.id.list_fragment_cont, fragmentList, FRAG_LIST_TAG);

            } else {
                logger.debug("fragmentList != null 2 fragment : fragmentList.isAdded() :" + fragmentList.isAdded());
            }

        } else {
            //todo remove?
            Fragment fragmentList = fragMgr.findFragmentByTag(FRAG_LIST_TAG);
            if (null != fragmentList) {
                xact.remove(fragmentList);
            }
        }
        xact.commit();
        Button btnOpenList = (Button) findViewById(R.id.btn_open_list);
        if (btnOpenList != null) {
            btnOpenList.setOnClickListener(new ButtonOpenListClick());
        }
    }

    @Override
    public void wordSelectedInList(String word) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAG_MAIN_TAG);
        if (fragment != null && fragment.isAdded()) {
            ((MainFragment) fragment).setInputWord(word);
        }
    }

    private class ButtonOpenListClick implements View.OnClickListener {

        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), WordListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TranslateIntentService.TRANSLATE_MAIN_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAG_MAIN_TAG);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}