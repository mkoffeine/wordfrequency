package com.koffeine.wordfrequency2.rest;

import com.google.gson.Gson;
import com.koffeine.wordfrequency2.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mKoffeine on 16.04.2016.
 */
public class Translate {
    private Logger logger = Logger.getLogger();
    private static final String YA_TRANSLATE = "https://translate.yandex.net";
    private static final String KEY = "trnsl.1.1.20160416T120010Z.a482d3f664ee0dc1.8bbec4c111ae6c7ae6060abab76c39e82523fbb6";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(YA_TRANSLATE)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private TranslateService service = retrofit.create(TranslateService.class);
    private Call<Object> call;


    public String translate(String word) {
        String res = null;
        Map<String, String> mapToSend = new HashMap<>();
        mapToSend.put("key", KEY);
        mapToSend.put("lang", "en-uk");
        mapToSend.put("text", word);
        call = service.translate(mapToSend);

        try {
            Response<Object> response = call.execute();
            if (response.code() == 200) {
                Gson gson = new Gson();
                Map map = gson.fromJson(response.body().toString(), Map.class);
                Object text = map.get("text");
                if (text instanceof List && ((List) text).size() > 0) {
                    res = ((List<String>) text).get(0);
                }
            }
        } catch (Exception e) {
            logger.debug("Translation stopped cause: " + e.getMessage());
        }
        return res;
    }

//    public static void main(String[] args) {
//        System.out.println(new Translate().translate("test"));
//    }
}
