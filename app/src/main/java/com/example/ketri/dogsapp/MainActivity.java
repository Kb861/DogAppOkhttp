package com.example.ketri.dogsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ketri.dogsapp.Adapter.DogListAdapter;
import com.example.ketri.dogsapp.Adapter.RecyclerViewClickListener;
import com.example.ketri.dogsapp.Model.BreedPhoto;
import com.example.ketri.dogsapp.Model.DogListModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "http://dog.ceo/api/";
    public OkHttpClient client;
    private Gson gson = new Gson();
    private BreedPhoto breedPhoto;
    private DogListModel dogListModel;
    private DogListAdapter dogListAdapter;
    private ArrayList<String> dogs = new ArrayList<>();

    @BindView(R.id.dogs_rv)
    RecyclerView recycler;

    @BindView(R.id.random_dogIV)
    ImageView randomDogImage;

    @BindView(R.id.show_random_dog_btn)
    Button show_random_dog_btn;

    @OnClick(R.id.show_random_dog_btn)
    void showDog() {
        getBreedPhoto("breeds/image/random");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createClient();
        setAdapter();
        try {
            getDogList("breeds/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position, String name) {
                String url = "breed/"+name+"/images/random";
                getBreedPhoto(url);
            }
        };
        dogListAdapter = new DogListAdapter(dogs,recyclerViewClickListener);
        recycler.setAdapter(dogListAdapter);
    }

    private void createClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    private Request createRequest(String url){
        return new Request.Builder().url(BASE_URL + url).build();
    }
    private void getBreedPhoto(String url) {
        Request request = createRequest(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                breedPhoto = gson.fromJson(responseBody.string(),BreedPhoto.class);
                if(breedPhoto.getStatus().equals("success"))
                {
                    runOnUiThread();
                }
            }
        });

    }

    private void runOnUiThread() {
        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String photo = breedPhoto.getMessage();
                    Glide.with(MainActivity.this).load(photo).into(randomDogImage);
                }
        });
    }

    private void getDogList(String url)throws IOException{
        Request request=createRequest(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                dogListModel = gson.fromJson(responseBody.string(),DogListModel.class);
                if (dogListModel.getStatus().equals("success"))
                {
                    dogs.addAll(dogListModel.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dogListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
