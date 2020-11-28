package com.example.passadicosspot.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.passadicosspot.MainActivity_Navigation;
import com.example.passadicosspot.R;
import com.example.passadicosspot.classes.Imagem;
import com.example.passadicosspot.classes.ProjectConstants;
import com.example.passadicosspot.classes.User;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends Fragment implements OnMapReadyCallback, DialogDescriptionFragment.OnDialogDismissListener {

    private GoogleMap mMap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Imagem> listaImages = new ArrayList<>();
    static final Map<String, Imagem> mImages = new HashMap<>();
    static final Map<String, ImageView> mImageViews = new HashMap<>();
    static final Map<String, Bitmap> mbitmaps = new HashMap<>();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String baseURL = "gs://icm-trabalho1.appspot.com/";
    private LocationManager mLocationManager;
    public static ArrayList<String> ArrayVazio = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private ArrayList<String> sentidos = ProjectConstants.sentidos;
    private int checkedItem;
    private GeoPoint fim;
    private double maxDistancia = Math.sqrt(Math.pow(-8.2113233 - -8.1767019, 2) + Math.pow(40.9932033 - 40.9529338, 2));
    private int tempoPrevisto = 150;
    private TextView displayTime;
    public MapFragment() {
        // Required empty public constructor
    }

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
        displayTime= view.findViewById(R.id.textViewTime);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        view.findViewById(R.id.floatingActionButtonRota).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rota(view);

            }
        });
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
        //Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        carregarImagens();

    }

    private void Rota(View view) {
        String[] adapter = sentidos.toArray(new String[0]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Qual o sentido do Percurso?");
        dialogBuilder.setSingleChoiceItems(adapter, checkedItem,
                (dialogInterface, which) -> {
                    checkedItem = which;
                });
        dialogBuilder.setPositiveButton("Done", (dialog, which) -> DeterminarFim(view));
        dialogBuilder.create().show();
    }

    private void DeterminarFim(View view) {
        if (checkedItem == 0) {
            fim = new GeoPoint(40.9932033, -8.2113233);
        } else {
            fim = new GeoPoint(40.9529338, -8.1767019);
        }
        displayTime.setVisibility(view.VISIBLE);
        Log.d("MapFragment-Rota", sentidos.get(checkedItem));
    }

    LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if(fim!=null) {
                    double distancia = Math.sqrt(Math.pow(fim.getLongitude() - location.getLongitude(), 2) + Math.pow(fim.getLatitude() - location.getLatitude(), 2));
                    Log.d("MapFragment-Distancia", distancia + "");
                    double temporestante = (distancia * tempoPrevisto) / maxDistancia;
                    Log.d("MapFragment-Minutos", temporestante + "m");
                    displayTime.setText("Faltam cerca de "+ Math.round(temporestante)+ " minutos");
                }
            }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // no meu emulador dá erro aqui não sei porquê
            // por isso tivemos de pôr esta função
        }

    };


    private void carregarImagens() {
        listaImages = new ArrayList<>();
        db.collection("Imagens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Imagem x = document.toObject(Imagem.class);
                                x.setId(document.getId());
                                listaImages.add(x);
                                Log.d("SucessoDB", "Sucesso em Obter os Dados!");
                                //s.append(document.getId() +" ->"+ x.toString());
                            }

                            mapFragment.getMapAsync(MapFragment.this);
                        } else {
                            Log.d("ErroDB", "Erro em Obter os Dados!");
                        }
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final View random_view = new View(getContext());
        PolylineOptions pOptions = new PolylineOptions();
        pOptions.addAll(Arrays.asList(ProjectConstants.route));
        Polyline polyline1 = googleMap.addPolyline(pOptions);
        for (int j = 0; j < listaImages.size(); j++) {
            Imagem i = listaImages.get(j);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(i.getLocation().getLatitude(), i.getLocation().getLongitude()))
                    .title(i.getUsername())
                    .snippet(String.valueOf(j))
            );
            final int key = j;
            ImageView imageView = new ImageView(getContext());
            StorageReference x = FirebaseStorage.getInstance().getReferenceFromUrl(i.getPhotoURL());
            x.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String downloadUrl = task.getResult().toString();

                        Glide.with(imageView)
                                .asBitmap()
                                .load(downloadUrl)
                                .into(new CustomTarget<Bitmap>() {

                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        mbitmaps.put(String.valueOf(key), resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });

                        mImageViews.put(String.valueOf(key), imageView);
                        Log.d("MapFragment-PostImage", "Successful");

                    } else {
                        Log.w("ErroImagem", "Getting download url was not successful.",
                                task.getException());
                    }
                }
            });
            mImages.put(String.valueOf(j), i);
        }
        mMap.setInfoWindowAdapter(new Custom_InfoAdapter(mImages));
        UiSettings u = mMap.getUiSettings();
        u.setTiltGesturesEnabled(false);
        u.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.9689, -8.20364), 12.75f));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("param1", listaImages.get(Integer.valueOf(marker.getSnippet())));
                bundle.putSerializable("param2", ((MainActivity_Navigation) getActivity()).getUser());
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_mapFragment_to_postFragment, bundle);
            }
        });


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, listener);

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            DialogDescriptionFragment d = new DialogDescriptionFragment(imageBitmap, this);
            d.show(getChildFragmentManager(), "tag");
        }
    }

    @Override
    public void onDialogDismissListener(Bitmap bitmap, String description) {
        mbitmaps.put("1", bitmap);

        Log.d("MapFragment-Description", description);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = ((MainActivity_Navigation) getActivity()).getUsername() + "_" + timeStamp + ".png";
        StorageReference x = FirebaseStorage.getInstance().getReference(imageFileName);
        Location location = null;
        boolean isLocationinArouca = true;
        try {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("MapFragment-location",location.getLatitude()+","+location.getLongitude());
            if (location.getLatitude() < 40.948139526617005
                    || location.getLatitude() > 40.996195057883995
                    || location.getLongitude() < -8.232616201179585
                    || location.getLongitude() > -8.160170554061377){
                Toast.makeText(getActivity(),"Não está perto dos Passadiços!",Toast.LENGTH_LONG).show();
                isLocationinArouca = false;
            }

        }catch (SecurityException e){
            return;
        }
        if (isLocationinArouca){
            putImageInStorage(x,bitmap,"",description,location);
        }

    }

    private void putImageInStorage(StorageReference storageReference,final Bitmap bitmap, final String key, String description, final Location location){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
        byte[] byteArray =  out.toByteArray();

        storageReference.putBytes(byteArray).addOnCompleteListener(getActivity(),(task)->{
            if (task.isSuccessful()){
                String name = task.getResult().getMetadata().getReference().getName();
                String imageUrl = baseURL + name;
                Date timeStamp = new Date();
                Imagem ImageReference = new Imagem(description,"",new GeoPoint(location.getLatitude(),location.getLongitude()),imageUrl,((MainActivity_Navigation)getActivity()).getUsername(), ArrayVazio, timeStamp);
                db.collection("Imagens").add(ImageReference);
                Log.d("MapFragment-FileName",name);
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(ImageReference.getUsername())
                        .snippet(String.valueOf(listaImages.size())));
                int m = listaImages.size();
                listaImages.add(ImageReference);
                mbitmaps.put(String.valueOf(m),bitmap);
            }
            else{
                Log.d("MapFragment-PutImage","Put Image in Storage Failed");
            }
        });
    }
    private class Custom_InfoAdapter implements GoogleMap.InfoWindowAdapter{
        private final Map<String,Imagem> mapaImagens;
        public Custom_InfoAdapter(final Map<String,Imagem> m){
            mapaImagens=m;
            String s = "";
            for(String i : mapaImagens.keySet()){
                s+=i + " " + mapaImagens.get(i).toString();
            }
        }
        @Override
        public View getInfoWindow(Marker marker) {
            LayoutInflater inflater = LayoutInflater.from(MapFragment.this.getContext());
            View view = inflater.inflate(R.layout.info_view_item, null);
            TextView textView = view.findViewById(R.id.textViewTitle);
            textView.setText(marker.getTitle());
            ImageView imageView= view.findViewById(R.id.ImgView);
            imageView.setImageBitmap(mbitmaps.get(marker.getSnippet()));
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }


    }
}