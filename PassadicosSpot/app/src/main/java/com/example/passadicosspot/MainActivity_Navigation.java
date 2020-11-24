package com.example.passadicosspot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.passadicosspot.classes.Imagem;
import com.example.passadicosspot.classes.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity_Navigation extends AppCompatActivity {
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;
    private String TypeUser;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    //private FirebaseRecyclerAdapter<Imagem, MessageViewHolder> mFirebaseAdapter;

    //Variables for data manipulation
    private ArrayList<Imagem> Array_Images = new ArrayList<>();

    //Variables for App stuff
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    private int checkedItem;
    private ArrayList<String> types = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__navigation);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            //displaySingleSelectionDialog();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();

            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("UserPost")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d("Notification", "Sucess");
                        }
                        Log.d("Notification", "Sucess");
                        Toast.makeText(MainActivity_Navigation.this, "Teste Notificação", Toast.LENGTH_SHORT).show();
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
        db.collection("Users").whereEqualTo("username", mUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().size()>0){
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            User x = document.toObject(User.class);
                            if (x.getUsername().equals(mUsername)) {
                                TypeUser = x.getTipo();
                            }
                        }
                    }
                    else{
                            displaySingleSelectionDialog();
                        }
                    Log.d("TYPEUSER", "TYPE:"+ TypeUser );
                    } else{

                    Log.d("ErroDB", "Erro em Obter os Dados!");
                }
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment, R.id.feedFragment, R.id.profileFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
    private void displaySingleSelectionDialog() {

        types.add("Normal");
        types.add("Perito");
        String[] adapter = types.toArray(new String[0]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Qual o tipo de utilizador?");
        dialogBuilder.setSingleChoiceItems( adapter, checkedItem,
                (dialogInterface, which) -> {
                    checkedItem = which;
                });
        dialogBuilder.setPositiveButton("Done", (dialog, which) -> showSelectedVersion());
        dialogBuilder.create().show();
    }
    private void showSelectedVersion() {
        User x = new User(mUsername,types.get(checkedItem));
        db.collection("Users").add(x);
        Toast.makeText(this, "Utilizador " + types.get(checkedItem), Toast.LENGTH_SHORT).show();
    }
    public String getUsername(){
        return mUsername;
    }
    public FirebaseAuth getAuth() {return mFirebaseAuth;}
    public FirebaseUser getmFirebaseUser() {return mFirebaseUser;}
    public  GoogleSignInClient getClient(){return mSignInClient;}
    public String getTypeUser(){return TypeUser;}
    public User getUser(){return new User(mUsername,TypeUser);}
}