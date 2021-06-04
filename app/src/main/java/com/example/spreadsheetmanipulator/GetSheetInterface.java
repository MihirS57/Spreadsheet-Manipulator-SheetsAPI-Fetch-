package com.example.spreadsheetmanipulator;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetSheetInterface {

        @GET("v4/spreadsheets/1FWN2XqBsTBEyAnakamL9BlM-dbcvZgvWXfko9sbURD4/values/Sheet1!A1:C10?key=AIzaSyAh1HgkExMcCiWFKnWgHifrhViEYeeywmY")
        Call<SheetResponse> getData();
}
