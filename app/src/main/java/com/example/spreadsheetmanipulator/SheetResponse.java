package com.example.spreadsheetmanipulator;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SheetResponse {

            @SerializedName("range")
            String range;
            @SerializedName("majorDimension")
            String majorDimension;
            @SerializedName("values")
            List<List<String>> values;


    public String getRange() {
        return range;
    }

    public String getMajorDimension() {
        return majorDimension;
    }

    public List<List<String>> getValues() {
        return values;
    }



}
