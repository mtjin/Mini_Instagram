package com.mtjinse.myminiinstgram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mtjinse.myminiinstgram.models.MiniGram;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://memolease.ipdisk.co.kr:1337";
    Retrofit retrofit;
    MiniGramService miniGramService;

    RecyclerView minigram_recycler;
    MiniGramAdapter miniGramAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<MiniGram> miniGramArrayList = new ArrayList<>();

    Bus bus = BusProvider.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bus.register(this); //정류소 등록

        minigram_recycler = findViewById(R.id.minigram_recycler);

        //Retrofit 객체생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    /*addConverterFactory(GsonConverterFactory.create())은
    Json을 우리가 원하는 형태로 만들어주는 Gson라이브러리 Retrofit2에 연결하는 것입니다 */

        miniGramService = retrofit.create(MiniGramService.class);
        miniGramAdapter = new MiniGramAdapter(MainActivity.this,miniGramArrayList);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        minigram_recycler.setLayoutManager(linearLayoutManager);
        minigram_recycler.setAdapter(miniGramAdapter);

        miniGramService.getMiniGram().enqueue(new Callback<List<MiniGram>>() {
            @Override
            public void onResponse(Call<List<MiniGram>> call, Response<List<MiniGram>> response) {
                if(response.isSuccessful()) {
                    List<MiniGram> getMiniGrams = response.body();
                    for (MiniGram miniGram : getMiniGrams){
                        miniGramArrayList.add(miniGram);
                    }
                    miniGramAdapter.notifyDataSetChanged();
                }else {
                    Log.d("MainiActivity", "failed");
                }
            }

            @Override
            public void onFailure(Call<List<MiniGram>> call, Throwable t) {

            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        bus.unregister(this); //이액티비티 떠나면 정류소 해제해줌
    }

    @Subscribe //이벤트버스에서 보낸 이벤트가 여기에 정착을 하는 정류소를 만든다고 생각
    public void updateLike(HeartEvent heartEvent) {//public항상 붙여줘야함
        miniGramService.updateLike(heartEvent.getId(), heartEvent.isLike()).enqueue(new Callback<MiniGram>() {
            @Override
            public void onResponse(Call<MiniGram> call, Response<MiniGram> response) {
                if(response.isSuccessful()){
                    Log.d("like update", "sucess");
                }else{
                    Log.d("like update", "failed");
                }
            }

            @Override
            public void onFailure(Call<MiniGram> call, Throwable t) {
                Log.d("like update", "not connected");
            }
        });
    }
}
