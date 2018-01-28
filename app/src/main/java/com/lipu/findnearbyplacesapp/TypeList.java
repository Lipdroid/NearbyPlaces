package com.lipu.findnearbyplacesapp;

import java.util.ArrayList;
import java.util.List;


import com.lipu.findnearbyplacesapp.Permissions.RequestUserPermission;
import com.lipu.findnearbyplacesapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TypeList extends Activity {

    private List<Type> googleTypes = new ArrayList<Type>();
    AutoCompleteTextView actv;
    ListView list;
    ImageButton search, rate, otherproducts, mail;
    PrefsValues m_pPrefsValues;
    boolean is_location_permission_granted = false;
    RequestUserPermission requestUserPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.type);
        AdView mAdView = (AdView) findViewById(R.id.adView1);
        if (isNetworkAvailable()) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        } else
            mAdView.setVisibility(View.GONE);


        m_pPrefsValues = new PrefsValues(this);
        search = (ImageButton) findViewById(R.id.search);
        rate = (ImageButton) findViewById(R.id.rate);
        otherproducts = (ImageButton) findViewById(R.id.ibOurProducts);
        mail = (ImageButton) findViewById(R.id.ibBanglaDua);
        String[] Types = getResources().getStringArray(R.array.place_type_name);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Types);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        actv.setAdapter(adapter);

        populateCarList();
        populateListView();
        registerClickCallback();
        mail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        Feedback.class);
                startActivity(intent);

                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


            }
        });
        otherproducts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Lipdroid")));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Lipdroid&hl=en")));
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TypeList.this);
                    builder.setTitle("Information");
                    builder.setMessage("No Internet Connection!!");
                    builder.setNegativeButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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

                AlertDialog.Builder builder = new AlertDialog.Builder(TypeList.this);
                builder.setTitle("Rating");
                builder.setMessage("Rate this Application!");
                builder.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                if (isNetworkAvailable()) {
                                    Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                    try {
                                        startActivity(goToMarket);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                                    }
                                } else
                                    Toast.makeText(TypeList.this, "Please Check your internet connection", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.create();
                builder.show();

            }
        });
        requestUserPermission = new RequestUserPermission(this);
        is_location_permission_granted = requestUserPermission.verifyStoragePermissions();
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (is_location_permission_granted) {
                    if (isNetworkAvailable()) {
                        Intent cv = new Intent(TypeList.this,
                                MainActivity.class/* otherclass */);
                        String s = actv.getText().toString();
                        int position = 30;
                        if (s.equals("Airport")) {
                            position = 0;
                        } else if (s.equals("ATM")) {
                            position = 1;
                        } else if (s.equals("Bank")) {
                            position = 2;
                        } else if (s.equals("Doctor")) {
                            position = 3;
                        } else if (s.equals("Restaurant")) {
                            position = 4;
                        } else if (s.equals("Mosque")) {
                            position = 5;
                        } else if (s.equals("Church")) {
                            position = 6;
                        } else if (s.equals("Bar")) {
                            position = 7;
                        } else if (s.equals("Car Repair")) {
                            position = 8;
                        } else if (s.equals("Departmental Store")) {
                            position = 9;
                        } else if (s.equals("Fire Stations")) {
                            position = 10;
                        } else if (s.equals("Food")) {
                            position = 11;
                        } else if (s.equals("Gas Stations")) {
                            position = 12;
                        } else if (s.equals("Gym")) {
                            position = 13;
                        } else if (s.equals("Hospital")) {
                            position = 14;
                        } else if (s.equals("Pollice")) {
                            position = 15;
                        } else if (s.equals("Post office")) {
                            position = 16;
                        } else if (s.equals("School")) {
                            position = 17;
                        } else if (s.equals("Shopping")) {
                            position = 18;
                        } else if (s.equals("Stadium")) {
                            position = 19;
                        } else if (s.equals("Store")) {
                            position = 20;
                        } else if (s.equals("Taxi Stations")) {
                            position = 21;
                        } else if (s.equals("Train Stations")) {
                            position = 22;
                        } else if (s.equals("University")) {
                            position = 23;
                        } else if (s.equals("Zoo")) {
                            position = 24;
                        } else if (s.equals("Bus Station")) {
                            position = 25;
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TypeList.this);
                            builder.setTitle("Information");
                            builder.setMessage("You are typing it wrong,please select from the dropdown!!");
                            builder.setNegativeButton("ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            dialog.dismiss();
                                        }
                                    });
                            builder.create();
                            builder.show();
                        }

                        if (position == 30) {

                        } else {
                            cv.putExtra("Category", position + "");
                            startActivity(cv);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TypeList.this);
                        builder.setTitle("Information");
                        builder.setMessage("No Internet Connection!!");
                        builder.setNegativeButton("ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                });
                        builder.create();
                        builder.show();

                    }
                } else {
                    is_location_permission_granted = requestUserPermission.verifyStoragePermissions();
                }
            }

        });
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

    private void populateCarList() {
        googleTypes.add(new Type("Airport", R.drawable.airport, "Press to see on map"));
        googleTypes.add(new Type("ATM", R.drawable.atm, "Press to see on map"));
        googleTypes.add(new Type("Bank", R.drawable.bank, "Press to see on map"));
        googleTypes.add(new Type("Doctor", R.drawable.doctor, "Press to see on map"));
        googleTypes.add(new Type("Restaurant", R.drawable.restaurant, "Press to see on map"));
        googleTypes.add(new Type("Mosque", R.drawable.mosque, "Press to see on map"));
        googleTypes.add(new Type("Church", R.drawable.church, "Press to see on map"));
        googleTypes.add(new Type("Bar", R.drawable.bar, "Press to see on map"));
        googleTypes.add(new Type("Car Repair", R.drawable.car_repair, "Press to see on map"));
        googleTypes.add(new Type("Departmental Store", R.drawable.department_store, "Press to see on map"));
        googleTypes.add(new Type("Fire Stations", R.drawable.fire_station, "Press to see on map"));
        googleTypes.add(new Type("Food", R.drawable.food, "Press to see on map"));
        googleTypes.add(new Type("Gas Stations", R.drawable.gas_station, "Press to see on map"));
        googleTypes.add(new Type("Gym", R.drawable.gym, "Press to see on map"));
        googleTypes.add(new Type("Hospital", R.drawable.hospital, "Press to see on map"));
        googleTypes.add(new Type("Pollice", R.drawable.police, "Press to see on map"));
        googleTypes.add(new Type("Post office", R.drawable.post_office, "Press to see on map"));
        googleTypes.add(new Type("School", R.drawable.school, "Press to see on map"));
        googleTypes.add(new Type("Shopping", R.drawable.shopping, "Press to see on map"));
        googleTypes.add(new Type("Stadium", R.drawable.stadium, "Press to see on map"));
        googleTypes.add(new Type("Store", R.drawable.store, "Press to see on map"));
        googleTypes.add(new Type("Taxi Stations", R.drawable.taxi_stand, "Press to see on map"));
        googleTypes.add(new Type("Train Stations", R.drawable.train_station, "Press to see on map"));
        googleTypes.add(new Type("University", R.drawable.university, "Press to see on map"));
        googleTypes.add(new Type("Zoo", R.drawable.zoo, "Press to see on map"));
        googleTypes.add(new Type("Bus Stations", R.drawable.bus, "Press to see on map"));

    }

    private void populateListView() {
        ArrayAdapter<Type> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.typeListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        list = (ListView) findViewById(R.id.typeListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {
                if(is_location_permission_granted) {
                    if (isNetworkAvailable()) {
                        Intent cv = new Intent(TypeList.this,
                                MainActivity.class/* otherclass */);
                        cv.putExtra("Category", position + "");
                        startActivity(cv);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TypeList.this);
                        builder.setTitle("Information");
                        builder.setMessage("No Internet Connection!!");
                        builder.setNegativeButton("ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                });
                        builder.create();
                        builder.show();
                    }

                }else {
                    is_location_permission_granted = requestUserPermission.verifyStoragePermissions();
                }
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Type> {
        public MyListAdapter() {
            super(TypeList.this, R.layout.item_view, googleTypes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view,
                        parent, false);
            }

            // Find the car to work with.
            Type currentCar = googleTypes.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView
                    .findViewById(R.id.item_icon);
            imageView.setImageResource(currentCar.getIconID());

            // Name:
            TextView name = (TextView) itemView.findViewById(R.id.item_txtMake);
            name.setText(currentCar.gettypeName());

            // Additional text:
            TextView additional = (TextView) itemView
                    .findViewById(R.id.item_txtYear);
            additional.setText("" + currentCar.getadditional());

            return itemView;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Do you really want to exit ?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(TypeList.this, SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            m_pPrefsValues.setDeslat("");
                            m_pPrefsValues.setDeslng("");
                            m_pPrefsValues.setName("");
                            m_pPrefsValues.setaddress("");
                            intent.putExtra("Exit me", true);
                            startActivity(intent);
                            finish();

                            // return;
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestUserPermission.REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    is_location_permission_granted = true;

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    is_location_permission_granted = false;

                }
                return;
            }

        }
    }
}
