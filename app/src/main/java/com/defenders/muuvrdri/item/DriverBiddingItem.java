package com.defenders.muuvrdri.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.json.BiddingDriversJson;
import com.defenders.muuvrdri.json.BiddngJson;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class DriverBiddingItem extends RecyclerView.Adapter<DriverBiddingItem.ItemRowHolder> {

    private List<BiddingDriversJson> dataList;
    private Context mContext;
    private int rowLayout;
    private String pick,drop;
    Activity activity;
    BiddngJson itemMain;

    public DriverBiddingItem(BiddngJson itemMain, Activity activity, Context context, List<BiddingDriversJson> dataList, int rowLayout, String pick, String drop) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.pick = pick;
        this.drop = drop;
        this.activity = activity;
        this.itemMain = itemMain;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final BiddingDriversJson singleItem = dataList.get(position);

        holder.tvName.setText(singleItem.getDriver_name());
        holder.tvPrice.setText(""+singleItem.getBidding_price());
        holder.tvPick.setText(pick);
        holder.tvDrop.setText(drop);


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPick,tvDrop,tvPrice,tvTime;

        CardView cardViewDriverBidding;

        ItemRowHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_driver_name);
            tvPick = itemView.findViewById(R.id.tv_pick);
            tvDrop = itemView.findViewById(R.id.tv_drop);

            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTime = itemView.findViewById(R.id.tv_times);
            cardViewDriverBidding = itemView.findViewById(R.id.card_driver_bidding);
        }
    }
}
