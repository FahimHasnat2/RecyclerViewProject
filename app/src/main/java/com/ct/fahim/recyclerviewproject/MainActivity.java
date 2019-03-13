package com.ct.fahim.recyclerviewproject;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    String data;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set the view ex: list/grid

        new myAsyncTask().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String fetchDataFromUrl (String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                assert response.body() != null;
                return response.body().string();
            }
    }

    class myAsyncTask extends AsyncTask {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                data = fetchDataFromUrl("https://api.androidhive.info/contacts/");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            MyContacts myContacts = new Gson().fromJson(data, MyContacts.class);
            recyclerView.setAdapter(new MyAdapter(MainActivity.this, myContacts.getContacts()));
        }
    }
}
