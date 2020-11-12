package com.defenders.muuvrdri.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.item.DriverBiddingItem;
import com.defenders.muuvrdri.json.BiddingDriversResponseJson;
import com.defenders.muuvrdri.json.BiddingResponseJson;
import com.defenders.muuvrdri.json.BiddingResponseSingleJson;
import com.defenders.muuvrdri.json.BiddingSingleDataJson;
import com.defenders.muuvrdri.json.BiddngJson;
import com.defenders.muuvrdri.json.idJson;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.TimeUtils;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleBiddingActivity extends AppCompatActivity {

    private int time;
    private CountDownTimer countDownTimer;

    ProgressDialog pd;
    long timeScheduled;
    TextView tvTimer;

    TextView tvPickup,tvDestination;
    RecyclerView rvDriverBidding;
    RecyclerView.LayoutManager layoutManager;
    BiddngJson itemMain;

    ImageView ivUpBid,ivDownBid;
    TextView tvSinglePrice;

    BiddingSingleDataJson mainData;

    Button btnCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bidding);
        tvPickup = findViewById(R.id.tv_pickup);
        tvDestination = findViewById(R.id.tv_destination);

        rvDriverBidding = findViewById(R.id.rv_bidding_drivers);
        layoutManager = new LinearLayoutManager(this);
        rvDriverBidding.setLayoutManager(layoutManager);

        ivUpBid = findViewById(R.id.iv_upbid);
        ivDownBid = findViewById(R.id.iv_downbid);
        tvSinglePrice = findViewById(R.id.tv_bidprice);

        btnCancel = findViewById(R.id.btn_cancel);



        tvTimer = findViewById(R.id.tv_timer);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();

        mainData = (BiddingSingleDataJson) getIntent().getExtras().get("data");

        tvSinglePrice.setText(mainData.getBidding_price().toString());

        ivUpBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleBiddingActivity.this, "cannot change right now", Toast.LENGTH_SHORT).show();
            }
        });
        ivDownBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SingleBiddingActivity.this, "cannot change right now", Toast.LENGTH_SHORT).show();
            }
        });



        User userLogin = BaseApp.getInstance(this).getLoginUser();
        final DriverService service = ServiceGenerator.createService(DriverService.class, userLogin.getEmail(), userLogin.getPassword());
        idJson item = new idJson();
        item.setId(mainData.getUser_id());


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(SingleBiddingActivity.this, R.style.DialogStyle)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("CANCEL YOUR BID")
                        .setMessage("Are you sure?")
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ProgressDialog pd = new ProgressDialog(SingleBiddingActivity.this);
                                pd.setMessage("cancelling bid...");
                                pd.setCancelable(false);
                                pd.show();
                                idJson item2 = new idJson();
                                item2.setId(userLogin.getId());
                                service.deleteBidding(item2).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(SingleBiddingActivity.this, "Cancelled successfully", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                            finish();
                                        }
                                        else{
                                            pd.dismiss();
                                            Toast.makeText(SingleBiddingActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        pd.dismiss();
                                        Toast.makeText(SingleBiddingActivity.this, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        service.fetchBiddingDetails(item).enqueue(new Callback<BiddingResponseSingleJson>() {
            @Override
            public void onResponse(Call<BiddingResponseSingleJson> call, Response<BiddingResponseSingleJson> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("success")){
                        pd.dismiss();
                        itemMain = response.body().getData();
                        String pick = response.body().getData().getPickup();
                        String destination = response.body().getData().getDestination();
                        String halfPick,halfDes;
                        if (pick.length() >= 10) {

                            halfPick = pick.substring(0, 10) + "...";
                        } else {
                            halfPick = pick;
                        }
                        if (destination.length() >= 10) {

                            halfDes = destination.substring(0, 10) + "...";
                        } else {
                            halfDes = destination;
                        }
                        tvPickup.setText(halfPick);
                        tvDestination.setText(halfDes);
                        long remaining_millis = Long.parseLong(response.body().getData().getTimeScheduled()) - TimeUtils.getMillisFrom(TimeUtils.getTime());
                        startTimer(remaining_millis);
                    }
                    else{
                        pd.dismiss();
                        Toast.makeText(SingleBiddingActivity.this, "no bidding request found", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    pd.dismiss();
                    Toast.makeText(SingleBiddingActivity.this, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BiddingResponseSingleJson> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(SingleBiddingActivity.this, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        service.fetchBiddingDrivers(item).enqueue(new Callback<BiddingDriversResponseJson>() {
            @Override
            public void onResponse(Call<BiddingDriversResponseJson> call, Response<BiddingDriversResponseJson> response) {
                if(response.isSuccessful()){
                    //Toast.makeText(biddingDetailsActivity.this, "successful", Toast.LENGTH_SHORT).show();
                    if(response.body().getMessage().equals("success")){
                        //Toast.makeText(SingleBiddingActivity.this, ""+response.body().getData().get(0).getDriver_name(), Toast.LENGTH_SHORT).show();
                        DriverBiddingItem adapter = new DriverBiddingItem(itemMain,SingleBiddingActivity.this,getApplicationContext(),response.body().getData(),R.layout.item_driverlist,tvPickup.getText().toString(),tvDestination.getText().toString());
                        rvDriverBidding.setAdapter(adapter);
                    }
                    else{

                    }

                }else{
                    Toast.makeText(SingleBiddingActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BiddingDriversResponseJson> call, Throwable t) {

                Toast.makeText(SingleBiddingActivity.this, "error:" +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void startTimer(long remaining_millis) {

        countDownTimer = new CountDownTimer(remaining_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //update button
                tvTimer.setText(getFormattedTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                //Toast.makeText(SingleBiddingActivity.this, "your bidding time finished", Toast.LENGTH_SHORT).show();
            }
        }.start();

    }
    @SuppressLint("DefaultLocale")
    private String getFormattedTime(long timeLeftInMillis) {
        int inSecond = (int) (timeLeftInMillis / 1000);
        int hour = inSecond / 3600;
        int min = (inSecond - hour*3600) / 60;
        int sec = inSecond % 60;
        if (inSecond > 60) {
            return String.format("%d:%d:%d", hour, min, sec);
        } else {
            return String.format("%d:%d:%d", hour, min, inSecond);
        }
    }
}
