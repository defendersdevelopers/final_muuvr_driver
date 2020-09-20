package com.defenders.muuvrdri.utils.api.service;

import com.defenders.muuvrdri.json.AcceptRequestJson;
import com.defenders.muuvrdri.json.AcceptResponseJson;
import com.defenders.muuvrdri.json.BankResponseJson;
import com.defenders.muuvrdri.json.BiddingResponseJson;
import com.defenders.muuvrdri.json.ChangePassRequestJson;
import com.defenders.muuvrdri.json.ConsentRequestJson;
import com.defenders.muuvrdri.json.ConsentResponseJson;
import com.defenders.muuvrdri.json.FinishRequestJson;
import com.defenders.muuvrdri.json.GetOnRequestJson;
import com.defenders.muuvrdri.json.JobResponseJson;
import com.defenders.muuvrdri.json.UpdateLocationRequestJson;
import com.defenders.muuvrdri.json.AllTransResponseJson;
import com.defenders.muuvrdri.json.DetailRequestJson;
import com.defenders.muuvrdri.json.DetailTransResponseJson;
import com.defenders.muuvrdri.json.EditKendaraanRequestJson;
import com.defenders.muuvrdri.json.EditprofileRequestJson;
import com.defenders.muuvrdri.json.GetHomeRequestJson;
import com.defenders.muuvrdri.json.GetHomeResponseJson;
import com.defenders.muuvrdri.json.LoginRequestJson;
import com.defenders.muuvrdri.json.LoginResponseJson;
import com.defenders.muuvrdri.json.PrivacyRequestJson;
import com.defenders.muuvrdri.json.PrivacyResponseJson;
import com.defenders.muuvrdri.json.RegisterRequestJson;
import com.defenders.muuvrdri.json.RegisterResponseJson;
import com.defenders.muuvrdri.json.ResponseJson;
import com.defenders.muuvrdri.json.TopupRequestJson;
import com.defenders.muuvrdri.json.TopupResponseJson;
import com.defenders.muuvrdri.json.VerifyRequestJson;
import com.defenders.muuvrdri.json.WalletRequestJson;
import com.defenders.muuvrdri.json.WalletResponseJson;
import com.defenders.muuvrdri.json.WithdrawRequestJson;
import com.defenders.muuvrdri.json.WithdrawResponseJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public interface DriverService {

    @POST("driver/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("driver/update_location")
    Call<ResponseJson> updatelocation(@Body UpdateLocationRequestJson param);

    @POST("driver/syncronizing_account")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("driver/logout")
    Call<GetHomeResponseJson> logout(@Body GetHomeRequestJson param);

    @POST("driver/turning_on")
    Call<ResponseJson> turnon(@Body GetOnRequestJson param);

    @POST("driver/accept")
    Call<AcceptResponseJson> accept(@Body AcceptRequestJson param);

    @POST("driver/start")
    Call<AcceptResponseJson> startrequest(@Body AcceptRequestJson param);

    @POST("driver/finish")
    Call<AcceptResponseJson> finishrequest(@Body FinishRequestJson param);

    @POST("driver/edit_profile")
    Call<LoginResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("driver/edit_kendaraan")
    Call<LoginResponseJson> editKendaraan(@Body EditKendaraanRequestJson param);

    @POST("driver/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("driver/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("driver/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("driver/register_driver")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);


    @POST("driver/upload_consent")
    Call<ConsentResponseJson> uploadConsent(@Body ConsentRequestJson param);

    @POST("pelanggan/list_bank")
    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);

    @POST("driver/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @POST("driver/job")
    Call<JobResponseJson> job();


    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

    @POST("pelanggan/topupstripe")
    Call<TopupResponseJson> topup(@Body TopupRequestJson param);

    @POST("driver/withdraw")
    Call<WithdrawResponseJson> withdraw(@Body WithdrawRequestJson param);

    @POST("pelanggan/wallet")
    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);

    @POST("driver/topuppaypal")
    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("driver/verifycode")
    Call<ResponseJson> verifycode(@Body VerifyRequestJson param);

    @GET("driver/fetch_biddings")
    Call<BiddingResponseJson> fetchBidding();
}
