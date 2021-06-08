package com.example.spreadsheetmanipulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView data,result;
    Button acc_btn;
    int[] mark;
    int len=-1;
    String buffer="",buffer2="";
    String sheetID = "1FWN2XqBsTBEyAnakamL9BlM-dbcvZgvWXfko9sbURD4";
    String clientID = "1046885823995-tdh4kik6da4e32ae116hh3tticdkgrgj.apps.googleusercontent.com";

    private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/spreadsheets";
    private static final String CODE = "code";
    private static final String REDIRECT_URI = "com.example.spreadsheetmanipulator:/oauth2redirect";

    //Authorization
    static String AUTHORIZATION_CODE;
    private static final String GRANT_TYPE = "authorization_code";

    //Response
    static String Authcode;
    static String Tokentype;
    static String Refreshtoken;
    static Long Expiresin, ExpiryTime;

    NewSheetInterface nsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        data = findViewById( R.id.disp_data );
        result = findViewById( R.id.disp_result );

        acc_btn = findViewById( R.id.fetch_acc );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "https://sheets.googleapis.com/" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        GetSheetInterface gsi = retrofit.create( GetSheetInterface.class );
        nsi = retrofit.create( NewSheetInterface.class );

        Call<SheetResponse> call = gsi.getData();
        call.enqueue( new Callback<SheetResponse>() {
            @Override
            public void onResponse(Call<SheetResponse> call, Response<SheetResponse> response) {
                if(response.isSuccessful()){
                    data.setText( "Success" );
                    int sheetsLen = response.body().getSheets().size();
                    for(int i = 0;i<sheetsLen;i++){
                        int dataLen = response.body().getSheets().get( i ).getData().size();
                        for(int j = 0;j<dataLen;j++){
                            int rowLen = response.body().getSheets().get( i ).getData().get( j ).getRowData().size();
                            for(int k = 0;k<rowLen;k++){
                                int valueLen = response.body().getSheets().get( i ).getData().get( j ).getRowData().get( k ).getValues().size();
                                for(int l = 0;l<valueLen;l++){
                                    if(response.body().getSheets().get( i ).getData().get( j ).getRowData().get( k ).getValues().get( l ).getFormattedValue()!=null){
                                        buffer += response.body().getSheets().get( i ).getData().get( j ).getRowData().get( k ).getValues().get( l ).getFormattedValue() + "\n";
                                    }else{
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    data.setText( buffer );
                }else{
                    data.setText( "Unsuccessful" );
                }
            }

            @Override
            public void onFailure(Call<SheetResponse> call, Throwable t) {
                data.setText( "Fail" );
            }
        } );
        /*
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



        results.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SheetProperties sp = new SheetProperties();
                sp.setTitle( "Results" );
                AddSheetRequest asr = new AddSheetRequest();
                asr.setProperties( sp );

                List<Request> requests = new ArrayList<>();
                requests.add( new Request().setAddSheet(  asr) );
                BatchUpdateSpreadsheetRequest bsr = new BatchUpdateSpreadsheetRequest();
                bsr.setRequests(requests  );


                Sheets sheetsService = createSheetsService(  );
                try {
                    googleSheetService.spreadsheets().batchUpdate( sheetID,bsr );
                } catch (IOException e) {
                    e.printStackTrace();
                }


                NewSheetRequest.Requests.AddSheet nad = new NewSheetRequest.Requests.AddSheet( new NewSheetRequest.Requests.AddSheet.Properties( "Results" ) );

                NewSheetRequest nsr = new NewSheetRequest(new NewSheetRequest.Requests( nad ));

                Call<NewSheetResponse> callN = nsi.newSheet( nsr );
                callN.enqueue( new Callback<NewSheetResponse>() {
                    @Override
                    public void onResponse(Call<NewSheetResponse> call, Response<NewSheetResponse> response1) {
                        if(response1.isSuccessful()){
                            result.setText( response1.body().spreadsheetId );
                        }else{
                            try {
                                result.setText( response1.errorBody().string() );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewSheetResponse> call, Throwable t) {
                        result.setText( "Fail" );
                    }
                } );



            }
        } );

         */


        acc_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData( Uri.parse("https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + clientID + "&response_type=" + CODE + "&redirect_uri=" + REDIRECT_URI + "&scope=" + OAUTH_SCOPE));

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }



                /*
                authenticationPart();
                NetHttpTransport httpTransport = null;
                try {
                    Log.e("TRY","Inside Try");
                    httpTransport = new com.google.api.client.http.javanet.NetHttpTransport();
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                    Sheets googleSheetService = new Sheets.Builder(httpTransport
                            ,jsonFactory,getCredential(httpTransport,jsonFactory  ) )
                            .setApplicationName( "Spreadsheet Manipulator" )
                            .build();

                    Log.e("TRY","Sheet Service Created");
                    SheetProperties sp = new SheetProperties();
                    sp.setTitle( "Results" );
                    AddSheetRequest asr = new AddSheetRequest();
                    asr.setProperties( sp );

                    List<Request> requests = new ArrayList<>();
                    requests.add( new Request().setAddSheet(  asr) );
                    BatchUpdateSpreadsheetRequest bsr = new BatchUpdateSpreadsheetRequest();
                    bsr.setRequests(requests  );

                    try {
                        Log.e("TRY TRY","Inside Try Try");
                        Sheets.Spreadsheets.BatchUpdate request = googleSheetService.spreadsheets().batchUpdate( sheetID,bsr );
                        BatchUpdateSpreadsheetResponse response = request.execute();
                        String ID = response.getSpreadsheetId();
                        Log.e("TRY TRY","Response done");
                        if(ID!="") {
                            Toast.makeText( MainActivity.this, ID, Toast.LENGTH_SHORT ).show();
                        }else{
                            Toast.makeText( MainActivity.this, "Empty", Toast.LENGTH_SHORT ).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                 */

            }
        } );



    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();

        if (data != null && !TextUtils.isEmpty(data.getScheme())){
            String code = data.getQueryParameter(CODE);

            if (!TextUtils.isEmpty(code)) {

                //Success Toast
                Toast.makeText(MainActivity.this, "Success",Toast.LENGTH_LONG).show();
                AUTHORIZATION_CODE = code;

                // Using Retrofit builder getting Authorization code
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://www.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = builder.build();

                OAuthServer.OAuthServerIntface oAuthServerIntface = retrofit.create(OAuthServer.OAuthServerIntface.class);
                final Call<OAuthToken> accessTokenCall = oAuthServerIntface.getAccessToken(
                        AUTHORIZATION_CODE,
                        clientID,
                        REDIRECT_URI,
                        GRANT_TYPE
                );

                accessTokenCall.enqueue(new Callback<OAuthToken>() {
                    @Override
                    public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                        Authcode = response.body().getAccessToken();
                        Tokentype = response.body().getTokenType();
                        Expiresin = response.body().getExpiresIn();
                        Refreshtoken = response.body().getRefreshToken();
                        ExpiryTime = System.currentTimeMillis() + (Expiresin * 1000);

                        //result.setText( Authcode );

                        NewSheetRequest.Requests.AddSheet nad = new NewSheetRequest.Requests.AddSheet( new NewSheetRequest.Requests.AddSheet.Properties( "Results" ) );

                        NewSheetRequest nsr = new NewSheetRequest(new NewSheetRequest.Requests( nad ));

                        HashMap<String,String> map = new HashMap<>();
                        map.put("Authorization",Authcode);

                        Call<NewSheetResponse> callN = nsi.newSheet(map, nsr );
                        callN.enqueue( new Callback<NewSheetResponse>() {
                            @Override
                            public void onResponse(Call<NewSheetResponse> call, Response<NewSheetResponse> response1) {
                                if(response1.isSuccessful()){
                                    result.setText( response1.body().spreadsheetId );
                                }else{
                                    try {
                                        result.setText( response1.errorBody().string() );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<NewSheetResponse> call, Throwable t) {
                                result.setText( t.getMessage() );
                            }
                        } );



                    }

                    @Override
                    public void onFailure(Call<OAuthToken> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error in fetching token",Toast.LENGTH_LONG).show();

                    }
                });
            }
            if(TextUtils.isEmpty(code)) {
                //a problem occurs, the user reject our granting request or something like that
                Toast.makeText(this, "Other Error",Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    /*private void createSheetsService(Intent data) {

        Log.e("Auth","Inside create part");

            GoogleSignIn.getSignedInAccountFromIntent( data )
                    .addOnSuccessListener( new OnSuccessListener<GoogleSignInAccount>() {
                        @Override
                        public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                            GoogleAccountCredential credential = GoogleAccountCredential
                                    .usingOAuth2( MainActivity.this, Collections.singleton( SheetsScopes.SPREADSHEETS ) );

                            Log.e("Auth","success");
                            credential.setSelectedAccount( googleSignInAccount.getAccount() );
                            String authcode = googleSignInAccount.getServerAuthCode();
                            Log.e("Auth","create success");
                            Toast.makeText( MainActivity.this, authcode, Toast.LENGTH_SHORT ).show();


                        }
                    } )
                    .addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Auth","create failure");
                        }
                    } );




    }




    public void authenticationPart(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes( new Scope( SheetsScopes.SPREADSHEETS ) )
                .requestServerAuthCode( clientID )
                .build();

        Log.e("Auth","Inside Auth part");


        GoogleSignInClient client = GoogleSignIn.getClient(MainActivity.this, signInOptions);
        startActivityForResult( client.getSignInIntent(),400 );
        //activityResultLauncher.launch( client.getSignInIntent() );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        switch (requestCode){
            case 400:
                GoogleSignIn.getSignedInAccountFromIntent(data);
                createSheetsService( data );
                break;
            default:
                Log.e("Auth","Auth failed");
        }
    }

    /*ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Log.e("Auth","Result OK");
                        createSheetsService(data);
                    }else{
                        Log.e("Auth","Result Not OK");
                    }
                }
            });

     */



/*
    private Credential getCredential(NetHttpTransport HTTP_TRANSPORT, JsonFactory JSON_FACTORY) throws IOException {
        Log.e("TRY","Inside Credential");
        InputStream in = this.getResources().openRawResource(R.raw.credentials);
        //InputStream in = MainActivity.class.getResourceAsStream( "/raw/credentials.json" );
        if (in == null) {
            Log.e("TRY","IN null");
            Toast.makeText( this, "Resource not found: ", Toast.LENGTH_SHORT ).show();
            throw new FileNotFoundException("Resource not found: " +"src/main/resource/credentials.json");

        }
        String temp = in.toString();
        if(temp.isEmpty()){
            result.setText( "Empty" );
        }else {
            result.setText( temp );
        }
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(clientSecrets == null){
            Log.e("TRY","Client Secrets null");
        }else {
            result.setText( clientSecrets.getDetails().getClientId() );
        }
        File tokenFolder = new File( Environment.getExternalStorageDirectory() +
                File.separator + "tokens");
        if (!tokenFolder.exists()) {
            tokenFolder.mkdirs();
        }

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singleton(  SheetsScopes.SPREADSHEETS))
                .setDataStoreFactory(new FileDataStoreFactory(tokenFolder))
                .setAccessType("offline")
                .build();
        // Build flow and trigger user authorization request.
        /*GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singleton( SheetsScopes.SPREADSHEETS ))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .build();


        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

    }

 */
}