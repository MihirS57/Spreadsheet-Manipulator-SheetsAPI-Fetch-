package com.example.spreadsheetmanipulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView data,result;
    Button fetch,results;
    int[] mark;
    int len=-1;
    String buffer="",buffer2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        data = findViewById( R.id.disp_data );
        result = findViewById( R.id.disp_result );
        fetch = findViewById( R.id.fetch_btn );
        results = findViewById( R.id.result_btn );
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://sheets.googleapis.com/" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        GetSheetInterface gsi = retrofit.create( GetSheetInterface.class );

        fetch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buffer = "";
                buffer2 = "";
                data.setText( "" );
                result.setText( "" );
                Call<SheetResponse> call = gsi.getData();
                call.enqueue( new Callback<SheetResponse>() {
                    @Override
                    public void onResponse(Call<SheetResponse> call, Response<SheetResponse> response) {
                        if(response.isSuccessful()){
                            len = response.body().getValues().size();
                            mark = new int[len-1];

                            for(int i = 0;i<len;i++){
                                buffer+= response.body().getValues().get( i ).get( 0 )+" ";
                                buffer+= response.body().getValues().get( i ).get( 1 )+" ";
                                buffer+= response.body().getValues().get( i ).get( 2 )+" ";
                                if(!response.body().getValues().get( i ).get( 2 ).equals( "Marks" )){
                                    mark[i-1] = Integer.parseInt( response.body().getValues().get( i ).get( 2 ) );
                                }
                                buffer+="\n";
                            }
                            data.setText( buffer );
                        }else{
                            data.setText( "Unsuccessful"+response.errorBody().toString() );
                        }
                    }

                    @Override
                    public void onFailure(Call<SheetResponse> call, Throwable t) {
                        data.setText( t.getMessage() );
                    }
                } );
            }
        } );

        results.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buffer != ""){
                    buffer2+="\n";
                    for(int i = 0;i<len-1;i++){
                        if(mark[i] >= 40){
                            buffer2+="Pass\n";
                        }else{
                            buffer2+="Fail\n";
                        }
                    }
                    result.setText( buffer2 );
                }else{
                    result.setText( "No Data" );
                }

            }
        } );

    }
}