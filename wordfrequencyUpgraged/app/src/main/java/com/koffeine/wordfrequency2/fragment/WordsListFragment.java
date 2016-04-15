package com.koffeine.wordfrequency2.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.koffeine.wordfrequency2.WordsFreqApplication;
import com.koffeine.wordfrequency2.provider.WordSQLHelper;
import com.koffeine.wordfrequency2.provider.WordSQLHolder;

public class WordsListFragment extends ListFragment {
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Cursor cursor = getActivity().getContentResolver()
//                .query(WordFreqProvider.WORD_FREQ_CONTENT_URI, null, null, null, null);
        WordSQLHolder sqlHolder = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder();
        Cursor cursor = sqlHolder.getAllWordCursor();
        getActivity().startManagingCursor(cursor);

        String from[] = {WordSQLHelper.COLUMN_NAME};
        int to[] = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, cursor, from, to);

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
