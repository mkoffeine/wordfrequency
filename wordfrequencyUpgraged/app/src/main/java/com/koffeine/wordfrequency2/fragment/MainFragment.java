package com.koffeine.wordfrequency2.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koffeine.wordfrequency2.Logger;
import com.koffeine.wordfrequency2.R;
import com.koffeine.wordfrequency2.WordsFreqApplication;
import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.loader.WordsLoader;
import com.koffeine.wordfrequency2.provider.WordSQLHolder;
import com.koffeine.wordfrequency2.rest.Translate;


public class MainFragment extends Fragment {
    private Logger logger = Logger.getLogger(MainFragment.class.getSimpleName());
    private static final int LOADER__ID = 1;
    private String id = "";
    private EditText inText;
    private TextView outText;
    private TextView txTranslate;
    private String TEXT = "text";
    private TranslateTask translateTask;
    private Button btnOpenList;

    private Loader<IWordsModel> wordLoader;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (((WordsFreqApplication) getContext().getApplicationContext()).getWordsModel() == null) {
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            wordLoader = loaderManager.initLoader(LOADER__ID, null, new WordLoaderCallback());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        txTranslate = (TextView) inflate.findViewById(R.id.tx_translate);
        if (savedInstanceState != null) {
            String text = savedInstanceState.getString(TEXT);

            if (text != null && inText != null) {
                inText.setText(text);
            }
        }
        btnOpenList = (Button) getActivity().findViewById(R.id.btn_open_list);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btnOpenList != null) {
            btnOpenList.setVisibility(View.VISIBLE);
        }
        updateStatus();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        super.onSaveInstanceState(outState);
        if (outState != null && inText != null) {
            outState.putString(TEXT, inText.getText().toString());
        }
        logger.debug("123123 main fragment onSaveInstanceState " + outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.debug("onStart " + id);
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        inText.addTextChangedListener(new OnValueChanged());
        outText = (TextView) getActivity().findViewById(R.id.editTextResult);
        logger.debug("Model: " + getWordsModel());


        Button btnClipboard = (Button) getActivity().findViewById(R.id.button_copy);
        btnClipboard.setOnClickListener(new ButtonBufferClick());
        Button btnClear = (Button) getActivity().findViewById(R.id.button_clear);
        btnClear.setOnClickListener(new ButtonClearClick());
        Button btnPast = (Button) getActivity().findViewById(R.id.button_past);
        btnPast.setOnClickListener(new ButtonPastClick());
        Button btnAdd = (Button) getActivity().findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new ButtonAddClick());
    }

    @Override
    public void onStop() {
        inText.setOnClickListener(null);
        outText.setOnClickListener(null);
        outText = null;
        logger.debug("onStop " + id);
        super.onStop();
    }

    @Override
    public void onPause() {
        cancelTranslationIfActive();
        if (btnOpenList != null) {
            btnOpenList.setVisibility(View.INVISIBLE);
        }
        super.onPause();
    }

    private void cancelTranslationIfActive() {
        if (translateTask != null) {
            translateTask.cancel(true);
            translateTask = null;
        }
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy " + id);
        logger = null;
        super.onDestroy();
    }

    private IWordsModel getWordsModel() {
        return ((WordsFreqApplication) getActivity().getApplicationContext()).getWordsModel();
    }


    private void updateStatus() {
        String s = inText.getText().toString();
        logger.debug("onTextChanged " + s);
        String status = "";
        if (getWordsModel() != null) {
            status = getWordsModel().getStatus(s.toLowerCase().trim());
        }
        outText.setText(status);
        cancelTranslationIfActive();
        if (s.length() > 2) {
            translateTask = new TranslateTask();
            translateTask.execute(s);
        } else {
            txTranslate.setText("");
        }
    }


    private class OnValueChanged implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            updateStatus();

        }

        public void afterTextChanged(Editable editable) {

        }
    }

    private class ButtonBufferClick implements View.OnClickListener {

        public void onClick(View view) {
            if (Build.VERSION.SDK_INT < 11) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(inText.getText().toString());
            } else {
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", inText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        }
    }

    private class ButtonPastClick implements View.OnClickListener {

        public void onClick(View view) {
            if (Build.VERSION.SDK_INT < 11) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                inText.setText(clipboard.getText());
            } else {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData primaryClip = clipboard.getPrimaryClip();
                if (primaryClip != null && primaryClip.getItemCount() > 0) {
                    ClipData.Item item = primaryClip.getItemAt(0);
                    if (item != null) {
                        inText.setText(item.getText());
                    }
                }
            }
        }
    }

    private class ButtonAddClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText editText = (EditText) getActivity().findViewById(R.id.editTextInput);
            String w = editText.getText().toString();
            if (w.length() > 1) {
                WordSQLHolder sqlHolder = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder();
                sqlHolder.insertInDB(w);
            }
        }
    }


    private class ButtonClearClick implements View.OnClickListener {

        public void onClick(View view) {
            inText.setText("");
            inText.requestFocus();
        }
    }

    private class TranslateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return new Translate().translate(params[0]);
        }

        @Override
        protected void onPostExecute(String word) {
            if (!isCancelled()) {
                txTranslate.setText(word != null ? word : "");
            }
        }
    }

    private class WordLoaderCallback implements LoaderManager.LoaderCallbacks<IWordsModel> {

        @Override
        public Loader<IWordsModel> onCreateLoader(int id, Bundle args) {
            Loader<IWordsModel> loader = null;
            if (id == LOADER__ID) {
                loader = new WordsLoader(getContext().getApplicationContext());
            }
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<IWordsModel> loader, IWordsModel data) {
            ((WordsFreqApplication) getContext().getApplicationContext()).setWordsModel(data);
            if (MainFragment.this.isAdded()) {
                updateStatus();
            }

            String message = "onPostExecute. model is ready";
            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onLoaderReset(Loader<IWordsModel> loader) {
        }
    }
}
