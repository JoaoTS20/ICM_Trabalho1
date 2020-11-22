package com.example.passadicosspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {



    //Stress
    TextView s;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    //private FirebaseRecyclerAdapter<Imagem, MessageViewHolder> mFirebaseAdapter;

    //Variables for data manipulation
    private ArrayList<Imagem> Array_Images = new ArrayList<>();

    //Variables for App stuff
    private FragmentTransaction ft = getSupportFragmentManager().beginTransaction();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        //Obter Dados
        s= (TextView) findViewById(R.id.text);

        db.collection("Imagens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Imagem x= document.toObject(Imagem.class);
                                x.setId(document.getId());
                                Array_Images.add(x);
                                Log.d("SucessoDB", "Sucesso em Obter os Dados!");
                                s.append(document.getId() +" ->"+ x.toString());
                            }
                        } else{
                            Log.d("ErroDB", "Erro em Obter os Dados!");
                        }
                    }
                });
        //create_fead();
        create_map();
    }




    public void create_fead(){
        Fragment x = FeedFragment.newInstance();
        //Fragment x = FragmentA_list.newInstance(cars);
        //ft.replace(R.id.your_placeholder, Fragment_B_info.newInstance("Porto"));
        ft.replace(R.id.your_placeholder, x);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

    }
    public void create_map(){
        Fragment x = MapFragment.newInstance();
        //Fragment x = FragmentA_list.newInstance(cars);
        //ft.replace(R.id.your_placeholder, Fragment_B_info.newInstance("Porto"));
        ft.replace(R.id.your_placeholder, x);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public String getUsename(){
        return mUsername;
    }
}