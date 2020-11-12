package com.defenders.muuvrdri.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.constants.Constants;
import com.defenders.muuvrdri.constants.VersionChecker;
import com.defenders.muuvrdri.fragment.HomeFragment;
import com.defenders.muuvrdri.fragment.OrderFragment;
import com.defenders.muuvrdri.json.GetHomeRequestJson;
import com.defenders.muuvrdri.json.GetHomeResponseJson;
import com.defenders.muuvrdri.json.UpdateLocationRequestJson;
import com.defenders.muuvrdri.models.PayuModel;
import com.defenders.muuvrdri.utils.MyLocationService;
import com.defenders.muuvrdri.fragment.HistoryFragment;
import com.defenders.muuvrdri.fragment.MessageFragment;
import com.defenders.muuvrdri.fragment.ProfileFragment;
import com.defenders.muuvrdri.json.ResponseJson;
import com.defenders.muuvrdri.models.TransaksiModel;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;
import com.defenders.muuvrdri.utils.SettingPreference;


import java.util.Objects;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    long mBackPressed;


    public static String apikey;

    LinearLayout mAdViewLayout;
    RelativeLayout toolbar;

    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;
    private FragmentManager fragmentManager;
    BottomNavigationView navigation;
    int previousSelect = 0;
    SettingPreference sp;
    OrderFragment orderFragment;
    HomeFragment homeFragment;
    LocationRequest locationRequest;
    RelativeLayout rlprogress;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean canceled;

    public static MainActivity getInstance() {
        return mainActivity;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Menu menu = navigation.getMenu();
            menu.findItem(R.id.home).setIcon(R.drawable.ic_home);
            menu.findItem(R.id.order).setIcon(R.drawable.ic_transaksi);
            menu.findItem(R.id.chat).setIcon(R.drawable.ic_pesan);
            menu.findItem(R.id.profile).setIcon(R.drawable.ic_profil);

            TransaksiModel transaksi = new TransaksiModel();
            switch (item.getItemId()) {
                case R.id.home:
                    canceled = false;
                    navigationItemSelected(0);
                    item.setIcon(R.drawable.ic_home_s);
                    gethome();
                    toolbar.setVisibility(View.VISIBLE);
                    return true;
                case R.id.order:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    HistoryFragment listFragment = new HistoryFragment();
                    navigationItemSelected(1);
                    item.setIcon(R.drawable.ic_transaksi_s);
                    loadFrag2(listFragment, getString(R.string.menu_home), fragmentManager, transaksi, "", "");
                    toolbar.setVisibility(View.VISIBLE);
                    return true;
                case R.id.chat:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    MessageFragment pesanFragment = new MessageFragment();
                    navigationItemSelected(2);
                    item.setIcon(R.drawable.ic_pesan_s);
                    loadFrag2(pesanFragment, getString(R.string.menu_chat), fragmentManager, transaksi, "", "");
                    toolbar.setVisibility(View.VISIBLE);
                    return true;
                case R.id.profile:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    ProfileFragment profilFragment = new ProfileFragment();
                    navigationItemSelected(3);
                    item.setIcon(R.drawable.ic_profil_s);
                    loadFrag2(profilFragment, getString(R.string.menu_profile), fragmentManager, transaksi, "", "");
                    toolbar.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdViewLayout = findViewById(R.id.adView);
        fragmentManager = getSupportFragmentManager();
        navigation = findViewById(R.id.navigation);
        sp = new SettingPreference(this);
        orderFragment = new OrderFragment();
        homeFragment = new HomeFragment();
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        Menu menu = navigation.getMenu();
        toolbar = findViewById(R.id.rltoolbar);
        toolbar.setVisibility(View.VISIBLE);
        rlprogress = findViewById(R.id.rlprogress);
        menu.findItem(R.id.home).setIcon(R.drawable.ic_home_s);
        canceled = false;
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        Constants.TOKEN = loginUser.getToken();
        Constants.USERID = loginUser.getId();
        apikey = getString(R.string.google_maps_key);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Constants.versionname = Objects.requireNonNull(packageInfo).versionName;
        updatelocation();
        gethome();
        if (sp.getSetting()[19].equals("2") || sp.getSetting()[19].equals("3")) {
          navigation.setVisibility(View.GONE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        Check_version();

    }

    public void Check_version(){
        VersionChecker versionChecker = new VersionChecker(this);
        versionChecker.execute();
    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                clickDone();

            }
        } else {
            super.onBackPressed();
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
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

    private void gethome() {

        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetHomeRequestJson param = new GetHomeRequestJson();
        param.setId(loginUser.getId());
        param.setPhone(loginUser.getNoTelepon());
        userService.home(param).enqueue(new Callback<GetHomeResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<GetHomeResponseJson> call, @NonNull final Response<GetHomeResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        PayuModel payu = response.body().getPayu().get(0);
                        Constants.CURRENCY = response.body().getCurrency();
                        sp.updateCurrency(response.body().getCurrency());
                        sp.updateabout(response.body().getAboutus());
                        sp.updateemail(response.body().getEmail());
                        sp.updatephone(response.body().getPhone());
                        sp.updateweb(response.body().getWebsite());
                        sp.updatePaypal(response.body().getPaypalkey());
                        sp.updatepaypalmode(response.body().getPaypalmode());
                        sp.updatepaypalactive(response.body().getPaypalactive());
                        sp.updatestripeactive(response.body().getStripeactive());
                        sp.updatecurrencytext(response.body().getCurrencytext());
                        sp.updatePayudebug(payu.getPayudebug());
                        sp.updatePayumerchantid(payu.getPayuid());
                        sp.updatePayusalt(payu.getPayusalt());
                        sp.updatePayumerchantkey(payu.getPayukey());
                        sp.updatePayuActive(payu.getActive());
                        sp.updateStatusdriver(response.body().getDriverstatus());

                        TransaksiModel transaksifake = new TransaksiModel();
                        if (!canceled) {
                            if (response.body().getDriverstatus().equals("3") || response.body().getDriverstatus().equals("2")) {
                                TransaksiModel transaksi = response.body().getTransaksi().get(0);
                                navigation.setVisibility(View.GONE);
                                loadFrag2(orderFragment, getString(R.string.menu_home), fragmentManager, transaksi, response.body().getSaldo(), response.body().getDriverstatus());
                            } else {
                                navigation.setVisibility(View.VISIBLE);
                                loadFrag2(homeFragment, getString(R.string.menu_home), fragmentManager, transaksifake, response.body().getSaldo(), response.body().getDriverstatus());
                            }
                        }
                        User user = response.body().getDatadriver().get(0);
                        saveUser(user);
                        if (mainActivity != null) {
                            Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                            User loginUser = BaseApp.getInstance(MainActivity.this).getLoginUser();
                            realm.beginTransaction();
                            loginUser.setWalletSaldo(Long.parseLong(response.body().getSaldo()));
                            realm.commitTransaction();
                        }
                        rlprogress.setVisibility(View.GONE);
                    } else {
                        Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        BaseApp.getInstance(MainActivity.this).setLoginUser(null);
                        startActivity(new Intent(MainActivity.this, IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                        Toast.makeText(MainActivity.this, "Your account has been suspended, please contact admin!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetHomeResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(MainActivity.this).setLoginUser(user);
    }

    public void loadFrag2(Fragment f1, String name, FragmentManager fm, TransaksiModel transaksi, String saldo, String status) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle args = new Bundle();
        args.putString("id_pelanggan", transaksi.getIdPelanggan());
        args.putString("id_transaksi", transaksi.getId());
        args.putString("response", String.valueOf(transaksi.status));
        args.putString("saldo", saldo);
        args.putString("status", status);
        f1.setArguments(args);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.Container, f1, name);
        ft.commitAllowingStateLoss();
    }

    public void navigationItemSelected(int position) {
        previousSelect = position;
    }

    private void updatelocation() {
        buildlocation();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildlocation() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void Updatelocationdata(final Location location) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                onLocationChanged(location);
            }
        });

    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            User loginUser = BaseApp.getInstance(this).getLoginUser();
            DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
            UpdateLocationRequestJson param = new UpdateLocationRequestJson();

            param.setId(loginUser.getId());
            param.setLatitude(String.valueOf(location.getLatitude()));
            param.setLongitude(String.valueOf(location.getLongitude()));
            param.setBearing(String.valueOf(location.getBearing()));

            service.updatelocation(param).enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                    if (response.isSuccessful()) {
                        Log.e("location", response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

                }
            });
        }
    }


}
