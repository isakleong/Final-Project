package com.example.android_skripsi.TaskServer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android_skripsi.Adapter.AdapterBiroUnit;
import com.example.android_skripsi.Adapter.AdapterContentFotoAspirasi;
import com.example.android_skripsi.Adapter.AdapterContentFotoBiroUnit;
import com.example.android_skripsi.Adapter.AdapterContentFotoBiroUnitHistory;
import com.example.android_skripsi.Adapter.AdapterContentFotoResolusi;
import com.example.android_skripsi.Adapter.AdapterContentStatus;
import com.example.android_skripsi.Adapter.AdapterFotoAspirasiEdited;
import com.example.android_skripsi.Adapter.AdapterHistory;
import com.example.android_skripsi.Adapter.AdapterTrending;
import com.example.android_skripsi.DetailBiroUnit;
import com.example.android_skripsi.DetailHistory;
import com.example.android_skripsi.EditAspirasi;
import com.example.android_skripsi.HistoryFragment;
import com.example.android_skripsi.InputAspirasi;
import com.example.android_skripsi.InsertResolusiAspirasi;
import com.example.android_skripsi.Main2Activity;
import com.example.android_skripsi.MainActivity;
import com.example.android_skripsi.ProfileFragment;
import com.example.android_skripsi.Provider.ProviderBiroUnit;
import com.example.android_skripsi.Provider.ProviderFotoAspirasi;
import com.example.android_skripsi.Provider.ProviderFotoBiroUnit;
import com.example.android_skripsi.Provider.ProviderFotoResolusi;
import com.example.android_skripsi.Provider.ProviderHistory;
import com.example.android_skripsi.Provider.ProviderTrending;
import com.example.android_skripsi.R;
import com.example.android_skripsi.SignIn;
import com.example.android_skripsi.SplashScreen;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.android_skripsi.SignIn.idUser;
import static com.example.android_skripsi.SignIn.idUserBiroUnit;
import static com.example.android_skripsi.SignIn.idUserCivitas;

public class TaskServer {
    String callMode;
    Activity activityCaller;
    Context context;

    //data user login
    String id_user, id_status, token_user;

    //data aspirasi
    String idAspirasi, teksAspirasi, tglAspirasi, biroUnit, userCivitas, idFotoAspirasi,
            fotoAspirasi, detailFotoAspirasi, statusAspirasi, modelPredict = "";
    String fotoAspirasiEdit, detailFotoAspirasiEdit;
    Integer anonym;

    //data resolusi aspirasi
    String idResolusiAspirasi, teksResolusiAspirasi, tglResolusiAspirasi, idFotoResolusiAspirasi, fotoResolusiAspirasi,
            detailFotoResolusiAspirasi;

    //data detail status aspirasi
    Integer idStatusAspirasi;
    String namaStatusAspirasi, tglStatusAspirasi;

    //data list biro unit
    String idBiroUnit, namaBiroUnit, emailBiroUnit, ratingBiroUnit, jobDescBiroUnit, fotoBiroUnit, modeFotoBiroUnit;

    //data feedback resolusi
    String teksFeedbackResolusi, tglFeedbackResolusi, ratingFeedbackResolusi, rateEval, detailRateEval;

    //data trending
    String idTrending, teksTrending, biroUnitTrending;

    public String getModeFotoBiroUnit() { return modeFotoBiroUnit; }

    public void setModeFotoBiroUnit(String modeFotoBiroUnit) { this.modeFotoBiroUnit = modeFotoBiroUnit; }

    public String getToken_user() {
        return token_user;
    }

    public void setToken_user(String token_user) {
        this.token_user = token_user;
    }

    public String getIdTrending() {
        return idTrending;
    }

    public void setIdTrending(String idTrending) {
        this.idTrending = idTrending;
    }

    public String getTeksTrending() {
        return teksTrending;
    }

    public void setTeksTrending(String teksTrending) {
        this.teksTrending = teksTrending;
    }

    public String getBiroUnitTrending() {
        return biroUnitTrending;
    }

    public void setBiroUnitTrending(String biroUnitTrending) {
        this.biroUnitTrending = biroUnitTrending;
    }

    public String getIdBiroUnit() {
        return idBiroUnit;
    }

    public void setIdBiroUnit(String idBiroUnit) {
        this.idBiroUnit = idBiroUnit;
    }

    public String getFotoBiroUnit() {
        return fotoBiroUnit;
    }

    public void setFotoBiroUnit(String fotoBiroUnit) {
        this.fotoBiroUnit = fotoBiroUnit;
    }

    public String getFotoAspirasiEdit() {
        return fotoAspirasiEdit;
    }

    public void setFotoAspirasiEdit(String fotoAspirasiEdit) {
        this.fotoAspirasiEdit = fotoAspirasiEdit;
    }

    public String getDetailFotoAspirasiEdit() {
        return detailFotoAspirasiEdit;
    }

    public void setDetailFotoAspirasiEdit(String detailFotoAspirasiEdit) {
        this.detailFotoAspirasiEdit = detailFotoAspirasiEdit;
    }

    public String getDetailRateEval() {
        return detailRateEval;
    }

    public void setDetailRateEval(String detailRateEval) {
        this.detailRateEval = detailRateEval;
    }

    public String getRateEval() {
        return rateEval;
    }

    public void setRateEval(String rateEval) {
        this.rateEval = rateEval;
    }

    public String getTeksFeedbackResolusi() {
        return teksFeedbackResolusi;
    }

    public void setTeksFeedbackResolusi(String teksFeedbackResolusi) {
        this.teksFeedbackResolusi = teksFeedbackResolusi;
    }

    public String getTglFeedbackResolusi() {
        return tglFeedbackResolusi;
    }

    public void setTglFeedbackResolusi(String tglFeedbackResolusi) {
        this.tglFeedbackResolusi = tglFeedbackResolusi;
    }

    public String getRatingFeedbackResolusi() {
        return ratingFeedbackResolusi;
    }

    public void setRatingFeedbackResolusi(String ratingFeedbackResolusi) {
        this.ratingFeedbackResolusi = ratingFeedbackResolusi;
    }

    public String getIdFotoResolusiAspirasi() {
        return idFotoResolusiAspirasi;
    }

    public void setIdFotoResolusiAspirasi(String idFotoResolusiAspirasi) {
        this.idFotoResolusiAspirasi = idFotoResolusiAspirasi;
    }

    public String getIdResolusiAspirasi() {
        return idResolusiAspirasi;
    }

    public void setIdResolusiAspirasi(String idResolusiAspirasi) {
        this.idResolusiAspirasi = idResolusiAspirasi;
    }

    public String getTeksResolusiAspirasi() {
        return teksResolusiAspirasi;
    }

    public void setTeksResolusiAspirasi(String teksResolusiAspirasi) {
        this.teksResolusiAspirasi = teksResolusiAspirasi;
    }

    public String getTglResolusiAspirasi() {
        return tglResolusiAspirasi;
    }

    public void setTglResolusiAspirasi(String tglResolusiAspirasi) {
        this.tglResolusiAspirasi = tglResolusiAspirasi;
    }

    public String getFotoResolusiAspirasi() {
        return fotoResolusiAspirasi;
    }

    public void setFotoResolusiAspirasi(String fotoResolusiAspirasi) {
        this.fotoResolusiAspirasi = fotoResolusiAspirasi;
    }

    public String getDetailFotoResolusiAspirasi() {
        return detailFotoResolusiAspirasi;
    }

    public void setDetailFotoResolusiAspirasi(String detailFotoResolusiAspirasi) {
        this.detailFotoResolusiAspirasi = detailFotoResolusiAspirasi;
    }

    public String getUserCivitas() {
        return userCivitas;
    }

    public void setUserCivitas(String userCivitas) {
        this.userCivitas = userCivitas;
    }

    public String getStatusAspirasi() {
        return statusAspirasi;
    }

    public void setStatusAspirasi(String statusAspirasi) {
        this.statusAspirasi = statusAspirasi;
    }

    public Integer getAnonym() {
        return anonym;
    }

    public void setAnonym(Integer anonym) {
        this.anonym = anonym;
    }

    public String getModelPredict() { return modelPredict; }

    public void setModelPredict(String modelPredict) { this.modelPredict = modelPredict; }

    public String getIdFotoAspirasi() { return idFotoAspirasi; }

    public void setIdFotoAspirasi(String idFotoAspirasi) {
        this.idFotoAspirasi = idFotoAspirasi;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_status() {
        return id_status;
    }

    public void setId_status(String id_status) {
        this.id_status = id_status;
    }

    public String getNamaStatusAspirasi() {
        return namaStatusAspirasi;
    }

    public void setNamaStatusAspirasi(String namaStatusAspirasi) {
        this.namaStatusAspirasi = namaStatusAspirasi;
    }

    public String getIdAspirasi() {
        return idAspirasi;
    }

    public void setIdAspirasi(String idAspirasi) {
        this.idAspirasi = idAspirasi;
    }

    public String getTglStatusAspirasi() {
        return tglStatusAspirasi;
    }

    public void setTglStatusAspirasi(String tglStatusAspirasi) {
        this.tglStatusAspirasi = tglStatusAspirasi;
    }

    public String getNamaBiroUnit() {
        return namaBiroUnit;
    }

    public void setNamaBiroUnit(String namaBiroUnit) {
        this.namaBiroUnit = namaBiroUnit;
    }

    public String getEmailBiroUnit() {
        return emailBiroUnit;
    }

    public void setEmailBiroUnit(String emailBiroUnit) {
        this.emailBiroUnit = emailBiroUnit;
    }

    public String getRatingBiroUnit() {
        return ratingBiroUnit;
    }

    public void setRatingBiroUnit(String ratingBiroUnit) {
        this.ratingBiroUnit = ratingBiroUnit;
    }

    public String getJobDescBiroUnit() {
        return jobDescBiroUnit;
    }

    public void setJobDescBiroUnit(String jobDescBiroUnit) {
        this.jobDescBiroUnit = jobDescBiroUnit;
    }

    public Integer getIdStatusAspirasi() {
        return idStatusAspirasi;
    }

    public void setIdStatusAspirasi(Integer idStatusAspirasi) {
        this.idStatusAspirasi = idStatusAspirasi;
    }

    public String getTglAspirasi() {
        return tglAspirasi;
    }

    public void setTglAspirasi(String tglAspirasi) {
        this.tglAspirasi = tglAspirasi;
    }

    public String getTeksAspirasi() {
        return teksAspirasi;
    }

    public void setTeksAspirasi(String teksAspirasi) {
        this.teksAspirasi = teksAspirasi;
    }

    public String getBiroUnit() {
        return biroUnit;
    }

    public void setBiroUnit(String biroUnit) {
        this.biroUnit = biroUnit;
    }

    public String getFotoAspirasi() {
        return fotoAspirasi;
    }

    public void setFotoAspirasi(String fotoAspirasi) {
        this.fotoAspirasi = fotoAspirasi;
    }

    public String getDetailFotoAspirasi() {
        return detailFotoAspirasi;
    }

    public void setDetailFotoAspirasi(String detailFotoAspirasi) {
        this.detailFotoAspirasi = detailFotoAspirasi;
    }

    public String getCallMode() {
        return callMode;
    }

    public void setCallMode(String mode) {
        this.callMode = mode;
    }

    public Activity getActivityCaller() {
        return activityCaller;
    }

    public void setActivityCaller(Activity activityCaller) {
        this.activityCaller = activityCaller;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    ShimmerFrameLayout mShimmerViewContainer;

    public String callApi(){
        final JsonObjectRequest request;
        final String ipAddress = "http://opensource.petra.ac.id/~m26416103/skripsi/";
        if(callMode.equals("userLogin")){
            String apiURL = ipAddress+"users/login.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String profile_picture="";
                            if(status_code.equals("200")){
                                JSONArray jsonArray = responseObject.getJSONArray("result");
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject resultObject = jsonArray.getJSONObject(i);
                                    if(SignIn.signInMode == 0){
                                        idUserCivitas = resultObject.getInt("id_user_civitas");
                                        SignIn.idUser = idUserCivitas;
                                        SignIn.nama_lengkap = resultObject.getString("nama_lengkap");
                                        SignIn.nrp = resultObject.getString("nrp");
                                        SignIn.gender = resultObject.getString("gender");
                                        SignIn.tgl_lahir = resultObject.getString("tgl_lahir");
                                        SignIn.tempat_lahir = resultObject.getString("tempat_lahir");
                                        SignIn.alamat = resultObject.getString("alamat");
                                        profile_picture = resultObject.getString("profile_picture");

                                        String arrProfilePicture[]   = profile_picture.split(",");

                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrProfilePicture[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        SignIn.profilePictureByteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(SignIn.profilePictureByteArray, 0, SignIn.profilePictureByteArray.length);
                                        SignIn.profilePictureBitmap = bmp;

                                    }
                                    else{
                                        idUserBiroUnit = resultObject.getInt("id_user_biro_unit");
                                        SignIn.idUser = idUserBiroUnit;
                                    }

                                }

                                SharedPreferences preferences = getActivityCaller().
                                        getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isUserLogin", true);
                                editor.putInt("idUser", SignIn.idUser);
                                editor.putString("nama_lengkap", SignIn.nama_lengkap);
                                editor.putString("nrp", SignIn.nrp);
                                editor.putString("tempat_lahir", SignIn.tempat_lahir);
                                editor.putString("tgl_lahir", SignIn.tgl_lahir);
                                editor.putString("alamat", SignIn.alamat);
                                editor.putInt("sign_in_mode", SignIn.signInMode);

                                editor.commit();
//
                                TaskServer taskServer = new TaskServer();
                                taskServer.setCallMode("updateToken");
                                taskServer.setActivityCaller(getActivityCaller());
                                taskServer.setToken_user(getToken_user());
                                taskServer.setId_user(idUser+"");
                                taskServer.callApi();

                            }
                            else{
                                Toast.makeText(activityCaller, "Login failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", SignIn.emailSignIn);
                        params.put("password", SignIn.passwordSignIn);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get profile picture
        else if(callMode.equals("getProfilePicture")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/users/profile_picture.php")
                    .appendQueryParameter("id_user_civitas", idUser+"");
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            //data foto aspirasi found
                            if(status_code.equals("200")){

                                String profile_picture="";
                                profile_picture = responseObject.getString("result");

                                String arrProfilePicture[]   = profile_picture.split(",");

                                //get result from encoded_image
                                byte[] decodedString = Base64.decode(arrProfilePicture[1], Base64.DEFAULT);
                                Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                byte[] byteArray = stream.toByteArray();
                                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                ProfileFragment.imgProfilePicture.setImageBitmap(SignIn.profilePictureBitmap);
                            }

                            //data profile picture not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        //update token fcm
        else if(callMode.equals("updateToken")){
            String apiURL = ipAddress+"users/token.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("201")){
                                Toast.makeText(activityCaller, "Login success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Login failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();
                        Log.e("INI TOKEN DAPAT ", getToken_user());
                        params.put("token_fcm", getToken_user());
                        params.put("id_user", idUser+"");
                        params.put("user_class", SignIn.signInMode+"");

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get model predicted
        else if(callMode.equals("getModelPredict")){
            final String ipLocal = "http://192.168.5.10:8088/skripsi/";
            String apiURL = ipLocal+"model/call.php";

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String model_predict = responseObject.getString("result");
                            if(status_code.equals("201")){
                                //insert aspirasi
                                TaskServer taskServer = new TaskServer();
                                taskServer.setCallMode("insertAspirasi");
                                taskServer.setActivityCaller(getActivityCaller());
                                taskServer.setTeksAspirasi(getTeksAspirasi());
                                taskServer.setBiroUnit(getBiroUnit());
                                taskServer.setAnonym(getAnonym());
                                taskServer.setModelPredict(model_predict);
                                taskServer.callApi();
                            }
                            else{
                                Toast.makeText(activityCaller, "Submit data aspirasi failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("teks_aspirasi", teksAspirasi);

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert aspirasi
        else if(callMode.equals("insertAspirasi")){
            String apiURL = ipAddress+"aspirasi/aspirasi.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String id_aspirasi = responseObject.getString("result");
                            if(status_code.equals("201")){
                                //check whether aspirasi have an image publication
                                if(InputAspirasi.hasilImageRemarks.size()>0){
                                    Log.e("INI SIZE ", InputAspirasi.hasilImageRemarks.size()+"");

                                    for(int i=0;i<InputAspirasi.hasilImageRemarks.size();i++){
                                        String fotoAspirasiGet = "";
                                        String detailFotoAspirasiGet = "";
                                        String[] parts = InputAspirasi.hasilImageRemarks.get(i).split("---");

                                        fotoAspirasiGet+="data:image/jpeg;base64,";
                                        fotoAspirasiGet+=parts[0];
                                        detailFotoAspirasiGet+=parts[1];

                                        TaskServer taskServer = new TaskServer();
                                        taskServer.setActivityCaller(getActivityCaller());
                                        taskServer.setCallMode("insertFotoAspirasi");
                                        taskServer.setFotoAspirasi(fotoAspirasiGet);
                                        taskServer.setDetailFotoAspirasi(detailFotoAspirasiGet);
                                        taskServer.callApi();
                                    }
                                }

                                //insert detail status aspirasi
                                TaskServer taskServer2 = new TaskServer();
                                taskServer2.setCallMode("insertDetailStatusAspirasi");
                                taskServer2.setActivityCaller(activityCaller);
                                //1 == aspirasi baru
                                taskServer2.setIdStatusAspirasi(1);
                                taskServer2.setTglStatusAspirasi(getTglAspirasi());
                                taskServer2.callApi();

                                InputAspirasi.animateView(InputAspirasi.progressOverlay, View.GONE, 0, 200);
                                Toast.makeText(activityCaller, "Submit data aspirasi success", Toast.LENGTH_SHORT).show();
                                //input data aspirasi completed
                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Submit data aspirasi failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        setTglAspirasi(currentDateandTime);

                        params.put("teks_aspirasi", teksAspirasi);
                        params.put("tgl_aspirasi", getTglAspirasi());
                        if(getBiroUnit().equals("None (Classified by System)")){
                            params.put("id_user_biro_unit", "");
                        }
                        else{
                            params.put("id_user_biro_unit", getBiroUnit());
                        }
                        params.put("model_predict", getModelPredict());
                        params.put("id_user_civitas", SignIn.idUser+"");
                        params.put("anonim", getAnonym()+"");

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert resolusi aspirasi
        else if(callMode.equals("insertResolusiAspirasi")){
            String apiURL = ipAddress+"resolusi_aspirasi/insert.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String id_aspirasi = responseObject.getString("result");
                            if(status_code.equals("201")){
                                //check whether aspirasi have an image publication
                                if(InsertResolusiAspirasi.hasilImageRemarksResolusi.size()>0){
                                    for(int i=0;i<InsertResolusiAspirasi.hasilImageRemarksResolusi.size();i++){
                                        String fotoResolusi = "";
                                        String detailFotoResolusi = "";
                                        String[] parts = InsertResolusiAspirasi.hasilImageRemarksResolusi.
                                                get(i).split("---");

                                        fotoResolusi+="data:image/jpeg;base64,";
                                        fotoResolusi+=parts[0];
                                        detailFotoResolusi+=parts[1];

                                        TaskServer taskServer = new TaskServer();
                                        taskServer.setActivityCaller(getActivityCaller());
                                        taskServer.setCallMode("insertFotoResolusiAspirasi");
                                        taskServer.setFotoResolusiAspirasi(fotoResolusi);
                                        taskServer.setDetailFotoResolusiAspirasi(detailFotoResolusi);
                                        taskServer.callApi();
                                    }
                                }

                                //insert detail status aspirasi
                                TaskServer taskServer = new TaskServer();
                                taskServer.setCallMode("insertDetailStatusAspirasi");
                                taskServer.setActivityCaller(activityCaller);
                                //5 == resolusi aspirasi telah dikirim oleh biro unit kepada user civitas
                                taskServer.setIdStatusAspirasi(5);
                                taskServer.setTglStatusAspirasi(getTglResolusiAspirasi());
                                taskServer.callApi();

                                Toast.makeText(activityCaller, "Submit data resolusi aspirasi success", Toast.LENGTH_SHORT).show();
                                //input data aspirasi completed
                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Submit data resolusi aspirasi failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        setTglResolusiAspirasi(currentDateandTime);

                        params.put("teks_resolusi", teksResolusiAspirasi);
                        params.put("tgl_resolusi", getTglResolusiAspirasi());
                        params.put("id_aspirasi", idAspirasi);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get feedback resolusi
        else if(callMode.equals("getFeedbackResolusiAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/feedback_resolusi/feedback_resolusi.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");

                            //data detail resolusi found
                            if(status_code.equals("200")){
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final TextView contentTeksFeedbackResolusi = (TextView)
                                        getActivityCaller().findViewById(R.id.contentTeksFeedbackResolusi);
                                final TextView contentTglFeedbackResolusi = (TextView)
                                        getActivityCaller().findViewById(R.id.contentTglFeedbackResolusi);
                                final RatingBar ratingBar = (RatingBar)
                                        getActivityCaller().findViewById(R.id.contentRatingResolusi);
                                final TextView contentDetailRating = (TextView)
                                        getActivityCaller().findViewById(R.id.contentDetailRating);
                                final TextView headerTeksFeedbackResolusi = (TextView)
                                        getActivityCaller().findViewById(R.id.headerTeksFeedbackResolusi);
                                final View dividerRating = (View)
                                        getActivityCaller().findViewById(R.id.dividerRating);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idResolusiAspirasi = resultObject.getString("id_resolusi_aspirasi");
                                        teksFeedbackResolusi = resultObject.getString("teks_feedback");
                                        tglFeedbackResolusi = resultObject.getString("tgl_feedback");
                                        ratingFeedbackResolusi = resultObject.getString("rating");
                                        detailRateEval = resultObject.getString("detail_rating");

                                        if(teksFeedbackResolusi.equals("")){
                                            headerTeksFeedbackResolusi.setVisibility(View.GONE);
                                            contentTeksFeedbackResolusi.setVisibility(View.GONE);
                                        }
                                        else{
                                            contentTeksFeedbackResolusi.setText(teksFeedbackResolusi);
                                        }

                                        if(detailRateEval.equals("")){
                                            dividerRating.setVisibility(View.GONE);
                                            contentDetailRating.setVisibility(View.GONE);
                                        }
                                        else{
                                            String arrDetRate[] = detailRateEval.split(",");
                                            String detRate = "";

                                            for(int j=0;j<arrDetRate.length;j++){
                                                if(j==arrDetRate.length-1){
                                                    detRate+=arrDetRate[j];
                                                }
                                                else{
                                                    detRate+=arrDetRate[j];
                                                    detRate+=", ";
                                                }
                                            }
                                            contentDetailRating.setText(detRate);
                                        }

                                        contentTglFeedbackResolusi.setText("("+tglFeedbackResolusi+")");
                                        ratingBar.setRating(Float.parseFloat(ratingFeedbackResolusi));

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        //insert feedback resolusi
        else if(callMode.equals("insertFeedbackResolusi")){
            String apiURL = ipAddress+"feedback_resolusi/feedback_resolusi.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");

                            if(status_code.equals("201")){
                                //insert detail status aspirasi
                                TaskServer taskServer = new TaskServer();
                                taskServer.setCallMode("insertDetailStatusAspirasi");
                                taskServer.setActivityCaller(activityCaller);
                                //6 == feedback resolusi aspirasi telah dikirim oleh user civitas
                                taskServer.setIdStatusAspirasi(6);
                                taskServer.setTglStatusAspirasi(getTglFeedbackResolusi());
                                taskServer.callApi();

                                Toast.makeText(activityCaller, "Submit data review resolusi success", Toast.LENGTH_SHORT).show();
                                //input data aspirasi completed
                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Submit data review resolusi failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        setTglFeedbackResolusi(currentDateandTime);

                        params.put("teks_feedback", teksFeedbackResolusi);
                        params.put("tgl_feedback", getTglFeedbackResolusi());
                        params.put("rating", ratingFeedbackResolusi);
                        params.put("detail_rating", detailRateEval);
                        params.put("id_resolusi_aspirasi", idResolusiAspirasi);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //delete aspirasi
        else if(callMode.equals("deleteAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/delete.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi)
                    .appendQueryParameter("id_user_civitas", idUser+"");
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("204")){
                                //delete existing foto aspirasi success
                                Toast.makeText(activityCaller, "Delete aspirasi berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Delete foto aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //delete foto aspirasi
        else if(callMode.equals("deleteFotoAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/images/delete.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi)
                    .appendQueryParameter("id_foto_aspirasi", idFotoAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("204")){
                                //delete existing foto aspirasi success
                            }
                            else{
                                Toast.makeText(activityCaller, "Delete foto aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //edit foto aspirasi
        else if(callMode.equals("editFotoAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/images/update.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi)
                    .appendQueryParameter("id_foto_aspirasi", idFotoAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("204")){
                                //update existing foto aspirasi success
                            }
                            else{
                                Toast.makeText(activityCaller, "Update foto aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("foto_aspirasi", fotoAspirasiEdit);
                        params.put("detail_foto", detailFotoAspirasiEdit);

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert foto resolusi aspirasi
        else if(callMode.equals("insertFotoResolusiAspirasi")){
            String apiURL = ipAddress+"resolusi_aspirasi/images/images.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String id_aspirasi = responseObject.getString("result");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("foto_resolusi", fotoResolusiAspirasi);
                        params.put("detail_foto", detailFotoResolusiAspirasi);

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert foto aspirasi
        else if(callMode.equals("insertFotoAspirasi")){
            Log.e("TRACKING ", "MASUK");
            String apiURL = ipAddress+"aspirasi/images/images.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String id_aspirasi = responseObject.getString("result");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
//                        setTglAspirasi(currentDateandTime);

                        params.put("foto_aspirasi", fotoAspirasi);
                        params.put("detail_foto", detailFotoAspirasi);

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert foto aspirasi edited
        else if(callMode.equals("insertFotoAspirasiEdited")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/images/update.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("201")){
                                //update foto aspirasi success
                            }
                            else{
                                Toast.makeText(activityCaller, "Update foto aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("foto_aspirasi", fotoAspirasi);
                        params.put("detail_foto", detailFotoAspirasi);

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get data detail resolusi
        else if(callMode.equals("getDetailResolusi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/resolusi_aspirasi/detail.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");

                            //data detail resolusi found
                            if(status_code.equals("200")){
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final TextView contentTeksResolusi = (TextView)
                                        getActivityCaller().findViewById(R.id.contentTeksResolusi);
                                final TextView contentTglResolusi = (TextView)
                                        getActivityCaller().findViewById(R.id.contentTglResolusi);
                                final CardView cardView = (CardView)
                                        getActivityCaller().findViewById(R.id.cardViewDataResolusi);
                                cardView.setVisibility(View.VISIBLE);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idResolusiAspirasi = resultObject.getString("id_resolusi_aspirasi");
                                        teksResolusiAspirasi = resultObject.getString("teks_resolusi");
                                        tglResolusiAspirasi = resultObject.getString("tgl_resolusi");

                                        DetailHistory.idResolusiAspirasi = idResolusiAspirasi;

                                        contentTeksResolusi.setText(teksResolusiAspirasi);
                                        contentTglResolusi.setText("Waktu resolusi: "+tglResolusiAspirasi);

                                        //get foto resolusi aspirasi
                                        TaskServer taskServer = new TaskServer();
                                        taskServer.setCallMode("getFotoResolusiAspirasi");
                                        taskServer.setActivityCaller(getActivityCaller());
                                        taskServer.setIdResolusiAspirasi(idResolusiAspirasi);
                                        taskServer.callApi();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get foto resolusi
        else if(callMode.equals("getFotoResolusiAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/resolusi_aspirasi/images/images.php")
                    .appendQueryParameter("id_resolusi_aspirasi", idResolusiAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_foto_resolusi);
                mShimmerViewContainer.startShimmerAnimation();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            //data foto aspirasi found
                            if(status_code.equals("200")){
                                ArrayList<ProviderFotoResolusi> arrProvFotoResolusi = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final RecyclerView rvContentFotoResolusi = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvContentFotoResolusi);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvContentFotoResolusi.setLayoutManager(mLinearLayoutManager);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idFotoResolusiAspirasi = resultObject.getString("id_foto_resolusi");
                                        fotoResolusiAspirasi = resultObject.getString("foto_resolusi");
                                        detailFotoResolusiAspirasi = resultObject.getString("detail_foto");
                                        idResolusiAspirasi = resultObject.getString("id_resolusi_aspirasi");

                                        DetailHistory.idResolusiAspirasi = idResolusiAspirasi;

                                        //decode image
                                        String arrFotoAspirasi[]   = fotoResolusiAspirasi.split(",");
//                                        Toast.makeText(activityCaller, arrFotoAspirasi[0], Toast.LENGTH_LONG).show();

////                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrFotoAspirasi[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        ProviderFotoResolusi providerFotoResolusi = new ProviderFotoResolusi();
                                        providerFotoResolusi.setImageBitmapResolusi(bmp);
                                        providerFotoResolusi.setImageRemarksResolusi(detailFotoResolusiAspirasi);
                                        arrProvFotoResolusi.add(providerFotoResolusi);

                                    }
                                    AdapterContentFotoResolusi adpContentResolusi =
                                            new AdapterContentFotoResolusi(arrProvFotoResolusi);
                                    rvContentFotoResolusi.setAdapter(adpContentResolusi);
                                }

                            }

                            //data history not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get foto aspirasi
        else if(callMode.equals("getFotoAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/images/images.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);
                mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_list_vendor);
                mShimmerViewContainer.startShimmerAnimation();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");

                            final RecyclerView rvContentFotoAspirasi = (RecyclerView)
                                    getActivityCaller().findViewById(R.id.rvContentFotoAspirasi);
                            GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                    1);
                            rvContentFotoAspirasi.setLayoutManager(mLinearLayoutManager);

                            //data foto aspirasi found
                            if(status_code.equals("200")){
                                ArrayList<ProviderFotoAspirasi> arrProvFotoAspirasi = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idFotoAspirasi = resultObject.getString("id_foto_aspirasi");
                                        fotoAspirasi = resultObject.getString("foto_aspirasi");
                                        detailFotoAspirasi = resultObject.getString("detail_foto");

                                        //decode image
                                        String arrFotoAspirasi[]   = fotoAspirasi.split(",");

                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrFotoAspirasi[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        ProviderFotoAspirasi providerFotoAspirasi = new ProviderFotoAspirasi();
                                        providerFotoAspirasi.setImageBitmap(bmp);
                                        providerFotoAspirasi.setImageRemarks(detailFotoAspirasi);
                                        arrProvFotoAspirasi.add(providerFotoAspirasi);

                                        //add remarks data (before changed to temp variable)
                                        EditAspirasi.mapImageBefore.put(idFotoAspirasi, detailFotoAspirasi);
                                    }
                                    AdapterContentFotoAspirasi adpContentFotoAspirasi =
                                            new AdapterContentFotoAspirasi(arrProvFotoAspirasi);
                                    rvContentFotoAspirasi.setAdapter(adpContentFotoAspirasi);
                                }


                            }
                            //data foto aspirasi not found
                            else{
                                TextView headerFotoAspirasi = (TextView) activityCaller.findViewById(R.id.headerFotoAspirasi);
                                headerFotoAspirasi.setVisibility(View.GONE);
                                rvContentFotoAspirasi.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get foto aspriasi edited
        else if(callMode.equals("getFotoAspirasiEdited")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/images/images.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            //data foto aspirasi found
                            if(status_code.equals("200")){
                                ArrayList<ProviderFotoAspirasi> arrProvFotoAspirasi = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final RecyclerView rvImageAspirasiEdited = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvImageAspirasiEdited);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvImageAspirasiEdited.setLayoutManager(mLinearLayoutManager);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idFotoAspirasi = resultObject.getString("id_foto_aspirasi");
                                        fotoAspirasi = resultObject.getString("foto_aspirasi");
                                        detailFotoAspirasi = resultObject.getString("detail_foto");

                                        //decode image
                                        String arrFotoAspirasi[]   = fotoAspirasi.split(",");
//                                        Toast.makeText(activityCaller, arrFotoAspirasi[0], Toast.LENGTH_LONG).show();

////                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrFotoAspirasi[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        ProviderFotoAspirasi providerFotoAspirasi = new ProviderFotoAspirasi();
                                        providerFotoAspirasi.setIdImageRemarks(idFotoAspirasi);
                                        providerFotoAspirasi.setImageBitmap(bmp);
                                        providerFotoAspirasi.setImageRemarks(detailFotoAspirasi);
                                        arrProvFotoAspirasi.add(providerFotoAspirasi);
                                    }
                                    AdapterFotoAspirasiEdited adpFotoAspirasiEdited =
                                            new AdapterFotoAspirasiEdited(arrProvFotoAspirasi);
                                    rvImageAspirasiEdited.setAdapter(adpFotoAspirasiEdited);

                                    EditAspirasi.arrProvImgRemark = arrProvFotoAspirasi;
                                    EditAspirasi.adpImg = adpFotoAspirasiEdited;
                                }


                            }

                            //data history not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //edit data aspirasi
        else if(callMode.equals("editAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/update.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi)
                    .appendQueryParameter("id_user_civitas", idUser+"");
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("204")){
                                //check if an image aspirasi was deleted
                                if(EditAspirasi.idImageDeleted.size()>0){
                                    for(int i=0;i<EditAspirasi.idImageDeleted.size();i++){
                                        TaskServer taskServer = new TaskServer();
                                        taskServer.setActivityCaller(getActivityCaller());
                                        taskServer.setCallMode("deleteFotoAspirasi");
                                        taskServer.setIdFotoAspirasi(EditAspirasi.idImageDeleted.get(i));
                                        taskServer.setIdAspirasi(idAspirasi);
                                        taskServer.callApi();
                                    }
                                }

                                //check content image aspirasi edited
                                if(EditAspirasi.hasilImageRemarksEdited.size()>0){
                                    for(int i=0;i<EditAspirasi.hasilImageRemarksEdited.size();i++){
                                        String fotoAspirasiEditV = "";
                                        String detailFotoAspirasiEditV = "";
                                        String idFoto = "";
                                        String[] parts = EditAspirasi.hasilImageRemarksEdited.get(i).split("---");

                                        fotoAspirasiEditV+="data:image/jpeg;base64,";
                                        fotoAspirasiEditV+=parts[0];
                                        detailFotoAspirasiEditV=parts[1];
                                        idFoto=parts[2];

                                        //edit existing image aspirasi
                                        if(!idFoto.equals("0")){
                                            TaskServer taskServer = new TaskServer();
                                            taskServer.setActivityCaller(getActivityCaller());
                                            taskServer.setCallMode("editFotoAspirasi");
                                            taskServer.setFotoAspirasiEdit(fotoAspirasiEditV);
                                            taskServer.setDetailFotoAspirasiEdit(detailFotoAspirasiEditV);
                                            taskServer.setIdFotoAspirasi(idFoto);
                                            taskServer.setIdAspirasi(idAspirasi);
                                            taskServer.callApi();
                                        }
                                        //insert new image aspirasi
                                        else if(idFoto.equals("0")){
                                            TaskServer taskServer = new TaskServer();
                                            taskServer.setActivityCaller(getActivityCaller());
                                            taskServer.setCallMode("insertFotoAspirasiEdited");
                                            taskServer.setFotoAspirasi(fotoAspirasiEditV);
                                            taskServer.setDetailFotoAspirasi(detailFotoAspirasiEditV);
                                            taskServer.setIdAspirasi(idAspirasi);
                                            taskServer.callApi();
                                        }
                                    }
                                    Toast.makeText(activityCaller, "Update aspirasi berhasil", Toast.LENGTH_SHORT).show();
                                }
                                //if aspirasi don't have image
                                else{
                                    Toast.makeText(activityCaller, "Update aspirasi berhasil", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Update aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        setTglAspirasi(currentDateandTime);

                        params.put("teks_aspirasi", teksAspirasi);
                        params.put("tgl_aspirasi", getTglAspirasi());

                        if(getBiroUnit().equals("None (Classified by System)")){
                            params.put("id_user_biro_unit", "");
                        }
                        else{
                            params.put("id_user_biro_unit", getBiroUnit());
                        }

                        params.put("anonim", getAnonym()+"");

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //insert detail status aspirasi
        else if(callMode.equals("insertDetailStatusAspirasi")){
            String apiURL = ipAddress+"detail_status_aspirasi/detail_status_aspirasi.php";
            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            String id_aspirasi = responseObject.getString("result");

                            //insert data detail status aspirasi success
//                            if(status_code.equals("201")){
//                            }
//                            else{
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("tgl_status", getTglStatusAspirasi());
                        params.put("id_status_aspirasi", idStatusAspirasi+"");

                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //update status aspirasi (0,1,2)
        else if(callMode.equals("updateStatusAspirasi")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/status/update.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            if(status_code.equals("204")){
                                // update status aspirasi success

                                Intent intent = new Intent(activityCaller, MainActivity.class);
                                activityCaller.startActivity(intent);
                            }
                            else{
                                Toast.makeText(activityCaller, "Update aspirasi gagal", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("id_aspirasi", idAspirasi);
                        params.put("status_aspirasi", statusAspirasi);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        //get tracking status
        else if(callMode.equals("getStatusTracking")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/aspirasi/track.php")
                    .appendQueryParameter("id_aspirasi", idAspirasi);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");
                            //data foto aspirasi found
                            if(status_code.equals("200")){
                                ArrayList<ProviderHistory> arrProvHistory = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final RecyclerView rvContentStatus = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvContentStatus);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvContentStatus.setLayoutManager(mLinearLayoutManager);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        namaStatusAspirasi = resultObject.getString("nama_status");
                                        tglStatusAspirasi = resultObject.getString("tgl_status");

                                        ProviderHistory providerHistory = new ProviderHistory();
                                        providerHistory.setNamaStatusAspirasi(namaStatusAspirasi);
                                        providerHistory.setTglStatusAspirasi("Waktu Status: "+tglStatusAspirasi);

                                        arrProvHistory.add(providerHistory);
                                    }
                                    AdapterContentStatus adpContentStatus =
                                            new AdapterContentStatus(arrProvHistory);
                                    rvContentStatus.setAdapter(adpContentStatus);
                                }
                            }

                            //data history not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get detail notification
        else if(callMode.equals("getDetailNotification")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();

            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/detail_status_aspirasi/getDetailNotif.php")
                    .appendQueryParameter("id_user_civitas", id_user)
                    .appendQueryParameter("id_aspirasi", getIdAspirasi())
                    .appendQueryParameter("status_aspirasi", getStatusAspirasi());

            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

//                mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_see_history);
//                mShimmerViewContainer.startShimmerAnimation();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            //data history found
                            if(status_code.equals("200")){
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");
                                final Bundle b = new Bundle();

                                if(jsonArray.length()!=0){

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProviderHistory providerHistory = new ProviderHistory();

                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idAspirasi = resultObject.getString("id_aspirasi");
                                        tglAspirasi = resultObject.getString("tgl_aspirasi");
                                        teksAspirasi = resultObject.getString("teks_aspirasi");
                                        idStatusAspirasi = Integer.parseInt(
                                                resultObject.getString("id_status_aspirasi"));
                                        anonym = Integer.parseInt(resultObject.getString("anonym"));

                                        if(SignIn.signInMode == 0){
                                            biroUnit = resultObject.getString("nama_biro_unit");
                                            fotoBiroUnit = resultObject.getString("profile_picture");
                                            providerHistory.setNamaBiroUnit(biroUnit);
                                            providerHistory.setFotoBiroUnit(fotoBiroUnit);

                                            String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                            //get result from encoded_image
                                            byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                            Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                            providerHistory.setImageBitmap(bmp);
                                            providerHistory.setFotoBiroUnitbytearray(byteArray);
                                        }

                                        providerHistory.setIdAspirasi(Integer.parseInt(idAspirasi));
                                        providerHistory.setTeksAspirasi(teksAspirasi);
                                        providerHistory.setTglAspirasi(tglAspirasi);
                                        providerHistory.setAnonym(anonym);
                                        providerHistory.setIdStatusAspirasi(idStatusAspirasi);

                                        b.putInt("idAspirasi", providerHistory.getIdAspirasi());
                                        b.putInt("idStatusAspirasi", providerHistory.getIdStatusAspirasi());
                                        b.putString("contentTeksAspirasi", providerHistory.getTeksAspirasi());
                                        b.putString("contentTglAspirasi", providerHistory.getTglAspirasi());

                                        b.putByteArray("contentFotoBiroUnit", providerHistory.getFotoBiroUnitbytearray());
                                        b.putString("contentNamaBiroUnit", providerHistory.getNamaBiroUnit());
                                        b.putInt("anonym", providerHistory.getAnonym());

//                                        Intent intent = new Intent(getActivityCaller(), DetailHistory.class);
//                                        intent.putExtras(b);
//                                        activityCaller.startActivity(intent);
//                                        activityCaller.finish();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getActivityCaller(), DetailHistory.class);
                                                intent.putExtras(b);
                                                activityCaller.startActivity(intent);
                                                activityCaller.finish();
                                                            }
                                        }, 2500);
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get history -- not processed
        else if(callMode.equals("getNotProcessHistory")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();

            //user civitas
            if(SignIn.signInMode == 0){
                setId_user(idUser+"");
                setId_status(0+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_civitas", idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            //user biro unit
            else if(SignIn.signInMode == 1){
                setId_user(id_user=idUserBiroUnit+"");
                setId_status(3+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_biro_unit", SignIn.idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        HistoryFragment.animateView(HistoryFragment.MainprogressOverlay, View.GONE, 0, 200);
                        HistoryFragment.progressBarShowHistory.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            //data history found
                            if(status_code.equals("200")){
                                RecyclerView history = (RecyclerView)activityCaller.findViewById(R.id.rvHistory);
                                TextView txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                TextView txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                ImageView imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                ArrayList<ProviderHistory> arrProvHistory = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("result");

                                if(jsonArray.length()!=0){
                                    imgView.setImageResource(0);
                                    txtKet.setVisibility(View.INVISIBLE);
                                    txtOops.setVisibility(View.INVISIBLE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProviderHistory providerHistory = new ProviderHistory();

                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idAspirasi = resultObject.getString("id_aspirasi");
                                        tglAspirasi = resultObject.getString("tgl_aspirasi");
                                        teksAspirasi = resultObject.getString("teks_aspirasi");
                                        idStatusAspirasi = Integer.parseInt(
                                                resultObject.getString("id_status_aspirasi"));
                                        anonym = Integer.parseInt(resultObject.getString("anonym"));
                                        String id_biro_unit = resultObject.getString("id_user_biro_unit");


                                        if(SignIn.signInMode == 0){
                                            biroUnit = resultObject.getString("nama_biro_unit");
                                            fotoBiroUnit = resultObject.getString("profile_picture");

                                            if(fotoBiroUnit!=null && !fotoBiroUnit.isEmpty()){
                                                providerHistory.setNamaBiroUnit(biroUnit);
                                                providerHistory.setFotoBiroUnit(fotoBiroUnit);

                                                String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                                //get result from encoded_image
                                                byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                                Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                                byte[] byteArray = stream.toByteArray();
                                                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                                providerHistory.setImageBitmap(bmp);
                                                providerHistory.setFotoBiroUnitbytearray(byteArray);

                                                providerHistory.setIdBiroUnit(Integer.parseInt(id_biro_unit));

                                            }
                                            else{
                                                providerHistory.setNamaBiroUnit(null);
                                                providerHistory.setFotoBiroUnit(null);
                                                providerHistory.setImageBitmap(null);
                                                providerHistory.setFotoBiroUnitbytearray(null);
                                                providerHistory.setIdBiroUnit(null);
                                            }
                                        }
                                        else if(SignIn.signInMode == 1){
                                            userCivitas = resultObject.getString("nama_lengkap");
                                            providerHistory.setNamaUserCivitas(userCivitas);
                                        }

                                        providerHistory.setIdAspirasi(Integer.parseInt(idAspirasi));
                                        providerHistory.setTeksAspirasi(teksAspirasi);
                                        providerHistory.setTglAspirasi(tglAspirasi);
                                        providerHistory.setAnonym(anonym);
                                        providerHistory.setIdStatusAspirasi(idStatusAspirasi);

                                        arrProvHistory.add(providerHistory);
                                    }

                                    AdapterHistory adp = new AdapterHistory(arrProvHistory);
                                    history.setAdapter(adp);
                                }
                            }
                            //data history not found
                            else{
                                TextView txtKet, txtOops;
                                ImageView imgView;
                                RecyclerView rvHistory;
                                rvHistory   = (RecyclerView) activityCaller.findViewById(R.id.rvHistory);
                                txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                //check user class
                                if(SignIn.signInMode==1){
                                    txtOops.setText("Stay Tune!");
                                    txtKet.setText("Riwayat aspirasi masih kosong\nNantikan aspirasi dari civitas akademika");
                                }

                                ArrayList<ProviderHistory> arrProvHist = new ArrayList<>();
                                AdapterHistory adpHistory = new AdapterHistory(arrProvHist);
                                rvHistory.setAdapter(adpHistory);
                                imgView.setImageResource(R.drawable.empty);
                                txtKet.setVisibility(View.VISIBLE);
                                txtOops.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get history -- in process
        else if(callMode.equals("getInProcessHistory")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();

            //user civitas
            if(SignIn.signInMode == 0){
                setId_user(idUser+"");
                setId_status(0+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_civitas", SignIn.idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            //user biro unit
            else if(SignIn.signInMode == 1){
                setId_user(id_user=idUserBiroUnit+"");
                setId_status(3+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_biro_unit", SignIn.idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);
                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        HistoryFragment.animateView(HistoryFragment.MainprogressOverlay, View.GONE, 0, 200);
                        HistoryFragment.progressBarShowHistory.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            //data history found
                            if(status_code.equals("200")){
                                RecyclerView history = (RecyclerView)activityCaller.findViewById(R.id.rvHistory);
                                TextView txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                TextView txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                ImageView imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                ArrayList<ProviderHistory> arrProvHistory = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                history.setItemViewCacheSize(0);

                                if(jsonArray.length()!=0){
                                    imgView.setImageResource(0);
                                    txtKet.setVisibility(View.INVISIBLE);
                                    txtOops.setVisibility(View.INVISIBLE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProviderHistory providerHistory = new ProviderHistory();

                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idAspirasi = resultObject.getString("id_aspirasi");
                                        tglAspirasi = resultObject.getString("tgl_aspirasi");
                                        teksAspirasi = resultObject.getString("teks_aspirasi");
                                        idStatusAspirasi = Integer.parseInt(
                                                resultObject.getString("id_status_aspirasi"));
                                        anonym = Integer.parseInt(resultObject.getString("anonym"));

                                        if(SignIn.signInMode == 0){
                                            biroUnit = resultObject.getString("nama_biro_unit");
                                            fotoBiroUnit = resultObject.getString("profile_picture");
                                            providerHistory.setNamaBiroUnit(biroUnit);
                                            providerHistory.setFotoBiroUnit(fotoBiroUnit);

                                            String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                            //get result from encoded_image
                                            byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                            Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                            providerHistory.setImageBitmap(bmp);
                                            providerHistory.setFotoBiroUnitbytearray(byteArray);
                                        }
                                        else if(SignIn.signInMode == 1){
                                            userCivitas = resultObject.getString("nama_lengkap");
                                            providerHistory.setNamaUserCivitas(userCivitas);
                                        }

//                                        int id_biro_unit = resultObject.getInt("id_user_biro_unit");
//                                        idStatusAspirasi = resultObject.getInt("id_status_aspirasi");


                                        providerHistory.setIdAspirasi(Integer.parseInt(idAspirasi));
                                        providerHistory.setTeksAspirasi(teksAspirasi);
                                        providerHistory.setTglAspirasi(tglAspirasi);
//                                        providerHistory.setTglStatusAspirasi(tglStatusAspirasi);
//                                        providerHistory.setIdBiroUnit(id_biro_unit);
                                        providerHistory.setAnonym(anonym);
                                        providerHistory.setIdStatusAspirasi(idStatusAspirasi);

                                        arrProvHistory.add(providerHistory);
                                    }
                                    AdapterHistory adp = new AdapterHistory(arrProvHistory);
                                    history.setAdapter(adp);
                                }


                            }

                            //data history not found
                            else{
                                TextView txtKet, txtOops;
                                ImageView imgView;
                                RecyclerView rvHistory;
                                rvHistory   = (RecyclerView) activityCaller.findViewById(R.id.rvHistory);
                                txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                //check user class
                                if(SignIn.signInMode==1){
                                    txtOops.setText("Stay Tune!");
                                    txtKet.setText("Riwayat aspirasi masih kosong\nNantikan aspirasi dari civitas akademika");
                                }

                                ArrayList<ProviderHistory> arrProvHist = new ArrayList<>();
                                AdapterHistory adpHistory = new AdapterHistory(arrProvHist);
                                rvHistory.setAdapter(adpHistory);
                                imgView.setImageResource(R.drawable.empty);
                                txtKet.setVisibility(View.VISIBLE);
                                txtOops.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        //get history -- completed
        else if(callMode.equals("getCompletedProcessHistory")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();

            //user civitas
            if(SignIn.signInMode == 0){
                setId_user(idUser+"");
                setId_status(0+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_civitas", SignIn.idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            //user biro unit
            else if(SignIn.signInMode == 1){
                setId_user(id_user=idUserBiroUnit+"");
                setId_status(3+"");

                builder.scheme("http")
                        .encodedAuthority("opensource.petra.ac.id/~m26416103")
                        .encodedPath("skripsi/detail_status_aspirasi/getDetailStatus.php")
                        .appendQueryParameter("id_user_biro_unit", SignIn.idUser+"")
                        .appendQueryParameter("status_aspirasi", getStatusAspirasi());
            }

            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        HistoryFragment.animateView(HistoryFragment.MainprogressOverlay, View.GONE, 0, 200);
                        HistoryFragment.progressBarShowHistory.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");
                            //data history found
                            if(status_code.equals("200")){
                                RecyclerView history = (RecyclerView)activityCaller.findViewById(R.id.rvHistory);
                                TextView txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                TextView txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                ImageView imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                ArrayList<ProviderHistory> arrProvHistory = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                if(jsonArray.length()!=0){
                                    imgView.setImageResource(0);
                                    txtKet.setVisibility(View.INVISIBLE);
                                    txtOops.setVisibility(View.INVISIBLE);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ProviderHistory providerHistory = new ProviderHistory();

                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        idAspirasi = resultObject.getString("id_aspirasi");
                                        tglAspirasi = resultObject.getString("tgl_aspirasi");
                                        teksAspirasi = resultObject.getString("teks_aspirasi");
                                        idStatusAspirasi = Integer.parseInt(
                                                resultObject.getString("id_status_aspirasi"));
                                        anonym = Integer.parseInt(resultObject.getString("anonym"));

                                        if(SignIn.signInMode == 0){
                                            biroUnit = resultObject.getString("nama_biro_unit");
                                            fotoBiroUnit = resultObject.getString("profile_picture");
                                            providerHistory.setNamaBiroUnit(biroUnit);
                                            providerHistory.setFotoBiroUnit(fotoBiroUnit);

                                            String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                            //get result from encoded_image
                                            byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                            Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                            providerHistory.setImageBitmap(bmp);
                                            providerHistory.setFotoBiroUnitbytearray(byteArray);
                                        }
                                        else if(SignIn.signInMode == 1){
                                            userCivitas = resultObject.getString("nama_lengkap");
                                            providerHistory.setNamaUserCivitas(userCivitas);
                                        }

//                                        int id_biro_unit = resultObject.getInt("id_user_biro_unit");
//                                        idStatusAspirasi = resultObject.getInt("id_status_aspirasi");


                                        providerHistory.setIdAspirasi(Integer.parseInt(idAspirasi));
                                        providerHistory.setTeksAspirasi(teksAspirasi);
                                        providerHistory.setTglAspirasi(tglAspirasi);
//                                        providerHistory.setTglStatusAspirasi(tglStatusAspirasi);
//                                        providerHistory.setIdBiroUnit(id_biro_unit);
                                        providerHistory.setAnonym(anonym);
                                        providerHistory.setIdStatusAspirasi(idStatusAspirasi);

                                        arrProvHistory.add(providerHistory);
                                    }
                                    AdapterHistory adp = new AdapterHistory(arrProvHistory);
                                    history.setAdapter(adp);
                                }


                            }

                            //data history not found
                            else{
                                TextView txtKet, txtOops;
                                ImageView imgView;
                                RecyclerView rvHistory;
                                rvHistory   = (RecyclerView) activityCaller.findViewById(R.id.rvHistory);
                                txtKet      = (TextView) activityCaller.findViewById(R.id.txtKet);
                                txtOops     = (TextView) activityCaller.findViewById(R.id.txtOops);
                                imgView     = (ImageView) activityCaller.findViewById(R.id.imgView);

                                //check user class
                                if(SignIn.signInMode==1){
                                    txtOops.setText("Stay Tune!");
                                    txtKet.setText("Riwayat aspirasi masih kosong\nNantikan aspirasi dari civitas akademika");
                                }

                                ArrayList<ProviderHistory> arrProvHist = new ArrayList<>();
                                AdapterHistory adpHistory = new AdapterHistory(arrProvHist);
                                rvHistory.setAdapter(adpHistory);
                                imgView.setImageResource(R.drawable.empty);
                                txtKet.setVisibility(View.VISIBLE);
                                txtOops.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get profile picture or content history foto biro unit
        else if(callMode.equals("getFotoBiroUnit")){
            //URI builder for url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority("opensource.petra.ac.id/~m26416103")
                    .encodedPath("skripsi/biro_unit/images/images.php")
                    .appendQueryParameter("id_user_biro_unit", idBiroUnit);
            String apiURL = builder.build().toString();

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                if(modeFotoBiroUnit.equals("profile")){
                    mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_foto_biro_unit);
                    mShimmerViewContainer.startShimmerAnimation();
                }
                else{
                    mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_show_foto_biro_unit_hist);
                    mShimmerViewContainer.startShimmerAnimation();
                }

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);

                            String status_code = responseObject.getString("status_code");

                            RecyclerView rvContentFotoBiroUnit = null;
                            if(modeFotoBiroUnit.equals("profile")){
                                rvContentFotoBiroUnit = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvContentFotoBiroUnit);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvContentFotoBiroUnit.setLayoutManager(mLinearLayoutManager);
                            }
                            else if(modeFotoBiroUnit.equals("history")){
                                rvContentFotoBiroUnit = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvContentFotoBiroUnitHist);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvContentFotoBiroUnit.setLayoutManager(mLinearLayoutManager);
                            }

                            ProviderFotoBiroUnit providerFotoBiroUnit;
                            ArrayList<ProviderFotoBiroUnit> arrProvFotoBiroUnit;

                            //data foto aspirasi found
                            if(status_code.equals("200")){
                                arrProvFotoBiroUnit = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        fotoBiroUnit = resultObject.getString("profile_picture");

                                        providerFotoBiroUnit = new ProviderFotoBiroUnit();
                                        //decode image
                                        String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        providerFotoBiroUnit.setImageBitmap(bmp);
                                        arrProvFotoBiroUnit.add(providerFotoBiroUnit);

                                    }
                                }
                            }
                            else{
                                arrProvFotoBiroUnit = new ArrayList<>();
                                providerFotoBiroUnit = new ProviderFotoBiroUnit();
                                providerFotoBiroUnit.setImageBitmap(null);
                                arrProvFotoBiroUnit.add(providerFotoBiroUnit);
                            }

                            if(modeFotoBiroUnit.equals("history")){
                                AdapterContentFotoBiroUnitHistory adpContentFotoBiroUnitHistory =
                                        new AdapterContentFotoBiroUnitHistory(arrProvFotoBiroUnit);
                                rvContentFotoBiroUnit.setAdapter(adpContentFotoBiroUnitHistory);
                            }
                            else if(modeFotoBiroUnit.equals("profile")){
                                AdapterContentFotoBiroUnit adpContentFotoBiroUnit =
                                        new AdapterContentFotoBiroUnit(arrProvFotoBiroUnit);
                                rvContentFotoBiroUnit.setAdapter(adpContentFotoBiroUnit);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get all biro unit
        else if(callMode.equals("getBiroUnit")){
            String apiURL = ipAddress+"biro_unit/getBiroUnit.php";
            //URI builder for url

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_see_biro_unit);
                mShimmerViewContainer.startShimmerAnimation();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");

                            //data biro unit found
                            if(status_code.equals("200")){

                                final ArrayList<ProviderBiroUnit> arrProvBiroUnit = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final RecyclerView rvBiroUnit = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvBiroUnit);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvBiroUnit.setLayoutManager(mLinearLayoutManager);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        namaBiroUnit = resultObject.getString("nama_biro_unit");
                                        emailBiroUnit = resultObject.getString("email");
                                        jobDescBiroUnit = resultObject.getString("informasi_jobdesc");
                                        ratingBiroUnit = resultObject.getString("rating");
                                        fotoBiroUnit = resultObject.getString("profile_picture");
                                        idBiroUnit = resultObject.getString("id_user_biro_unit");

                                        String arrFotoBiroUnit[]   = fotoBiroUnit.split(",");

                                        //get result from encoded_image
                                        byte[] decodedString = Base64.decode(arrFotoBiroUnit[1], Base64.DEFAULT);
                                        Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        decodedByte.compress(Bitmap.CompressFormat.PNG, 80, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        ProviderBiroUnit providerBiroUnit = new ProviderBiroUnit();
                                        providerBiroUnit.setIdBiroUnit(idBiroUnit);
                                        providerBiroUnit.setNamaBiroUnit(namaBiroUnit);
                                        providerBiroUnit.setEmailBiroUnit(emailBiroUnit);
                                        providerBiroUnit.setJobDescBiroUnit(jobDescBiroUnit);
                                        providerBiroUnit.setRatingBiroUnit(ratingBiroUnit);
                                        providerBiroUnit.setImageBitmap(bmp);
                                        providerBiroUnit.setBytearray(byteArray);


                                        arrProvBiroUnit.add(providerBiroUnit);
                                    }

                                    AdapterBiroUnit adpBiroUnit = new AdapterBiroUnit(activityCaller, arrProvBiroUnit, new AdapterBiroUnit.OnItemClick() {
                                        @Override
                                        public void onClick(String idBiroUnit,String namaBiroUnit,String emailBiroUnit,
                                                            String ratingBiroUnit, String jobDescBiroUnit) {
                                            Intent intent          = new Intent(getActivityCaller(), DetailBiroUnit.class);
                                            Bundle b               = new Bundle();
                                            //put data biro unit
                                            b.putString("id_user_biro_unit",idBiroUnit);
                                            b.putString("nama_biro_unit",namaBiroUnit);
                                            b.putString("contact_biro_unit",emailBiroUnit);
                                            b.putString("rating_biro_unit",ratingBiroUnit);
                                            b.putString("info_job_desc", jobDescBiroUnit);
//                                            b.putByteArray("detail_photo_avatar", byteArray);

                                            intent.putExtras(b);
                                            getActivityCaller().startActivity(intent);
                                        }
                                    });

//                                    AdapterBiroUnit adp = new AdapterBiroUnit(arrProvBiroUnit);
                                    rvBiroUnit.setAdapter(adpBiroUnit);
                                }
                            }

                            //data history not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        //get trending
        else if(callMode.equals("getTrending")){
            String apiURL = ipAddress+"trending/trending.php";
            //URI builder for url

            try {
                RequestQueue requestQueue   = Volley.newRequestQueue(activityCaller);

                mShimmerViewContainer = getActivityCaller().findViewById(R.id.shimmer_view_see_trending);
                mShimmerViewContainer.startShimmerAnimation();

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        apiURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        Log.i("LOG_RESPONSE", response);
                        //get response
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String status_code = responseObject.getString("status_code");

                            //data trending found
                            if(status_code.equals("200")){

                                final ArrayList<ProviderTrending> arrProvTrending = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray jsonArray =jsonObject.getJSONArray("result");

                                final RecyclerView rvTrending = (RecyclerView)
                                        getActivityCaller().findViewById(R.id.rvTrending);
                                GridLayoutManager mLinearLayoutManager  = new GridLayoutManager(getActivityCaller(),
                                        1);
                                rvTrending.setLayoutManager(mLinearLayoutManager);

                                if(jsonArray.length()!=0){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject resultObject = jsonArray.getJSONObject(i);
                                        teksTrending = resultObject.getString("trending_complaint");
                                        biroUnitTrending = resultObject.getString("nama_biro_unit");
                                        idTrending = resultObject.getString("id_trending");

                                        ProviderTrending providerTrending = new ProviderTrending();
                                        providerTrending.setTeksTrending(teksTrending);
                                        providerTrending.setBiroUnitTrending(biroUnitTrending);
                                        providerTrending.setIdTrending(idTrending);

                                        arrProvTrending.add(providerTrending);
                                    }

                                    AdapterTrending adpTrending = new AdapterTrending(activityCaller, arrProvTrending, new AdapterTrending.OnItemClick() {
                                        @Override
                                        public void onClick(String idTrending,String teksTrending,String biroUnitTrending) {
                                            Toast.makeText(activityCaller, "Clicked", Toast.LENGTH_SHORT).show();
//                                            Intent intent          = new Intent(getActivityCaller(), DetailTrending.class);
//                                            Bundle b               = new Bundle();
//                                            //put data biro unit
//                                            b.putString("id_trending",idTrending);
//                                            b.putString("teks_trending",teksTrending);
//                                            b.putString("biro_unit_trending",biroUnitTrending);
//
//                                            intent.putExtras(b);
//                                            getActivityCaller().startActivity(intent);
                                        }
                                    });

//                                    AdapterBiroUnit adp = new AdapterBiroUnit(arrProvBiroUnit);
                                    rvTrending.setAdapter(adpTrending);
                                }
                            }

                            //data history not found
                            else{

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        return "none";
    }


}
