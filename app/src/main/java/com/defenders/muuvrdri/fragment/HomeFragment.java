package com.defenders.muuvrdri.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.defenders.muuvrdri.activity.AllBiddingActivity;
import com.defenders.muuvrdri.activity.SingleBiddingActivity;
import com.defenders.muuvrdri.json.BiddingSingleDataResponseJson;
import com.defenders.muuvrdri.json.idJson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.activity.TopupSaldoActivity;
import com.defenders.muuvrdri.activity.WalletActivity;
import com.defenders.muuvrdri.activity.WithdrawActivity;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.json.GetOnRequestJson;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;
import com.defenders.muuvrdri.item.BanklistItem;
import com.defenders.muuvrdri.json.ResponseJson;
import com.defenders.muuvrdri.models.BankModels;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.SettingPreference;
import com.defenders.muuvrdri.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Home";
    private Context context;
    private GoogleMap gMap;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private GoogleApiClient googleApiClient;
    private TextView saldo;
    private RelativeLayout rlprogress;
    private Button onoff, uangbelanja,btnBidding;
    private SettingPreference sp;
    private ArrayList<BankModels> mList;
    private String statusdriver, saldodriver;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        RelativeLayout topup = getView.findViewById(R.id.topup);
        RelativeLayout withdraw = getView.findViewById(R.id.withdraw);
        RelativeLayout detail = getView.findViewById(R.id.detail);
        saldo = getView.findViewById(R.id.saldo);
        uangbelanja = getView.findViewById(R.id.maks);
        onoff = getView.findViewById(R.id.onoff);
        btnBidding = getView.findViewById(R.id.btnBidding);
        mList = new ArrayList<>();
        sp = new SettingPreference(context);
        rlprogress = getView.findViewById(R.id.rlprogress);
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TopupSaldoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        sp.updateNotif("OFF");
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WithdrawActivity.class);
                i.putExtra("type","withdraw");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WalletActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        uangbelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //rlprogress.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            statusdriver = bundle.getString("status");
            saldodriver = bundle.getString("saldo");
        }
        if (statusdriver.equals("1")) {
            rlprogress.setVisibility(View.GONE);
            sp.updateKerja("ON");
            onoff.setSelected(true);
            onoff.setText("ON");
        } else if (statusdriver.equals("4")) {
            rlprogress.setVisibility(View.GONE);
            onoff.setSelected(false);
            onoff.setText("OFF");
            sp.updateKerja("OFF");
        }

        onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getturnon();
            }
        });

        btnBidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userLogin = BaseApp.getInstance(getContext()).getLoginUser();
                final DriverService service = ServiceGenerator.createService(DriverService.class, userLogin.getEmail(), userLogin.getPassword());

                idJson item = new idJson();
                item.setId(userLogin.getId());

                service.checkBidding(item).enqueue(new Callback<BiddingSingleDataResponseJson>() {
                    @Override
                    public void onResponse(Call<BiddingSingleDataResponseJson> call, Response<BiddingSingleDataResponseJson> response) {
                        if(response.isSuccessful()){
                            if(response.body().getMessage().equals("success")){
                                Intent intent = new Intent(getContext(), SingleBiddingActivity.class);
                                intent.putExtra("data",response.body().getData());
                                startActivity(intent);
                            }else{
                                startActivity(new Intent(getContext(), AllBiddingActivity.class));
                            }
                        }
                        else{
                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BiddingSingleDataResponseJson> call, Throwable t) {
                        Toast.makeText(context, "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





            }
        });


        if (sp.getSetting()[1].isEmpty()) {
            Utility.currencyTXT(uangbelanja, "100000", context);
        } else if (sp.getSetting()[1].equals("Unlimited")) {
            uangbelanja.setText(sp.getSetting()[1]);
        } else {
            Utility.currencyTXT(uangbelanja, sp.getSetting()[1], context);
        }

        List<BankModels> items = getPeopleData(context);
        mList.addAll(items);


        return getView;
    }

    private static List<BankModels> getPeopleData(Context ctx) {
        List<BankModels> items = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.list_maximum);
        String[] name_arr = ctx.getResources().getStringArray(R.array.list_maximum);

        for (int i = 0; i < drw_arr.length(); i++) {
            BankModels obj = new BankModels();
            obj.setText(name_arr[i]);
            items.add(obj);
        }
        return items;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.getUiSettings().setMapToolbarEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


        updateLastLocation();
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (lastKnownLocation != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15f)
            );

            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        updateLastLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        updateLastLocation();
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        sp.updateAutoBid("OFF");

        Utility.currencyTXT(saldo, saldodriver, context);
    }

    private void getturnon() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetOnRequestJson param = new GetOnRequestJson();
        param.setId(loginUser.getId());
        if (statusdriver.equals("1")) {
            param.setOn(false);
        } else {
            param.setOn(true);
        }

        userService.turnon(param).enqueue(new Callback<ResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    rlprogress.setVisibility(View.GONE);
                    statusdriver = Objects.requireNonNull(response.body()).getData();
                    if (response.body().getData().equals("1")) {
                        sp.updateKerja("ON");
                        onoff.setSelected(true);
                        onoff.setText("ON");
                    } else if (response.body().getData().equals("4")) {
                        sp.updateKerja("OFF");
                        onoff.setSelected(false);
                        onoff.setText("OFF");
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void dialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bank);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final ImageView close = dialog.findViewById(R.id.close);
        final RecyclerView list = dialog.findViewById(R.id.recycleview);


        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(context, 1));

        BanklistItem bankItem = new BanklistItem(context, mList, R.layout.item_petunjuk, new BanklistItem.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(BankModels item) {
                if (item.getText().equals("Unlimited")) {
                    uangbelanja.setText(item.getText());
                } else {
                    Utility.currencyTXT(uangbelanja, item.getText(), context);
                }
                sp.updateMaksimalBelanja(item.getText());
                dialog.dismiss();
            }
        });

        list.setAdapter(bankItem);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
