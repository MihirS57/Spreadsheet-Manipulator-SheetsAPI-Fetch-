package com.example.spreadsheetmanipulator;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewSheetRequest {

    @SerializedName( "requests" )
    Requests requests;

    public Requests getRequests() {
        return requests;
    }

    public NewSheetRequest(Requests requests) {
        this.requests = requests;
    }

    public static class Requests{
        @SerializedName( "addSheet" )
        AddSheet addsheet;

        public Requests(AddSheet addsheet) {
            this.addsheet = addsheet;
        }

        public AddSheet getAddsheet() {
            return addsheet;
        }

        public static class AddSheet{
            @SerializedName( "properties" )
            Properties properties;

            public AddSheet(Properties properties) {
                this.properties = properties;
            }

            public Properties getProperties() {
                return properties;
            }

            public static class Properties{
                @SerializedName( "title" )
                String title = "Results";

                public Properties(String title) {
                    this.title = title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }
            }

        }

    }

}
