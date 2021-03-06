package com.example.progettoingsw;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.widget.Toast;

import com.example.progettoingsw.Connection.ConnectionClass;
import com.example.progettoingsw.Dao.Struttura;
import com.example.progettoingsw.Dao.StrutturaDaoImp;
import com.example.progettoingsw.Dao.UtenteDaoImp1;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback/*GoogleMap.OnMarkerClickListener*/ {

    double longitude;
    double latitude;
   String nickname;
    String indirizzo;
    LocationManager locationManager;
    SupportMapFragment mapFragment;
    String citta;
    String nome;
    float longi;
    float lat;
    String ind;
    UtenteDaoImp1 utente;
    ArrayList<Struttura> s;
    StrutturaDaoImp struttura;




    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle e= getIntent().getExtras();
        if(e!= null)
        {
            nickname=e.getString("nickname");
        }
        utente=new UtenteDaoImp1();
        struttura=new StrutturaDaoImp();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            try {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10000, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng lng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = addressList.get(0);
                                    if (address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 && !address.getAddressLine(0).contentEquals("null")) {
                                        indirizzo = addressList.get(0).getAddressLine(0);
                                        citta = addressList.get(0).getCountryName();

                                        if (ConnectionClass.con == null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MapsActivity.this, "Check DBMS Connection", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        } else {

                                                s=struttura.getListaStruttureByLatitudineLongitudine((latitude-0.03),(latitude+0.03),(longitude-1),(longitude+1));
                                                for(Struttura s1:s)
                                                {
                                                    nome = s1.getNome();
                                                    longi=s1.getLongitudine();
                                                    lat=s1.getLatitudine();
                                                    ind=s1.getIndirizzo();
                                                    LatLng lng1 = new LatLng(lat, longi);
                                                    MarkerOptions markerOpt = new MarkerOptions()
                                                            .position(lng1).title(nome+":"+ind);
                                                    mMap.addMarker(markerOpt);
                                                    markerOpt.isVisible();
                                                }

                                        }
                                        mMap.addMarker(new MarkerOptions().position(lng).title("sei qui"));
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(lng).zoom(15).build();


                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {


                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            //Toast.makeText(MapsActivity.this,"Posizione disattivata",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                            intent.putExtra("nickname",nickname);
                            startActivity(intent);
                            finish();


                        }
                    });
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10000, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng lng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());


                            try {
                                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addressList != null && addressList.size() > 0) {
                                    Address address = addressList.get(0);
                                    if (address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 && !address.getAddressLine(0).contentEquals("null")) {
                                        indirizzo = addressList.get(0).getAddressLine(0);
                                        citta = addressList.get(0).getCountryName();

                                        if (ConnectionClass.con == null) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MapsActivity.this, "Check DBMS Connection", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        } else {

                                                s=struttura.getListaStruttureByLatitudineLongitudine((latitude-0.03),(latitude+0.03),(longitude-1),(longitude+1));
                                                for(Struttura s1:s) {
                                                    nome = s1.getNome();
                                                    longi = s1.getLongitudine();
                                                    lat = s1.getLatitudine();
                                                    ind = s1.getIndirizzo();
                                                    LatLng lng1 = new LatLng(lat, longi);
                                                    MarkerOptions markerOpt = new MarkerOptions()
                                                            .position(lng1).title(nome + ":" + ind);
                                                    mMap.addMarker(markerOpt);
                                                    markerOpt.isVisible();
                                                }

                                        }
                                        mMap.addMarker(new MarkerOptions().position(lng).title("sei qui"));
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(lng).zoom(15).build();


                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {


                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText(MapsActivity.this, "Posizione disattivata", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapsActivity.this, RicercaActivity.class);
                            intent.putExtra("nickname",nickname);
                            startActivity(intent);
                            finish();


                        }
                    });
                } else {
                    Toast.makeText(MapsActivity.this, "Posizione disattivata", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, RicercaActivity.class);
                    intent.putExtra("nickname",nickname);
                    startActivity(intent);
                    finish();
                }


            } catch (Exception f) {
                f.printStackTrace();
            }


    }



        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            //googleMap.setOnMarkerClickListener(this);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    if (arg0 != null && !arg0.getTitle().equals("sei qui")) {
                        Intent intent1 = new Intent(MapsActivity.this, StrutturaActivity.class);
                        intent1.putExtra("string",arg0.getTitle());
                        intent1.putExtra("nickname",nickname);
                        startActivity(intent1);
                    }


                }
            });
        }
    @Override
    public void onDestroy() {
        // RUN SUPER | REGISTER ACTIVITY AS NULL IN APP CLASS
        if(nickname!=null)
            utente.setLogOut(nickname);
        super.onDestroy();

    }








    }
