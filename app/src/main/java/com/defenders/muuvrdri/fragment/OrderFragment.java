package com.defenders.muuvrdri.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.defenders.muuvrdri.json.ConsentRequestJson;
import com.defenders.muuvrdri.json.ConsentResponseJson;
import com.defenders.muuvrdri.json.FinishRequestJson;
import com.defenders.muuvrdri.json.idJson;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.defenders.muuvrdri.R;
import com.defenders.muuvrdri.activity.ChatActivity;
import com.defenders.muuvrdri.activity.MainActivity;
import com.defenders.muuvrdri.constants.BaseApp;
import com.defenders.muuvrdri.constants.Constants;
import com.defenders.muuvrdri.gmap.directions.Directions;
import com.defenders.muuvrdri.gmap.directions.Route;
import com.defenders.muuvrdri.item.ItemPesananItem;
import com.defenders.muuvrdri.json.AcceptRequestJson;
import com.defenders.muuvrdri.json.AcceptResponseJson;
import com.defenders.muuvrdri.json.ResponseJson;
import com.defenders.muuvrdri.json.VerifyRequestJson;
import com.defenders.muuvrdri.utils.api.FCMHelper;
import com.defenders.muuvrdri.utils.api.MapDirectionAPI;
import com.defenders.muuvrdri.utils.api.ServiceGenerator;
import com.defenders.muuvrdri.utils.api.service.DriverService;
import com.defenders.muuvrdri.json.DetailRequestJson;
import com.defenders.muuvrdri.json.DetailTransResponseJson;
import com.defenders.muuvrdri.json.fcm.FCMMessage;
import com.defenders.muuvrdri.models.OrderFCM;
import com.defenders.muuvrdri.models.PelangganModel;
import com.defenders.muuvrdri.models.TransaksiModel;
import com.defenders.muuvrdri.models.User;
import com.defenders.muuvrdri.utils.Log;
import com.defenders.muuvrdri.utils.Utility;
import com.defenders.muuvrdri.utils.PicassoTrustAll;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OrderFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context context;
    private GoogleMap gMap;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private String idtrans, idpelanggan, response, fitur, onsubmit;


    byte[] imageByteArray;
    Bitmap decoded;
    ImageView consentLetterImage;
    Button btnSubmitConsentLetter;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
    @BindView(R.id.verifycation)
    TextView verify;
    @BindView(R.id.namamerchant)
    TextView namamerchant;
    @BindView(R.id.llchat)
    LinearLayout llchat;
    @BindView(R.id.background)
    CircleImageView foto;
    @BindView(R.id.pickUpText)
    TextView pickUpText;
    @BindView(R.id.destinationText)
    TextView destinationText;
    @BindView(R.id.fitur)
    TextView fiturtext;
    @BindView(R.id.distance)
    TextView distanceText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.deliveryfee)
    TextView deliveryfee;
    @BindView(R.id.phonenumber)
    ImageView phone;

    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.phonemerchant)
    ImageView phonemerchant;
    @BindView(R.id.chatmerchant)
    ImageView chatmerchant;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.orderdetail)
    LinearLayout llorderdetail;
    @BindView(R.id.lldistance)
    LinearLayout lldistance;
    @BindView(R.id.senddetail)
    LinearLayout lldetailsend;
    @BindView(R.id.produk)
    TextView produk;
    @BindView(R.id.sendername)
    TextView sendername;
    @BindView(R.id.receivername)
    TextView receivername;
    @BindView(R.id.senderphone)
    Button senderphone;
    @BindView(R.id.receiverphone)
    Button receiverphone;
    @BindView(R.id.shimmerlayanan)
    ShimmerFrameLayout shimmerlayanan;
    @BindView(R.id.shimmerpickup)
    ShimmerFrameLayout shimmerpickup;
    @BindView(R.id.shimmerdestination)
    ShimmerFrameLayout shimmerdestination;
    @BindView(R.id.shimmerfitur)
    ShimmerFrameLayout shimmerfitur;
    @BindView(R.id.shimmerdistance)
    ShimmerFrameLayout shimmerdistance;
    @BindView(R.id.shimmerprice)
    ShimmerFrameLayout shimmerprice;
    @BindView(R.id.order)
    Button submit;
    @BindView(R.id.merchantdetail)
    LinearLayout llmerchantdetail;
    @BindView(R.id.merchantinfo)
    LinearLayout llmerchantinfo;
    @BindView(R.id.llbutton)
    LinearLayout llbutton;
    @BindView(R.id.merchantnear)
    RecyclerView rvmerchantnear;
    private ItemPesananItem itemPesananItem;
    private TextView totaltext;
    private String type;

    private String consentImageName = "";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = Objects.requireNonNull(inflater).inflate(R.layout.activity_detail_order, container, false);
        context = getContext();
        ButterKnife.bind(this, getView);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        totaltext = getView.findViewById(R.id.totaltext);
        fitur = "0";
        type = "0";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idpelanggan = bundle.getString("id_pelanggan");
            idtrans = bundle.getString("id_transaksi");
            response = bundle.getString("response");
        }
        shimmerload();
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
        switch (response) {
            case "2":
                onsubmit = "2";
                llchat.setVisibility(View.VISIBLE);
                break;
            case "3":
                onsubmit = "3";
                llchat.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
                submit.setText("finish");
                break;
            case "4":
                llchat.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_finish));
                break;
            case "5":
                llchat.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_cancel));
                break;
        }
        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rlprogress.setVisibility(View.GONE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));
        return getView;
    }

    private void getData(final String idtrans, final String idpelanggan) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdPelanggan(idpelanggan);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<DetailTransResponseJson> call, @NonNull Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    shimmertutup();
                    Log.e("", String.valueOf(Objects.requireNonNull(responsedata.body()).getData().get(0)));
                    final TransaksiModel transaksi = responsedata.body().getData().get(0);
                    final PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);
                    type = transaksi.getHome();

                    if (transaksi.isPakaiWallet()) {
                        totaltext.setText("Total (Wallet)");
                    } else {
                        totaltext.setText("Total (Cash)");
                    }

                    if (onsubmit.equals("2")) {
                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("Go buy orders");
                            submit.setText("deliver orders");
                            verify.setVisibility(View.VISIBLE);
                        } else {
                            layanandesk.setText(getString(R.string.notification_accept));
                        }
                        submit.setVisibility(View.VISIBLE);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (transaksi.getHome().equals("4")) {

                                    if (verify.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Please enter verify code!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                        String finalDate = timeFormat.format(transaksi.getWaktuOrder());
                                        rlprogress.setVisibility(View.VISIBLE);
                                        verify(verify.getText().toString(), pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);
                                    }
                                } else {
                                    start(pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, String.valueOf(transaksi.getWaktuOrder()));
                                }

                            }
                        });
                    } else if (onsubmit.equals("3")) {
                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("deliver orders");
                        } else {
                            layanandesk.setText(getString(R.string.notification_start));
                        }

                        verify.setVisibility(View.GONE);
                        submit.setText("Finish");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                ///here upload the consent latter
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                dialog.setContentView(R.layout.dialog_upload_consent_letter);
                                dialog.setCancelable(true);

                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



                                consentLetterImage = dialog.findViewById(R.id.imageview_consent_letter);

                                btnSubmitConsentLetter = dialog.findViewById(R.id.btn_consent_letter_submit);

                                ImageView btnClose = dialog.findViewById(R.id.bt_close);

                                btnClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                btnSubmitConsentLetter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(btnSubmitConsentLetter.getText().toString().equals("Upload")){
                                                if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                                                {
                                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                    startActivityForResult(intent, 1);
                                                }


                                        }
                                        else{
                                            ProgressDialog pd = new ProgressDialog(getContext());
                                            pd.setMessage("uploading...");
                                            pd.setCancelable(false);
                                            pd.show();
                                            User loginUser = BaseApp.getInstance(context).getLoginUser();
                                            DriverService driverService = ServiceGenerator.createService(
                                                    DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());

                                            ConsentRequestJson request = new ConsentRequestJson();
                                            request.setPhoto(getStringImage(decoded));

                                            driverService.uploadConsent(request).enqueue(new Callback<ConsentResponseJson>() {
                                                @Override
                                                public void onResponse(Call<ConsentResponseJson> call, Response<ConsentResponseJson> response) {
                                                    if(response.isSuccessful()){
                                                        pd.dismiss();
                                                       dialog.dismiss();
                                                        //Toast.makeText(getContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        consentImageName = response.body().getMessage();
                                                        if(!consentImageName.equals("")) {
                                                            finish(pelanggan, transaksi.token_merchant);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ConsentResponseJson> call, Throwable t) {
                                                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
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

                    fitur = transaksi.getOrderFitur();

                    if (transaksi.getHome().equals("3")) {
                        lldestination.setVisibility(View.GONE);
                        lldistance.setVisibility(View.GONE);
                        fiturtext.setText(transaksi.getEstimasi());
                    } else if (transaksi.getHome().equals("4")) {
                        llorderdetail.setVisibility(View.VISIBLE);
                        llmerchantdetail.setVisibility(View.VISIBLE);
                        llmerchantinfo.setVisibility(View.VISIBLE);
                        Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), context);
                        Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), context);
                        namamerchant.setText(transaksi.getNama_merchant());
                        itemPesananItem = new ItemPesananItem(responsedata.body().getItem(), R.layout.item_pesanan);
                        rvmerchantnear.setAdapter(itemPesananItem);

                        phonemerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Customer");
                                alertDialogBuilder.setMessage("You want to call Merchant (+" + transaksi.getTeleponmerchant() + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+" + transaksi.getTeleponmerchant()));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        chatmerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("senderid", loginUser.getId());
                                intent.putExtra("receiverid", transaksi.getId_merchant());
                                intent.putExtra("tokendriver", loginUser.getToken());
                                intent.putExtra("tokenku", transaksi.getToken_merchant());
                                intent.putExtra("name", transaksi.getNama_merchant());
                                intent.putExtra("pic", Constants.IMAGESMERCHANT + transaksi.getFoto_merchant());
                                startActivity(intent);
                            }
                        });

                    } else if (fitur.equalsIgnoreCase("5")) {
                        requestRoute();
                        lldetailsend.setVisibility(View.VISIBLE);
                        produk.setText(transaksi.getNamaBarang());
                        sendername.setText(transaksi.namaPengirim);
                        receivername.setText(transaksi.namaPenerima);

                        senderphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPengirim));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        receiverphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPenerima));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                    }
                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                    if (pickUpMarker != null) pickUpMarker.remove();
                    pickUpMarker = gMap.addMarker(new MarkerOptions()
                            .position(pickUpLatLng)
                            .title("Pick Up")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));


                    if (destinationMarker != null) destinationMarker.remove();
                    destinationMarker = gMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Destination")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                    updateLastLocation();
                    parsedata(transaksi, pelanggan);


                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<DetailTransResponseJson> call, @NonNull Throwable t) {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
            else
            {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        imageByteArray = baos.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
               /* Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContext().getContentResolver().openInputStream(Objects.requireNonNull(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                consentLetterImage.setImageBitmap(rotatedBitmap);


                */
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                imageByteArray = baos.toByteArray();
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
                consentLetterImage.setImageBitmap(photo);
                btnSubmitConsentLetter.setText("Submit");
            }
        }
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }



    private void shimmerload() {
        shimmerlayanan.startShimmerAnimation();
        shimmerpickup.startShimmerAnimation();
        shimmerdestination.startShimmerAnimation();
        shimmerfitur.startShimmerAnimation();
        shimmerdistance.startShimmerAnimation();
        shimmerprice.startShimmerAnimation();

        layanan.setVisibility(View.GONE);
        layanandesk.setVisibility(View.GONE);
        pickUpText.setVisibility(View.GONE);
        destinationText.setVisibility(View.GONE);
        fiturtext.setVisibility(View.GONE);
        priceText.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        shimmerlayanan.stopShimmerAnimation();
        shimmerpickup.stopShimmerAnimation();
        shimmerdestination.stopShimmerAnimation();
        shimmerfitur.stopShimmerAnimation();
        shimmerdistance.stopShimmerAnimation();
        shimmerprice.stopShimmerAnimation();

        shimmerlayanan.setVisibility(View.GONE);
        shimmerpickup.setVisibility(View.GONE);
        shimmerdestination.setVisibility(View.GONE);
        shimmerfitur.setVisibility(View.GONE);
        shimmerdistance.setVisibility(View.GONE);
        shimmerprice.setVisibility(View.GONE);

        layanan.setVisibility(View.VISIBLE);
        layanandesk.setVisibility(View.VISIBLE);
        pickUpText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        distanceText.setVisibility(View.VISIBLE);
        fiturtext.setVisibility(View.VISIBLE);
        priceText.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void parsedata(TransaksiModel request, final PelangganModel pelanggan) {
        requestRoute();
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        PicassoTrustAll.getInstance(context)
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);


        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (type.equals("4")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga() + totalbiaya), context);
        } else {
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()), context);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Customer");
                alertDialogBuilder.setMessage("You want to call Costumer (+" + pelanggan.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:+" + pelanggan.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", pelanggan.getId());
                intent.putExtra("tokendriver", loginUser.getToken());
                intent.putExtra("tokenku", pelanggan.getToken());
                intent.putExtra("name", pelanggan.getFullnama());
                intent.putExtra("pic", Constants.IMAGESUSER + pelanggan.getFoto());
                startActivity(intent);
            }
        });
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (pickUpLatLng != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    pickUpLatLng, 15f)
            );

            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }

    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
                final String json = Objects.requireNonNull(response.body()).string();
                final long distance = MapDirectionAPI.getDistance(context, json);
                final String time = MapDirectionAPI.getTimeDistance(context, json);
                if (distance >= 0) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLineDestination(json);
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.1f", km);
                            distanceText.setText(format);
                            fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private void requestRoute() {
        if (pickUpLatLng != null && destinationLatLng != null) {
            MapDirectionAPI.getDirection(pickUpLatLng, destinationLatLng).enqueue(updateRouteCallback);
        }
    }

    private void updateLineDestination(String json) {
        Directions directions = new Directions(context);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(context, R.color.colorgradient))
                        .width(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style_json));

            if (!success) {
                android.util.Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            android.util.Log.e("", "Can't find style. Error: ", e);
        }
        getData(idtrans, idpelanggan);
    }

    private void start(final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.startrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        onsubmit = "3";
                        getData(idtrans, idpelanggan);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "3";
                        if (type.equals("4")) {
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-" + idtrans + idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            orderfcm.desc = "driver delivers the order";
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void verify(String verificode, final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        VerifyRequestJson param = new VerifyRequestJson();
        param.setId(loginUser.getNoTelepon());
        param.setIdtrans(idtrans);
        param.setVerifycode(verificode);
        userService.verifycode(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull final Response<ResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        start(pelanggan, tokenmerchant, idtransmerchant, waktuorder);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Toast.makeText(context, "verify code not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void finish(final PelangganModel pelanggan, final String tokenmerchant) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        FinishRequestJson param = new FinishRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        param.setConsent(consentImageName);
        userService.finishrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(context, response.body().getData(), Toast.LENGTH_SHORT).show();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        idJson it = new idJson();
                        it.setId(idpelanggan);
                        userService.deleteBidding2(it).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                    rlprogress.setVisibility(View.GONE);
                                    Intent i = new Intent(context, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    OrderFCM orderfcm = new OrderFCM();
                                    orderfcm.id_driver = loginUser.getId();
                                    orderfcm.id_transaksi = idtrans;
                                    orderfcm.response = "4";
                                    orderfcm.desc = getString(R.string.notification_finish);
                                    if (type.equals("4")) {
                                        sendMessageToDriver(tokenmerchant, orderfcm);
                                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                                    } else {
                                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                                    }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });


                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessageToDriver(final String regIDTujuan, final OrderFCM response) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(response);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                android.util.Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }
}
