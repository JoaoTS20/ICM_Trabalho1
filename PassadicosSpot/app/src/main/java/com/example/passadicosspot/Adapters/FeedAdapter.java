package com.example.passadicosspot.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passadicosspot.R;
import com.example.passadicosspot.classes.Imagem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FeedAdapter extends FirestoreRecyclerAdapter<Imagem, FeedAdapter.ViewHolder> {
    StorageReference x;
    private OnRecyclerItemClickListener clickListener;

    public interface OnRecyclerItemClickListener{
        void OnRecyclerItemClick(Imagem i);
    }

    public FeedAdapter(@NonNull FirestoreRecyclerOptions<Imagem> options) {
        super(options);
    }

    public FeedAdapter(@NonNull FirestoreRecyclerOptions<Imagem> options, OnRecyclerItemClickListener onclickrecycler) {
        super(options);
        clickListener = onclickrecycler;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Imagem model) {
        holder.user.setText(model.getUsername());
        holder.description.setText(model.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getSnapshots().getSnapshot(position).getId();
                Imagem i = model;
                i.setId(id);
                clickListener.OnRecyclerItemClick(i);
            }
        });
        x = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPhotoURL());
        x.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    String dowmloadUrl = task.getResult().toString();
                    Glide.with(holder.PhotoURL)
                            .load(dowmloadUrl)
                            .into(holder.PhotoURL);

                }else{
                    Log.w("ErroImagem", "Getting download url was not successful.",
                            task.getException());
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);

        return new ViewHolder(mItemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, description;
        ImageView PhotoURL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user= itemView.findViewById(R.id.user);
            description = itemView.findViewById(R.id.description);
            PhotoURL= itemView.findViewById(R.id.imageView);
        }
    }
}
