package com.defenders.muuvrdri.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.json.BiddingResponseJson;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBiddingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bidding);


        User userLogin = BaseApp.getInstance(this).getLoginUser();


        final DriverService service = ServiceGenerator.createService(DriverService.class, userLogin.getEmail(), userLogin.getPassword());


        service.fetchBidding().enqueue(new Callback<BiddingResponseJson>() {
            @Override
            public void onResponse(Call<BiddingResponseJson> call, Response<BiddingResponseJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("success")) {
                        Toast.makeText(AllBiddingActivity.this, response.body().getData().get(0).getUser_id(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AllBiddingActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AllBiddingActivity.this, "response fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BiddingResponseJson> call, Throwable t) {
                Toast.makeText(AllBiddingActivity.this, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
