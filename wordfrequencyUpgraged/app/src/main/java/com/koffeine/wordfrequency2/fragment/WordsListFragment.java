package com.koffeine.wordfrequency2.fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koffeine.wordfrequency2.Logger;
import com.koffeine.wordfrequency2.WordsFreqApplication;
import com.koffeine.wordfrequency2.provider.WordSQLHelper;
import com.koffeine.wordfrequency2.service.TranslateIntentService;


public class WordsListFragment extends ListFragment {

    private static Handler handler;
    private static int MESSAGE_TRANSLATE = 1;
    private static int LOADER_CURSOR_ID = 2;
    private SimpleCursorAdapter adapter;
    private WordSelectedInList attach;
    private static Logger logger = Logger.getLogger(WordsListFragment.class.getSimpleName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String from[] = {WordSQLHelper.COLUMN_NAME};
        int to[] = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, null, from, to, 0);
        getActivity().getSupportLoaderManager().initLoader(LOADER_CURSOR_ID, null, new ListLoaderCallBack());
        setListAdapter(adapter);
        handler = new MyHandler(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("Empty list");
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setOnItemLongClickListener(new RemoveOnItemLongClickListener());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String text = ((TextView) v).getText().toString();
        showToastTranslation(text);
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

    private void showToastTranslation(final String word) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String res = TranslateIntentService.getTranslationAndCache(word);
                if (res != null) {
                    handler.sendMessage(handler.obtainMessage(MESSAGE_TRANSLATE, word + " : " + res));
                }
            }
        });
        t.start();
    }

    private static class MyHandler extends Handler {
        private Context context;

        public MyHandler(Context context) {
            this.context = context;
        }

        public void handleMessage(android.os.Message msg) {
            if (msg.what == MESSAGE_TRANSLATE) {
                if (msg.obj != null & msg.obj instanceof String) {
                    Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class RemoveOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String text = ((TextView) view).getText().toString();
            attach.wordSelectedInList(text);
            showNoticeDialog(text);
            return true;
        }
    }

    private static class WordsCursorLoader extends CursorLoader {
        public WordsCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return ((WordsFreqApplication)getContext().getApplicationContext()).getSqlHolder().getAllWordCursor();
        }

    }

    private class ListLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Loader<Cursor> loader = null;
            if (id == LOADER_CURSOR_ID) {
                loader = new WordsCursorLoader(getContext().getApplicationContext());
            }
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.changeCursor(data);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }

    public interface WordSelectedInList {
        void wordSelectedInList(String word);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attach = (WordSelectedInList) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attach = null;
    }
}
