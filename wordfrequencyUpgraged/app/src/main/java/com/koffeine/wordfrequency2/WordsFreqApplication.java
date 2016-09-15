package com.koffeine.wordfrequency2;

import android.app.Application;

import com.koffeine.wordfrequency2.model.IWordsModel;
import com.koffeine.wordfrequency2.provider.WordFreqProviderHolder;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;


public class WordsFreqApplication extends Application {
    private IWordsModel wordsModel;
    private WordFreqProviderHolder sqlHolder;
    private Logger logger = Logger.getLogger(WordsFreqApplication.class.getSimpleName());

    @Override
    public void onCreate() {
        super.onCreate();
        sqlHolder = new WordFreqProviderHolder(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).
                migration(new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                if (oldVersion <2) {
                    RealmSchema schema = realm.getSchema();
                    RealmObjectSchema realmObjectSchema = schema.get("TranslatedWord");
                    realmObjectSchema.addPrimaryKey("word");
                }
            }
        }).schemaVersion(2).build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    public WordFreqProviderHolder getSqlHolder() {
        return sqlHolder;
    }

    public IWordsModel getWordsModel() {
        return wordsModel;
    }

    public void setWordsModel(IWordsModel wordsModel) {
        this.wordsModel = wordsModel;
    }


}
