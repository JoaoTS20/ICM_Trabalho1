package com.example.passadicosspot.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

public class PostFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    }

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
            mParam2 = (User) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("PostFragment-Imagem",mParam1.toString());
        Log.d("PostFragment-User",mParam2.toString());

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ImageView imageView = view.findViewById(R.id.postImgView);
        TextView textViewAuthor = view.findViewById(R.id.txtViewName);
        TextView textViewDescription = view.findViewById(R.id.txtViewDesc);
        TextView textViewEspecialista = view.findViewById(R.id.txtViewEspecialista);

        recyclerView = view.findViewById(R.id.rcviewAnimais);
        editText = view.findViewById(R.id.editTxtAnimais);
        btnAdd = view.findViewById(R.id.btnAddAnimal);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);

        mAnimalList = new LinkedList<>(mParam1.getAnimaisIdentificados());
        recyclerView.setAdapter(new AnimalListAdapter(getContext(),mAnimalList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        textViewAuthor.setText(textViewAuthor.getText().toString() + mParam1.getUsername());
        textViewDescription.setText(textViewDescription .getText().toString() + mParam1.getDescription());
        textViewEspecialista.setText(textViewEspecialista.getText().toString() + mParam1.getEspecialista());

        Log.d("PostFragment-TipoUser",mParam2.getTipo());

        if (mParam1.getEspecialista().equals("") || mParam1.getEspecialista().equals(mParam2.getUsername())) {
            if(mParam2.getTipo().equals("Perito")){
                btnEdit = view.findViewById(R.id.btnEspecialista);
                btnEdit.setVisibility(View.VISIBLE);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editUnlocking();
                    }
                });
            }
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

    // Adicionar um animal ao RecyclerView
    // Se o EditText não tiver texto lança um Toast
    private void addAnimal(){
        Log.d("PostFragment", "onClick: "+editText.getText().toString());
        if (editText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Não inseriu nenhum nome de animal", Toast.LENGTH_SHORT).show();
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

    // Confirma as alterações na "Imagem"
    // Se a lista no RecyclerView não se alterar lança um Toast
    private void confirmImagem(){
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

            Log.d("PostFragment-User",mParam1.toString());

            db.collection("Imagens").document(mParam1.getId()).update("animaisIdentificados",animais);
            db.collection("Imagens").document(mParam1.getId()).update("especialista",especialista);
            Toast.makeText(getContext(),"A sua alteração foi feita com sucesso",Toast.LENGTH_LONG);
            Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.action_postFragment_to_mapFragment);
        }
    }
    // Cancela a operação de editar o post
    // Faz rollback das mudanças na lista de animais
    private void cancelOperation(){
        editText.setVisibility(View.INVISIBLE);
        btnAdd.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        mAnimalList = new LinkedList<>(mParam1.getAnimaisIdentificados());
        recyclerView.setAdapter(new AnimalListAdapter(getContext(),mAnimalList));
        editText.setText("");
    }

    // Coloca visíveis todos os botões e EditText para editar o Post
    // Regista todos os OnClickListeners
    private void editUnlocking(){
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
        btnEdit.setVisibility(View.INVISIBLE);
    }
}