package com.example.passadicosspot.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.passadicosspot.Adapters.AnimalListAdapter;
import com.example.passadicosspot.MainActivity;
import com.example.passadicosspot.MainActivity_Navigation;
import com.example.passadicosspot.R;
import com.example.passadicosspot.classes.Imagem;
import com.example.passadicosspot.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Imagem mParam1;
    private User mParam2;
    private EditText editText;
    private Button btnAdd;
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnEdit;
    private LinkedList<String> mAnimalList;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(Imagem param1, User param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Imagem) getArguments().getSerializable(ARG_PARAM1);
            //Log.d("kekw","🤡"+mParam2.toString());
            mParam2 = (User) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("kekw","🤡1"+mParam1.toString());
        Log.d("kekw","🤡2"+mParam2.toString());
        Log.d("kekw","🤡3"+ mParam2.toString());
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ImageView imageView = view.findViewById(R.id.postImgView);
        TextView textViewAuthor = view.findViewById(R.id.txtViewName);
        TextView textViewDescription = view.findViewById(R.id.txtViewDesc);
        TextView textViewEspecialista = view.findViewById(R.id.txtViewEspecialista);
        recyclerView = view.findViewById(R.id.rcviewAnimais);
        mAnimalList = new LinkedList<>(mParam1.getAnimaisIdentificados());
        recyclerView.setAdapter(new AnimalListAdapter(getContext(),mAnimalList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        editText = view.findViewById(R.id.editTxtAnimais);
        btnAdd = view.findViewById(R.id.btnAddAnimal);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        textViewAuthor.setText(textViewAuthor.getText().toString() + mParam1.getUsername());
        textViewDescription.setText(textViewDescription .getText().toString() + mParam1.getDescription());
        Log.d("kekw","😎"+ mParam2.getTipo());
        if (mParam1.getEspecialista().equals("") || mParam1.getEspecialista().equals(mParam2.getTipo())) {
            if(mParam2.getTipo().equals("Perito")){
                btnEdit = view.findViewById(R.id.btnEspecialista);
                btnEdit.setVisibility(View.VISIBLE);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.VISIBLE);
                        btnConfirm.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addAnimal();
                            }
                        });
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirmImagem();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelOperation();
                            }
                        });
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
        else{
            textViewEspecialista.setText(textViewEspecialista.getText().toString() + mParam1.getEspecialista());
        }
        StorageReference x = FirebaseStorage.getInstance().getReferenceFromUrl(mParam1.getPhotoURL());
        x.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    String downloadUrl = task.getResult().toString();
                    Glide.with(imageView)
                            .load(downloadUrl)
                            .into(imageView);

                }else{
                    Log.w("ErroImagem", "Getting download url was not successful.",
                            task.getException());
                }
            }
        });
        //imageView.setImageBitmap(mParam2);
        return view;
    }

    public void addAnimal(){
        Log.d("kekw", "onClick: "+editText.getText().toString());
        if (editText.getText().toString().equals("")){
            //TODO: fazer Toast
        }
        else {

            String novoAnimal = editText.getText().toString();
            int animalListSize = mAnimalList.size();

            mAnimalList.addLast(novoAnimal);

            recyclerView.getAdapter().notifyItemInserted(animalListSize);

            recyclerView.smoothScrollToPosition(animalListSize);

            editText.setText("");
        }
    }

    public void confirmImagem(){
        //TODO: Função de enviar os dados para lá
        if (new LinkedList<>(mParam1.getAnimaisIdentificados()).equals(mAnimalList)){
            Toast.makeText(getActivity(), "Animais Identificados iguais!", Toast.LENGTH_SHORT).show();
        }
        else {
            String username = mParam1.getUsername();
            String description = mParam1.getDescription();
            String especialista = mParam2.getUsername();
            GeoPoint location = mParam1.getLocation();
            Date date = mParam1.getDate();
            String photoURL =mParam1.getPhotoURL();
            ArrayList<String> animais = new ArrayList<>(mAnimalList);
            Imagem novaImagem = new Imagem(description, especialista, location, photoURL, username, animais, date);
            db.collection("Imagens").document(mParam1.getId()).update("animaisIdentificados",animais);
            db.collection("Imagens").document(mParam1.getId()).update("especialista",especialista);
            //TODO: Inserir nova Imagem com Query usando a antiga em mParam1

        }
    }

    public void cancelOperation(){
        editText.setVisibility(View.INVISIBLE);
        btnAdd.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        mAnimalList = new LinkedList<>(mParam1.getAnimaisIdentificados());
        recyclerView.setAdapter(new AnimalListAdapter(getContext(),mAnimalList));
        editText.setText("");
    }
}