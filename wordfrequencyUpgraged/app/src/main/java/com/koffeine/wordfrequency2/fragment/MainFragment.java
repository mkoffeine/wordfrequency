package com.koffeine.wordfrequency2.fragment;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

import com.koffeine.wordfrequency2.AbstractActivity;
import com.koffeine.wordfrequency2.Logger;
import com.koffeine.wordfrequency2.R;
import com.koffeine.wordfrequency2.WordsFreqApplication;
import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.model.loader.WordsLoader;
import com.koffeine.wordfrequency2.provider.WordFreqProviderHolder;
import com.koffeine.wordfrequency2.service.BindService;
import com.koffeine.wordfrequency2.service.TranslateIntentService;


public class MainFragment extends Fragment {
    private static Logger logger = Logger.getLogger(MainFragment.class.getSimpleName());
    private static final int LOADER__ID = 1;
    private String id = "";
    private EditText inText;
    private TextView outText;
    private TextView txTranslate;
    private Button btnOpenList;

    private Loader<IWordsModel> wordLoader;

    MyBroadcastReceiver br;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (((WordsFreqApplication) getContext().getApplicationContext()).getWordsModel() == null) {
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            wordLoader = loaderManager.initLoader(LOADER__ID, null, new WordLoaderCallback());
        }
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);

        br = new MyBroadcastReceiver();
        getActivity().registerReceiver(br, intFilt);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        inText = (EditText) getActivity().findViewById(R.id.editTextInput);
        txTranslate = (TextView) inflate.findViewById(R.id.tx_translate);
        btnOpenList = (Button) getActivity().findViewById(R.id.btn_open_list);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (btnOpenList != null) {
            btnOpenList.setVisibility(View.VISIBLE);
        }
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
        updateStatus();
    }

    @Override
    public void onStop() {
        inText.setOnClickListener(null);
        outText.setOnClickListener(null);
        outText = null;
        logger.debug("onStop " + id);
        Intent intent = new Intent(getActivity(), TranslateIntentService.class);
        getContext().stopService(intent);
        super.onStop();
    }

    @Override
    public void onPause() {
        if (btnOpenList != null) {
            btnOpenList.setVisibility(View.INVISIBLE);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        logger.debug("onDestroy " + id);
//        logger = null;
        getActivity().unregisterReceiver(br);
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

        if (AbstractActivity.isUseDictionary(getActivity())) {
            Intent intent = TranslateIntentService.createTranslationIntent(getActivity(), s);
            getContext().startService(intent);
        } else {
            txTranslate.setText("");
        }
        if (s.length() <= 2) {
            txTranslate.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TranslateIntentService.TRANSLATE_MAIN_CODE) {
            String word = data.getStringExtra(TranslateIntentService.EXTRA_WORD);
            txTranslate.setText(word != null ? word : "");
        }
    }

    public void setInputWord(String word) {
        inText.setText(word);
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
                WordFreqProviderHolder sqlHolder = ((WordsFreqApplication) getActivity().getApplication()).getSqlHolder();
                sqlHolder.insertInDB(w);
            }
        }
    }

    boolean bound = false;
    BindService myService;
    ServiceConnection sConn;

    private class ButtonClearClick implements View.OnClickListener {

        public void onClick(View view) {
            inText.setText("");
            inText.requestFocus();

            Intent intent = new Intent(getContext(), BindService.class);
            if (sConn == null) {
                sConn = new ServiceConnection() {

                    public void onServiceConnected(ComponentName name, IBinder binder) {
                        logger.debug("MainActivity onServiceConnected");
                        myService = ((BindService.MyBinder) binder).getService();
                        bound = true;
                        Toast.makeText(getContext(), myService.test("test"), Toast.LENGTH_SHORT).show();
                        ;
                    }

                    public void onServiceDisconnected(ComponentName name) {
                        logger.debug("MainActivity onServiceDisconnected");
                        myService = null;
                        bound = false;
                    }
                };
                getActivity().bindService(intent, sConn, Context.BIND_AUTO_CREATE);
            } else {
                if (myService != null) {
                    Toast.makeText(getContext(), myService.test("qqq" + Math.random()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "myService==NULL", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

//    private class TranslateTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return new Translate().translate(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String word) {
//            if (!isCancelled()) {
//                txTranslate.setText(word != null ? word : "");
//            }
//        }
//    }

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

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public MyBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            logger.debug("MyBroadcastReceiver " + context + "  " + intent);
            String message = intent.getStringExtra("tr") + ">  from broadcastreceiver";
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public final static String BROADCAST_ACTION = "Translate_BROADCAST_ACTION";
}
