package com.defenders.muuvrdri.item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.activity.SingleBiddingActivity;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.constants.Constants;
import com.defenders.muuvrdri.json.BiddingSingleDataJson;
import com.defenders.muuvrdri.json.BiddngJson;
import com.defenders.muuvrdri.json.fcm.FCMMessage;
import com.defenders.muuvrdri.json.idJson;
import com.defenders.muuvrdri.json.setBiddingJson;
import com.defenders.muuvrdri.models.BankModel;
import com.defenders.muuvrdri.models.Bid;
import com.defenders.muuvrdri.models.Chat;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.PicassoTrustAll;
import com.defenders.muuvrdri.utils.TimeUtils;
import com.defenders.muuvrdri.utils.api.FCMHelper;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.defenders.muuvrdri.json.fcm.FCMType.BIDDING;
import static com.defenders.muuvrdri.json.fcm.FCMType.ORDER;


public class BiddingItem extends RecyclerView.Adapter<BiddingItem.ItemRowHolder> {

    private List<BiddngJson> dataList;
    private Context mContext;
    private int rowLayout;
    Activity activity;

    public BiddingItem(Activity activity,Context context, List<BiddngJson> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final BiddngJson singleItem = dataList.get(position);


        holder.setData(singleItem.getTimeDistance(),singleItem.getPickup(),singleItem.getDestination(),singleItem.getPrice());
        holder.setTimer(singleItem.getTimeScheduled());

        holder.tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "coming soon", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_bid);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                TextView tv10,tv50,tv100;
                AppCompatButton btnBid;
                EditText etBid;
                tv10 = dialog.findViewById(R.id.tv_10);
                tv50 = dialog.findViewById(R.id.tv_50);
                tv100 = dialog.findViewById(R.id.tv_100);
                btnBid = dialog.findViewById(R.id.btn_bid);
                etBid = dialog.findViewById(R.id.et_bid);

                tv10.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        String bidPrice = String.valueOf(Integer.parseInt(singleItem.getPrice())- 10);
                        etBid.setText(bidPrice);
                    }
                });
                tv50.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        String bidPrice = String.valueOf(Integer.parseInt(singleItem.getPrice())- 50);
                        etBid.setText(bidPrice);
                    }
                });
                tv100.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        String bidPrice = String.valueOf(Integer.parseInt(singleItem.getPrice())- 100);
                        etBid.setText(bidPrice);
                    }
                });

                btnBid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String priceBid = etBid.getText().toString();
                        if(TextUtils.isEmpty(priceBid)){
                            Toast.makeText(mContext, "enter your bidding amount", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ProgressDialog pd = new ProgressDialog(activity);
                            pd.setMessage("BIDDING...");
                            pd.setCancelable(false);
                            pd.show();
                            User userLogin = BaseApp.getInstance(mContext).getLoginUser();
                            final DriverService service = ServiceGenerator.createService(DriverService.class, userLogin.getEmail(), userLogin.getPassword());

                            final String[] userToken = new String[1];
                            idJson userJson = new idJson();
                            userJson.setId(singleItem.getUser_id());
                            service.fetchUserToken(userJson).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.isSuccessful()){

                                        //Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                        userToken[0] = response.body();
                                        holder.sendMessageToDriver(userToken[0]);

                                        setBiddingJson item = new setBiddingJson();
                                        item.setBidding_price(Double.valueOf(priceBid));
                                        item.setDriverId(userLogin.getId());
                                        item.setUser_id(singleItem.getUser_id());
                                        service.setBidding(item).enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if(response.isSuccessful()){
                                                    pd.dismiss();
                                                    if(response.body().equals("success")){
                                                        Intent intent = new Intent(mContext, SingleBiddingActivity.class);
                                                        BiddingSingleDataJson item2 = new BiddingSingleDataJson();
                                                        item2.setBidding_price(Double.valueOf(priceBid));
                                                        item2.setDriverId(userLogin.getId());
                                                        item2.setUser_id(singleItem.getUser_id());
                                                        intent.putExtra("data",item2);
                                                        activity.startActivity(intent);
                                                        activity.finish();
                                                    }
                                                    else{
                                                        Toast.makeText(mContext, "error try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    Toast.makeText(mContext, "response error", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {

                                                pd.dismiss();
                                                Toast.makeText(mContext, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                    else{
                                        pd.dismiss();
                                        Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                    pd.dismiss();
                                    Toast.makeText(mContext, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {


        TextView tvTimer,tvTime,tvPickup,tvDestination,tvMap,tvPrice,tvBid;

        ItemRowHolder(View itemView) {
            super(itemView);
            tvTimer = itemView.findViewById(R.id.tv_timer);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPickup = itemView.findViewById(R.id.tv_pickup);
            tvDestination = itemView.findViewById(R.id.tv_destination);
            tvMap = itemView.findViewById(R.id.tv_map);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvBid = itemView.findViewById(R.id.tv_bid);

        }

        private void setData(String time,String pickup,String destination,String price){
            String halfPick,halfDes;
            if (pickup.length() >= 10) {

                halfPick = pickup.substring(0, 15) + "...";
            } else {
                halfPick = pickup;
            }
            if (destination.length() >= 10) {

                halfDes = destination.substring(0, 15) + "...";
            } else {
                halfDes = destination;
            }
            tvTime.setText(time);
            tvPickup.setText(halfPick);
            tvDestination.setText(halfDes);
            tvPrice.setText("₹"+price);
        }

        private void setTimer(String timer){
            long remaining_millis = Long.parseLong(timer) - TimeUtils.getMillisFrom(TimeUtils.getTime());
            startTimer(remaining_millis);
        }
        private void startTimer(long remaining_millis) {

            CountDownTimer countDownTimer = new CountDownTimer(remaining_millis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //update button
                    tvTimer.setText(getFormattedTime(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    //Toast.makeText(biddingDetailsActivity.this, "finish", Toast.LENGTH_SHORT).show();
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

        private void sendMessageToDriver(final String regID) {

            final FCMMessage message = new FCMMessage();
            message.setTo(regID);
            Bid bid  = new Bid();
            message.setData(bid);

            FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                    Log.e("REQUEST TO DRIVER", message.getData().toString());
                }

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }
}
