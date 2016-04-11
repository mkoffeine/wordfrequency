package com.koffeine.wordfrequency2.fragment;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.koffeine.wordfrequency2.WordsFreqApplication;

import java.util.List;

/**
 * Created by mKoffeine on 08.04.2016.
 */
public class WordsListFragment extends ListFragment {
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> list = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder().getAllWords();
        adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String text = ((TextView) v).getText().toString();
        showNoticeDialog(text);
        updateList();//todo remove it, it's temporaly
    }

    @TargetApi(11)
    private void updateList() {
        adapter.clear();
        List<String> list = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder().getAllWords();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        //todo fix: rework, maybe use broadcast receiver, +model for added words
    }

    public void showNoticeDialog(String word) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AskDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(AskDialogFragment.WORD, word);
        dialog.setArguments(bundle);
        setTargetFragment(WordsListFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), "AskDialog");
    }



    /*  private static class WordsCursorLoader extends CursorLoader {
        public WordsCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return ((WordsFreqApplication)getContext().getApplicationContext()).getSqlHolder().getAllWordCursor();
        }

    }*/
}
