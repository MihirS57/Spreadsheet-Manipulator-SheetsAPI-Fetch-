package com.example.spreadsheetmanipulator;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetSheetInterface {

        @GET("v4/spreadsheets/1FWN2XqBsTBEyAnakamL9BlM-dbcvZgvWXfko9sbURD4?includeGridData=true&key=AIzaSyAh1HgkExMcCiWFKnWgHifrhViEYeeywmY")
        Call<SheetResponse> getData();
}
