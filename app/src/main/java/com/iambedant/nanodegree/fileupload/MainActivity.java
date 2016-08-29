package com.iambedant.nanodegree.fileupload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final int PICKFILE_REQUEST_CODE = 1;
    Context mContext;
    TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        mTextViewResult = (TextView) findViewById(R.id.result);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
    }

    private void uploadFile(Uri fileUri) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image_request[image]", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "en-US";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        mTextViewResult.setText("Requesting...");
        Call<com.iambedant.nanodegree.fileupload.Response> call = service.uploadPhoto("CloudSight FZo7QixPuVNpXroQvmq8qg", description, body);
        call.enqueue(new Callback<com.iambedant.nanodegree.fileupload.Response>() {
            @Override
            public void onResponse(Call<com.iambedant.nanodegree.fileupload.Response> call,
                                   Response<com.iambedant.nanodegree.fileupload.Response> response) {
                Log.v("Upload", "success");
                if (response.body().getStatus().equals("completed")) {
                    mTextViewResult.setText(response.body().getName()+"");
                } else if (response.body().getStatus().equals("not completed")) {
                    getResult(response.body().getToken());
                }
            }

            @Override
            public void onFailure(Call<com.iambedant.nanodegree.fileupload.Response> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }


    public void getResult(String token) {
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        mTextViewResult.setText("Requesting...");
        Call<com.iambedant.nanodegree.fileupload.Response> call = service.getImage("CloudSight FZo7QixPuVNpXroQvmq8qg",token);
        call.enqueue(new Callback<com.iambedant.nanodegree.fileupload.Response>() {
            @Override
            public void onResponse(Call<com.iambedant.nanodegree.fileupload.Response> call,
                                   Response<com.iambedant.nanodegree.fileupload.Response> response) {
                Log.v("Upload", "success");
                if (response.body().getStatus().equals("completed")) {
                    mTextViewResult.setText(response.body().getName()+"");
                } else if (response.body().getStatus().equals("not completed")) {
                    getResult(response.body().getToken());
                }
            }

            @Override
            public void onFailure(Call<com.iambedant.nanodegree.fileupload.Response> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void openFile() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        //If you want to open any file
        intent.setType("*/*");

        /*
        //for pdf files
         intent.setType("application/pdf");

         //Multiple Mime Type
         intent.setType("image/*|application/pdf|audio/*");

         */

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        //  sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, PICKFILE_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICKFILE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                uploadFile(uri);
                // toastUri(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
