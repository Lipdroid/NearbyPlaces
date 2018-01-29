package com.lipu.findnearbyplacesapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lipu.findnearbyplacesapp.permissions.RequestUserPermission;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lipu.findnearbyplacesapp.utils.CorrectSizeUtil;

public class MainActivity extends FragmentActivity implements LocationListener,
        OnClickListener, OnMapReadyCallback {

    GoogleMap mGoogleMap;
    private List<LatLng> pontos;
    Spinner mSprPlaceType;
    public static String categoryGlobal;
    String[] mPlaceType = null;
    String[] mPlaceTypeName = null;
    ProgressDialog progressDialog, dialog;
    double mLatitude = 0;
    double mLongitude = 0;
    Button airport, atm, bank, doctor, mosque, church, restaurant, bar,
            car_repair, departmental, fire, food, gas, gym, hospital, police,
            post, school, shopping, stadium, store, taxi, train, university,
            zoo, bus, list;
    ImageView back,btn_back_indicator;
    ImageButton rate, mail, otherproducts;
    int icon;
    private ArrayList<Marker> markersDriver = new ArrayList<Marker>();
    Marker me;
    LocationManager mlocationManager;
    public static List<HashMap<String, String>> dataMap;
    PrefsValues m_pPrefsValues;
    private InterstitialAd interstitial;
    AdRequest adRequestIn;

    private ImageView btn_menu = null;
    private PopupWindow popupWindow = null;
    CorrectSizeUtil mCorrectSize = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        m_pPrefsValues = new PrefsValues(this);
        initializations();

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
        // Array of place types
        // mPlaceType = getResources().getStringArray(R.array.place_type);

        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

        // Getting reference to the Spinner
        // mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);

        // Setting adapter on Spinner to set place types
        // mSprPlaceType.setAdapter(adapter);

        Button btnFind;

        // Getting reference to Find Button
        // btnFind = (Button) findViewById(R.id.btn_find);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent cv = new Intent(MainActivity.this, TypeList.class/* otherclass */);
                    startActivity(cv);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);

                }
            });


            list.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent cv = new Intent(MainActivity.this,
                            ListActivity.class/* otherclass */);

                    startActivity(cv);
                    finish();
                    overridePendingTransition(R.anim.push_left_in,
                            R.anim.push_left_out);
                }
            });

            btn_menu = (ImageView) findViewById(R.id.btn_menu);
            btn_menu.setVisibility(View.VISIBLE);

            btn_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //menu btn pressed
                    showPopup(btn_menu);
                }
            });
            airport.setOnClickListener(this);
            atm.setOnClickListener(this);
            bank.setOnClickListener(this);
            doctor.setOnClickListener(this);
            restaurant.setOnClickListener(this);
            mosque.setOnClickListener(this);
            church.setOnClickListener(this);

            bar.setOnClickListener(this);
            car_repair.setOnClickListener(this);
            departmental.setOnClickListener(this);
            fire.setOnClickListener(this);
            food.setOnClickListener(this);
            gas.setOnClickListener(this);
            gym.setOnClickListener(this);

            hospital.setOnClickListener(this);
            police.setOnClickListener(this);
            post.setOnClickListener(this);
            school.setOnClickListener(this);
            shopping.setOnClickListener(this);
            stadium.setOnClickListener(this);
            store.setOnClickListener(this);

            taxi.setOnClickListener(this);
            train.setOnClickListener(this);
            university.setOnClickListener(this);
            zoo.setOnClickListener(this);
            bus.setOnClickListener(this);

        }
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();

    }

    private void pointthepins() {
        // TODO Auto-generated method stub
        categoryGlobal = getIntent().getExtras().getString("Category");
        int pressed = Integer.parseInt(categoryGlobal);
        switch (pressed) {
            case 0:
                airport.performClick();

                break;
            case 1:
                atm.performClick();
                break;
            case 2:
                bank.performClick();
                break;
            case 3:
                doctor.performClick();
                break;
            case 4:
                restaurant.performClick();
                break;
            case 5:
                mosque.performClick();
                break;
            case 6:
                church.performClick();
                break;
            case 7:
                bar.performClick();
                break;
            case 8:
                car_repair.performClick();

                break;
            case 9:
                departmental.performClick();
                break;

            case 10:
                fire.performClick();
                break;
            case 11:
                food.performClick();
                break;
            case 12:
                gas.performClick();
                break;
            case 13:
                gym.performClick();
                break;
            case 14:
                hospital.performClick();
                break;
            case 15:
                police.performClick();
                break;
            case 16:
                post.performClick();
                break;
            case 17:
                school.performClick();
                break;
            case 18:
                shopping.performClick();
                break;
            case 19:
                stadium.performClick();
                break;
            case 20:
                store.performClick();
                break;
            case 21:
                taxi.performClick();
                break;
            case 22:
                train.performClick();
                break;
            case 23:
                university.performClick();
                break;
            case 24:
                zoo.performClick();
                break;
            case 25:
                bus.performClick();
                break;

            default:
                break;
        }

    }

    private Location getLastKnownLocation() {
        mlocationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);
        List<String> providers = mlocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return new Location(provider);
            }
            Location l = mlocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void initializations() {
        // TODO Auto-generated method stub
        back = (ImageView) findViewById(R.id.btn_back);
        btn_back_indicator = (ImageView)findViewById(R.id.btn_back_indicator);
        list = (Button) findViewById(R.id.btn_list);
        airport = (Button) findViewById(R.id.btn_airport);
        atm = (Button) findViewById(R.id.btn_atm);
        bank = (Button) findViewById(R.id.btn_bank);
        doctor = (Button) findViewById(R.id.btn_doctor);
        restaurant = (Button) findViewById(R.id.btn_restaurant);
        mosque = (Button) findViewById(R.id.btn_mosque);
        church = (Button) findViewById(R.id.btn_church);

        bar = (Button) findViewById(R.id.btn_bar);
        car_repair = (Button) findViewById(R.id.btn_car_repair);
        departmental = (Button) findViewById(R.id.btn_departmental_store);
        fire = (Button) findViewById(R.id.btn_fire_station);
        food = (Button) findViewById(R.id.btn_food);
        gas = (Button) findViewById(R.id.btn_gas_station);
        gym = (Button) findViewById(R.id.btn_gym);
        hospital = (Button) findViewById(R.id.btn_hospital);

        police = (Button) findViewById(R.id.btn_police);
        post = (Button) findViewById(R.id.btn_post_office);
        school = (Button) findViewById(R.id.btn_school);
        shopping = (Button) findViewById(R.id.btn_shopping);
        stadium = (Button) findViewById(R.id.btn_stadium);
        store = (Button) findViewById(R.id.btn_store);
        taxi = (Button) findViewById(R.id.btn_taxi_stand);
        train = (Button) findViewById(R.id.btn_train_station);

        university = (Button) findViewById(R.id.btn_university);
        zoo = (Button) findViewById(R.id.btn_zoo);
        bus = (Button) findViewById(R.id.btn_bus);

        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, .02f);
        mAnimation.setDuration(500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        btn_back_indicator.setAnimation(mAnimation);

    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("downloading url error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Request for location permission
        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        boolean is_location_permission_granted = requestUserPermission.verifyStoragePermissions();

        //check already permitted
        if (!is_location_permission_granted) {
            return;
        }
        // Getting Google Map
        mGoogleMap = googleMap;

        // Enabling MyLocation in Google Map
        mGoogleMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service
        // LOCATION_SERVICE
        // LocationManager locationManager = (LocationManager)
        // getSystemService(LOCATION_SERVICE);

        // Getting Current Location From GPS
        Location location = getLastKnownLocation();
        if (location != null) {
            onLocationChanged(location);
        }
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = mlocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocationManager.requestLocationUpdates(provider, 20000, 0, this);

        if (m_pPrefsValues.getDeslat().equals("")
                || m_pPrefsValues.getDeslng().equals("")) {
            if (getIntent().getExtras().getString("Category") == null) {

            } else
                pointthepins();
        } else
            new GetDirection().execute();
    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

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
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    class GetDirection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Drawing the route, please wait!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(String... args) {
            String origin = m_pPrefsValues.getUserlat() + ","
                    + m_pPrefsValues.getUserlng();
            String destination = m_pPrefsValues.getDeslat() + ","
                    + m_pPrefsValues.getDeslng();
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin="
                    + origin
                    + "&destination="
                    + destination
                    + "&sensor=true&mode=driving";
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

            } catch (Exception e) {

            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            for (int i = 0; i < pontos.size() - 1; i++) {
                LatLng src = pontos.get(i);
                LatLng dest = pontos.get(i + 1);
                try {
                    // here is where it will draw the polyline in your map
                    Polyline line = mGoogleMap
                            .addPolyline(new PolylineOptions()
                                    .add(new LatLng(src.latitude, src.longitude),
                                            new LatLng(dest.latitude,
                                                    dest.longitude)).width(8)
                                    .color(Color.BLUE).geodesic(true));


                } catch (NullPointerException e) {
                    Log.e("Error",
                            "NullPointerException onPostExecute: "
                                    + e.toString());
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
                }

            }
            Marker destination = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(m_pPrefsValues.getDeslat()), Double.parseDouble(m_pPrefsValues.getDeslng())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)).title(" DESTINATION: " + m_pPrefsValues.getName() + " : " + m_pPrefsValues.getaddress()));

            m_pPrefsValues.setDeslat("");
            m_pPrefsValues.setDeslng("");
            m_pPrefsValues.setName("");
            m_pPrefsValues.setaddress("");
            dialog.dismiss();
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

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends
            AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(
                String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mGoogleMap.clear();
            dataMap = list;
            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));
                // Placing a marker on the touched position
                mGoogleMap.addMarker(markerOptions);

            }

            // put man market
            LatLng latLngMe = new LatLng(mLatitude, mLongitude);

            for (int i1 = 0; i1 < markersDriver.size(); i1++) {
                markersDriver.get(i1).remove();
            }

            Marker me = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLngMe)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.people)).title("ME"));
            markersDriver.add(me);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMe));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            progressDialog.dismiss();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        m_pPrefsValues.setUserlat(mLatitude + "");
        m_pPrefsValues.setUserlng(mLongitude + "");

        LatLng latLng = new LatLng(mLatitude, mLongitude);

        for (int i = 0; i < markersDriver.size(); i++) {
            markersDriver.get(i).remove();
        }

        Marker me = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.people))
                .title("ME"));
        markersDriver.add(me);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        progressDialog = ProgressDialog.show(MainActivity.this, "",
                "Please wait...");
        if (v.getId() == R.id.btn_airport) {
            categoryGlobal = "0";
            showPlaces("airport");
            icon = R.drawable.pin_airport;
        }
        if (v.getId() == R.id.btn_atm) {
            categoryGlobal = "1";
            showPlaces("atm");
            icon = R.drawable.pin_atm;
        }
        if (v.getId() == R.id.btn_bank) {
            categoryGlobal = "2";
            showPlaces("bank");
            icon = R.drawable.pin_bank;
        }
        if (v.getId() == R.id.btn_doctor) {
            categoryGlobal = "3";
            showPlaces("doctor");
            icon = R.drawable.pin_doctor;
        }
        if (v.getId() == R.id.btn_restaurant) {
            categoryGlobal = "4";
            showPlaces("restaurant");
            icon = R.drawable.pin_food;
        }

        if (v.getId() == R.id.btn_mosque) {
            categoryGlobal = "5";
            showPlaces("mosque");
            icon = R.drawable.pin_mosque;
        }
        if (v.getId() == R.id.btn_church) {
            categoryGlobal = "6";
            showPlaces("church");
            icon = R.drawable.pin_church;
        }

        if (v.getId() == R.id.btn_bar) {
            categoryGlobal = "7";
            showPlaces("bar");
            icon = R.drawable.pin_bar;
        }
        if (v.getId() == R.id.btn_car_repair) {
            categoryGlobal = "8";
            showPlaces("car_repair");
            icon = R.drawable.pin_car_repair;
        }
        if (v.getId() == R.id.btn_departmental_store) {
            categoryGlobal = "9";
            showPlaces("department_store");
            icon = R.drawable.pin_department_store;
        }
        if (v.getId() == R.id.btn_fire_station) {
            categoryGlobal = "10";
            showPlaces("fire_station");
            icon = R.drawable.pin_fire_station;
        }
        if (v.getId() == R.id.btn_food) {
            categoryGlobal = "11";
            showPlaces("food");
            icon = R.drawable.pin_food;
        }
        if (v.getId() == R.id.btn_gas_station) {
            categoryGlobal = "12";
            showPlaces("gas_station");
            icon = R.drawable.pin_gas_station;
        }
        if (v.getId() == R.id.btn_gym) {
            categoryGlobal = "13";
            showPlaces("gym");
            icon = R.drawable.pin_gym;
        }
        if (v.getId() == R.id.btn_hospital) {
            categoryGlobal = "14";
            showPlaces("hospital");
            icon = R.drawable.pin_hospital;
        }
        if (v.getId() == R.id.btn_police) {
            categoryGlobal = "15";
            showPlaces("police");
            icon = R.drawable.pin_police;
        }
        if (v.getId() == R.id.btn_post_office) {
            categoryGlobal = "16";
            showPlaces("post_office");
            icon = R.drawable.pin_post_office;
        }
        if (v.getId() == R.id.btn_school) {
            categoryGlobal = "17";
            showPlaces("school");
            icon = R.drawable.pin_school;
        }
        if (v.getId() == R.id.btn_shopping) {
            categoryGlobal = "18";
            showPlaces("shopping_mall");
            icon = R.drawable.pin_shopping;
        }
        if (v.getId() == R.id.btn_stadium) {
            categoryGlobal = "19";
            showPlaces("stadium");
            icon = R.drawable.pin_stadium;
        }
        if (v.getId() == R.id.btn_store) {
            categoryGlobal = "20";
            showPlaces("store");
            icon = R.drawable.pin_store;
        }
        if (v.getId() == R.id.btn_taxi_stand) {
            categoryGlobal = "21";
            showPlaces("taxi_stand");
            icon = R.drawable.pin_taxi_stand;
        }
        if (v.getId() == R.id.btn_train_station) {
            categoryGlobal = "22";
            showPlaces("train_station");
            icon = R.drawable.pin_train_station;
        }
        if (v.getId() == R.id.btn_university) {
            categoryGlobal = "23";
            showPlaces("university");
            icon = R.drawable.pin_university;
        }
        if (v.getId() == R.id.btn_zoo) {
            categoryGlobal = "24";
            showPlaces("zoo");
            icon = R.drawable.pin_zoo;
        }
        if (v.getId() == R.id.btn_bus) {
            categoryGlobal = "25";
            showPlaces("bus_station");
            icon = R.drawable.pin_bus;
        }

    }

    private void showPlaces(String type) {
        // TODO Auto-generated method stub

        StringBuilder sb = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + type);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyCUsDcO7Nq0Dw_11pFW_Dlz0pWzKnzkcEw");

        // Creating a new non-ui thread task to download Google place json data
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());

    }

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

    /* you should refer to a view to stick your popup wherever u want.
** e.g. Button button  = (Button) findviewbyId(R.id.btn);
**     if(popupWindow != null)
**         showPopup(button);
**/
    public void showPopup(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_filter_layout, null);
        TextView rate_app = (TextView) popupView.findViewById(R.id.tv_clear_data);
        TextView more_apps = (TextView) popupView.findViewById(R.id.tv_add_app);
        TextView feedback = (TextView) popupView.findViewById(R.id.tv_statistic);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
            }
        });
        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                    Toast.makeText(MainActivity.this, "Please Check your internet connection", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.create();
                builder.show();


            }
        });

        more_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new app
                popupWindow.dismiss();
                if (isNetworkAvailable()) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Lipdroid")));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Lipdroid&hl=en")));
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open statictics page
                popupWindow.dismiss();
                Intent intent = new Intent(getApplicationContext(),
                        Feedback.class);
                startActivity(intent);

                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });


        //new MultipleScreen(mContext).reSizeViewPx(popupView);
        mCorrectSize.correctSize(popupView);

        popupWindow.showAsDropDown(v);
    }
}
