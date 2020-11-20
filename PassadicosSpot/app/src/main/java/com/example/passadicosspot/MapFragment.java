package com.example.passadicosspot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Imagem> listaImages = new ArrayList<>();
    static final Map<String, Imagem> mImages = new HashMap<>();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        /*view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //checkLocationPermission();
        mapFragment.getMapAsync(this);
        db.collection("Imagens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Imagem x= document.toObject(Imagem.class);
                                x.setId(document.getId());
                                listaImages.add(x);
                                Log.d("SucessoDB", "Sucesso em Obter os Dados!");
                                //s.append(document.getId() +" ->"+ x.toString());
                            }
                            for (Imagem i : listaImages){
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(i.getLocation().getLatitude(),i.getLocation().getLongitude()))
                                        .title(i.getUsername())
                                        .snippet("0")
                                );
                            }
                        } else{
                            Log.d("ErroDB", "Erro em Obter os Dados!");
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions pOptions = new PolylineOptions();
        pOptions.addAll(Arrays.asList(ProjectConstants.route));
        Polyline polyline1 = googleMap.addPolyline(pOptions);
        LatLng MELBOURNE = new LatLng(40.9928911, -8.2113895);
        Marker melbourne = mMap.addMarker(new MarkerOptions()
                .position(MELBOURNE)
                .title("BananaLand")
                .snippet("0")
        );

        //mMap.setInfoWindowAdapter(new Custom_InfoAdapter(mImages));
        //melbourne.showInfoWindow();
        UiSettings u = mMap.getUiSettings();
        u.setTiltGesturesEnabled(false);
        u.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.9689, -8.20364), 12.75f));
    }
    /*
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/
    /*
    private class Custom_InfoAdapter implements GoogleMap.InfoWindowAdapter{
        private final Map<String,Bitmap> mapaImagens;
        public Custom_InfoAdapter(final Map<String,Bitmap> m){
            mapaImagens=m;
        }
        @Override
        public View getInfoWindow(Marker marker) {
            LayoutInflater inflater = LayoutInflater.from(MapFragment.this);
            View view = inflater.inflate(R.layout.info_view_item, null);
            TextView textView = view.findViewById(R.id.textViewTitle);
            textView.setText(marker.getTitle());
            ImageView imageView= view.findViewById(R.id.ImgView);
            imageView.setImageBitmap(mapaImagens.get(marker.getSnippet()));
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }*/
}