package com.example.spreadsheetmanipulator;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface NewSheetInterface {
    @POST("v4/spreadsheets/1FWN2XqBsTBEyAnakamL9BlM-dbcvZgvWXfko9sbURD4:batchUpdate?key=AIzaSyAh1HgkExMcCiWFKnWgHifrhViEYeeywmY")

    //Call<NewSheetResponse> newSheet(@Header ("Authorization") String type, @Body NewSheetRequest newSheetRequest);
    Call<NewSheetResponse> newSheet(@HeaderMap Map<String,String> header, @Body NewSheetRequest newSheetRequest);
}
