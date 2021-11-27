package com.inadesirrasukma.ugd_lib_a_10310.repositories;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.inadesirrasukma.ugd_lib_a_10310.model.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

public class MainRepository {

    private final Context context;

    @Inject
    public MainRepository(Context context) {
        this.context = context;
    }

    /**
     * TODO 1.5 isi logic untuk Method getResponse()
     *
     * @return MutableLiveData dari kelas response yang nanti disimpan di viewmodel
     * @throws Exception
     * @HINT: mirip guided tapi bungkus hasil keluaran dengan MutableLiveData
     * cara menggunakan live data ada didokumentasi resmi android google
     * https://developer.android.com/topic/libraries/architecture/livedata?hl=id
     */
    public MutableLiveData<Response> getResponse() throws Exception {
        Gson gson = new Gson();
        Response response = gson.fromJson(loadJson(), Response.class);
        return new MutableLiveData<>(response);
    }

    /**
     * TODO 1.4 isi logic untuk Method loadJson()
     *
     * @return referensi file datagame.json dari folder assets
     * @throws Exception
     * @HINT: gunakan context yang ada dikelas ini agar bisa menggunakan method assets
     */
    private Reader loadJson() throws Exception {
        AssetManager assetManager = this.context.getAssets();
        InputStream inputStream = assetManager.open("datagame.json");
        return new InputStreamReader(inputStream);
    }
}
