package com.mtjinse.myminiinstgram;

import com.mtjinse.myminiinstgram.models.MiniGram;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MiniGramService {

    @GET("/minigrams")
    Call<List<MiniGram>> getMiniGram();

    //서버에 데이터를 수정해달라고 요청하는 메서드
     //"http://memolease.ipdisk.co.kr:1337/minigrams/id/{like:true}" 이런식으로 보내질거다.
    //그냥 String으로 보내면 서버에 put을 할수없어서 UTF-8로 바꿔서 보내야하는데 이를 레트로핏에서 FORMURLENCODED로 해준다.
    @FormUrlEncoded
    @PUT("/minigrams/{id}")
    Call<MiniGram> updateLike(@Path("id") String id, @Field("like") boolean like);
}
