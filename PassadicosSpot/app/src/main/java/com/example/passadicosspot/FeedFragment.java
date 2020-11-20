package com.example.passadicosspot;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {


    // the fragment initialization parameters
    private static final String IMAGES_LIST = "list";
    private static final String DATABASE = "database";
    private static final String USERNAME = "username";

    //Data to use
    //private ArrayList<Imagem> image_list;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    //CollectionReference Images_ref= db.collection("Imagens");


    //private String username;



    //RecyclerView stuff
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FeedAdapter feedAdapter;


    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        //Bundle args = new Bundle();
        //args.putSerializable(IMAGES_LIST, image_list);
        //args.put(DATABASE,database );
        //args.putString(USERNAME, username);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            image_list = (ArrayList<Imagem>) getArguments().getSerializable(IMAGES_LIST);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        //
        recyclerView= view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Query query = FirebaseFirestore.getInstance().collection("Imagens");
        FirestoreRecyclerOptions<Imagem> options = new FirestoreRecyclerOptions.Builder<Imagem>().setQuery(query, Imagem.class).build();
        feedAdapter = new FeedAdapter(options);
        feedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(feedAdapter.getItemCount()-1);
            }
        });
        recyclerView.setAdapter(feedAdapter);
        return view;

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