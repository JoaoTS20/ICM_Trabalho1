package com.example.passadicosspot.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passadicosspot.R;

import java.util.LinkedList;

public class AnimalListAdapter extends
        RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>{
    private final LinkedList<String> mAnimalList;
    private LayoutInflater mInflater;

    public AnimalListAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mAnimalList = wordList;
    }
    @NonNull
    @Override
    public AnimalListAdapter.AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.animal_item,
                parent, false);
        return new AnimalViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalListAdapter.AnimalViewHolder holder, int position) {
        String mCurrent = mAnimalList.get(position);
        holder.animalItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mAnimalList.size();
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        public final TextView animalItemView;
        final AnimalListAdapter mAdapter;
        public AnimalViewHolder(View itemView, AnimalListAdapter adapter) {
            super(itemView);
            animalItemView = itemView.findViewById(R.id.txtView1Animal);
            this.mAdapter = adapter;
        }
    }
}
