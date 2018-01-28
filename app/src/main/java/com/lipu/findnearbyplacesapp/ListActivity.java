package com.lipu.findnearbyplacesapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lipu.findnearbyplacesapp.Permissions.RequestUserPermission;
import com.lipu.findnearbyplacesapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.lipu.findnearbyplacesapp.CustomListviewAdapter.OnClickListenerAdvanceListRow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ListActivity extends Activity {
    List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    ListView lv;
    ImageButton rate, mail, otherproducts;
    Button back;
    PrefsValues m_pPrefsValues;
    private InterstitialAd interstitial;
    AdRequest adRequestIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list);
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-1982840936615923/8353126897");

        // Create ad request.
        adRequestIn = new AdRequest.Builder().build();

        AdView mAdView = (AdView) findViewById(R.id.adView1);
        if (isNetworkAvailable()) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else
            mAdView.setVisibility(View.GONE);
        m_pPrefsValues = new PrefsValues(this);
        lv = (ListView) findViewById(R.id.listView1);
        back = (Button) findViewById(R.id.btn_back);
        rate = (ImageButton) findViewById(R.id.rate);
        otherproducts = (ImageButton) findViewById(R.id.ibOurProducts);
        mail = (ImageButton) findViewById(R.id.ibBanglaDua);
        data = MainActivity.dataMap;
        CustomListviewAdapter adapter = new CustomListviewAdapter(data,
                ListActivity.this, m_onClickListenerBookingListRow);
        lv.setAdapter(adapter);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent cv = new Intent(ListActivity.this, MainActivity.class/* otherclass */);
                cv.putExtra("Category", MainActivity.categoryGlobal);
                startActivity(cv);
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);

            }
        });
        mail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        Feedback.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);

            }
        });
        otherproducts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("market://search?q=pub:Lipdroid")));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/developer?id=Lipdroid&hl=en")));
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            ListActivity.this);
                    builder.setTitle("Information");
                    builder.setMessage("No Internet Connection!!");
                    builder.setNegativeButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            });
                    builder.create();
                    builder.show();
                }

            }
        });
        rate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ListActivity.this);
                builder.setTitle("Rating");
                builder.setMessage("Rate this Application!");
                builder.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                if (isNetworkAvailable()) {
                                    Uri uri = Uri.parse("market://details?id="
                                            + getApplicationContext()
                                            .getPackageName());
                                    Intent goToMarket = new Intent(
                                            Intent.ACTION_VIEW, uri);
                                    try {
                                        startActivity(goToMarket);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("http://play.google.com/store/apps/details?id="
                                                        + getApplicationContext()
                                                        .getPackageName())));
                                    }
                                } else
                                    Toast.makeText(
                                            ListActivity.this,
                                            "Please Check your internet connection",
                                            Toast.LENGTH_LONG).show();
                            }
                        });
                builder.create();
                builder.show();

            }
        });

    }

    private OnClickListenerAdvanceListRow m_onClickListenerBookingListRow = new OnClickListenerAdvanceListRow() {

        @Override
        public void OnClickRowAction(HashMap<String, String> hashMap, long id,
                                     int position) {

                // TODO Auto-generated method stub
                m_pPrefsValues.setDeslat(hashMap.get("lat"));
                m_pPrefsValues.setDeslng(hashMap.get("lng"));
                m_pPrefsValues.setName(hashMap.get("place_name"));
                m_pPrefsValues.setaddress(hashMap.get("vicinity"));
                Intent cv = new Intent(ListActivity.this, MainActivity.class/* otherclass */);
                cv.putExtra("Category", MainActivity.categoryGlobal);
                startActivity(cv);
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                finish();

        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            interstitial.loadAd(adRequestIn);

            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Call displayInterstitial() function

                    displayInterstitial();

                }

                @Override
                public void onAdClosed() {
                    // Proceed to the next level.

                }
            });
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }

    // Invoke displayInterstitial() when you are ready to display an
    // interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


}
