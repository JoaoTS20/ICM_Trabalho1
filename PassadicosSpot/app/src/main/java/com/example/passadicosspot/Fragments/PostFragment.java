package com.example.passadicosspot.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.passadicosspot.R;
import com.example.passadicosspot.classes.Imagem;
import com.example.passadicosspot.classes.User;

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
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private Imagem mParam1;
    private Bitmap mParam2;
    private User mParam3;
    private EditText editText;
    private Button btnAdd;
    private Button btnConfirm;
    private Button btnCancel;
    private Button btnEdit;

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
    public static PostFragment newInstance(Imagem param1, Bitmap param2, User param3) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        args.putSerializable(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Imagem) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getParcelable(ARG_PARAM2);
            //Log.d("kekw","🤡"+mParam2.toString());
            mParam3 = (User) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //TODO: RecyclerView
        Log.d("kekw","🤡1"+mParam1.toString());
        Log.d("kekw","🤡2"+mParam2.toString());
        Log.d("kekw","🤡3"+mParam3.toString());
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ImageView imageView = view.findViewById(R.id.postImgView);
        TextView textViewAuthor = view.findViewById(R.id.txtViewName);
        TextView textViewDescription = view.findViewById(R.id.txtViewDesc);
        TextView textViewEspecialista = view.findViewById(R.id.txtViewEspecialista);
        editText = view.findViewById(R.id.editTxtAnimais);
        btnAdd = view.findViewById(R.id.btnAddAnimal);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);
        textViewAuthor.setText(textViewAuthor.getText().toString() + mParam1.getUsername());
        textViewDescription.setText(textViewDescription .getText().toString() + mParam1.getDescription());
        if (mParam1.getEspecialista().equals("")) {
            if(mParam3.getTipo().equals("especialista")){
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
                                //TODO: Adicionar ao RecyclerView
                            }
                        });
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO: Função de enviar os dados para lá
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editText.setVisibility(View.INVISIBLE);
                                btnAdd.setVisibility(View.INVISIBLE);
                                btnConfirm.setVisibility(View.INVISIBLE);
                                btnEdit.setVisibility(View.VISIBLE);
                                view.setVisibility(View.INVISIBLE);
                                //TODO: RecyclerView resetar
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
        imageView.setImageBitmap(mParam2);
        return view;
    }
}