package com.example.passadicosspot.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.passadicosspot.Adapters.FeedAdapter;
import com.example.passadicosspot.MainActivity_Navigation;
import com.example.passadicosspot.R;
import com.example.passadicosspot.SignInActivity;
import com.example.passadicosspot.classes.Imagem;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    private ImageView logout;
    private ImageButton user_profile_photo;
    private TextView user_profile_name;
    private TextView user_type;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();




    //RecyclerView stuff
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FeedAdapter feedAdapter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //
        user_profile_name = view.findViewById(R.id.user_profile_name);
        user_profile_name.setText(((MainActivity_Navigation)getActivity()).getUsername());
        //
        user_profile_photo= view.findViewById(R.id.user_profile_photo);
        FirebaseUser User = ((MainActivity_Navigation)getActivity()).getmFirebaseUser();
        if (User.getPhotoUrl() == null) {
            Glide.with(view).load(R.drawable.ic_baseline_account_circle_24).into(user_profile_photo);
        } else {
            Glide.with(view)
                    .load(User.getPhotoUrl())
                    .into(user_profile_photo);
        }
        //
        logout = view.findViewById(R.id.logoutIcon);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logout();
            }
        });
        //
        user_type=view.findViewById(R.id.user_type);
        user_type.setText(((MainActivity_Navigation)getActivity()).getTypeUser());
        //
        recyclerView= view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Query query = FirebaseFirestore.getInstance().collection("Imagens").whereEqualTo("username",((MainActivity_Navigation)getActivity()).getUsername());
        FirestoreRecyclerOptions<Imagem> options = new FirestoreRecyclerOptions.Builder<Imagem>().setQuery(query, Imagem.class).build();
        feedAdapter = new FeedAdapter(options, new FeedAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnRecyclerItemClick(Imagem i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("param1", i);
                bundle.putSerializable("param2", ((MainActivity_Navigation) getActivity()).getUser());
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_profileFragment_to_postFragment, bundle);

            }
        });
        feedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(feedAdapter.getItemCount()-1);
            }
        });
        recyclerView.setAdapter(feedAdapter);
        //
        return  view;
    }


    private void logout(){
        ((MainActivity_Navigation)getActivity()).getAuth().signOut();
        //mFirebaseAuth.signOut();
        ((MainActivity_Navigation)getActivity()).getClient().signOut();
        //((MainActivity_Navigation)getActivity()).getUsename()= "anonymous" ;
        startActivity(new Intent(this.getContext(), SignInActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();
        feedAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedAdapter.stopListening();
    }

}
