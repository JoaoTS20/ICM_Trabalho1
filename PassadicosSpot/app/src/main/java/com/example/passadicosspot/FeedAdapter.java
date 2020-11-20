package com.example.passadicosspot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FeedAdapter extends FirestoreRecyclerAdapter<Imagem, FeedAdapter.ViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedAdapter(@NonNull FirestoreRecyclerOptions<Imagem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Imagem model) {
        holder.user.setText(model.getUsername());
        holder.description.setText(model.getDescription());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);

        return new ViewHolder(mItemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user, description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user= itemView.findViewById(R.id.user);
            description = itemView.findViewById(R.id.description);
        }
    }
}
