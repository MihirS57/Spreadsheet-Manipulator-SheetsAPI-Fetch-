package com.example.spreadsheetmanipulator;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SheetResponse {

    @SerializedName("spreadsheetId")
    String spreadsheetId;
    @SerializedName("properties")
    Properties properties;
    @SerializedName("sheets")
    List<Sheets> sheets;
    @SerializedName("spreadsheetUrl")
    String spreadsheetUrl;

    public String getSpreadsheetUrl() {
        return spreadsheetUrl;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public Properties getProperties() {
        return properties;
    }


    public List<Sheets> getSheets() {
        return sheets;
    }


    public class Properties{
        @SerializedName("title")
        String title;

        public Properties(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public class Sheets{
        @SerializedName("data")
        List<DataObject> data;

        public List<DataObject> getData() {
            return data;
        }

        public class DataObject{
            @SerializedName("rowData")
            List<RowObject> rowData;

            public List<RowObject> getRowData() {
                return rowData;
            }

            public class RowObject{
                @SerializedName("values")
                List<ValueObject> values;

                public List<ValueObject> getValues() {
                    return values;
                }

                public class ValueObject{
                    @SerializedName("formattedValue")
                    String formattedValue;

                    public String getFormattedValue() {
                        return formattedValue;
                    }
                }

            }
        }

    }

}
