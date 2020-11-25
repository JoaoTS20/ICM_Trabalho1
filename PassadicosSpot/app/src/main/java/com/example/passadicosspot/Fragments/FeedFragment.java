package com.example.passadicosspot.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.passadicosspot.Adapters.FeedAdapter;
import com.example.passadicosspot.MainActivity_Navigation;
import com.example.passadicosspot.R;
import com.example.passadicosspot.classes.Imagem;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    private boolean justNoSpecialist = false;
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
        view.findViewById(R.id.floatingActionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirestoreRecyclerOptions<Imagem> options = null;
                if (justNoSpecialist) {
                    Query query = FirebaseFirestore.getInstance().collection("Imagens").whereNotEqualTo("username", ((MainActivity_Navigation) getActivity()).getUsername());
                    options = new FirestoreRecyclerOptions.Builder<Imagem>().setQuery(query, Imagem.class).build();
                }
                else {
                    Query query = FirebaseFirestore.getInstance().collection("Imagens")
                            .whereNotEqualTo("username", ((MainActivity_Navigation) getActivity()).getUsername())
                            .whereEqualTo("especialista","");
                    options = new FirestoreRecyclerOptions.Builder<Imagem>().setQuery(query, Imagem.class).build();

                }
                justNoSpecialist = !justNoSpecialist;
                feedAdapter = new FeedAdapter(options, new FeedAdapter.OnRecyclerItemClickListener() {
                    @Override
                    public void OnRecyclerItemClick(Imagem i) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("param1", i);
                        bundle.putSerializable("param2", ((MainActivity_Navigation) getActivity()).getUser());
                        Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_feedFragment_to_postFragment, bundle);

                    }});
            }
        });
        recyclerView= view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Query query = FirebaseFirestore.getInstance().collection("Imagens").whereNotEqualTo("username",((MainActivity_Navigation)getActivity()).getUsername());
        FirestoreRecyclerOptions<Imagem> options = new FirestoreRecyclerOptions.Builder<Imagem>().setQuery(query, Imagem.class).build();
        feedAdapter = new FeedAdapter(options, new FeedAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnRecyclerItemClick(Imagem i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("param1", i);
                bundle.putSerializable("param2", ((MainActivity_Navigation) getActivity()).getUser());
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_feedFragment_to_postFragment, bundle);

            }
        });
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