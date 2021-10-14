package com.voxeldev.wristtheorist.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.R;
import com.voxeldev.wristtheorist.SubjectActivity;
import com.voxeldev.wristtheorist.databinding.ItemRecyclerViewBinding;
import com.voxeldev.wristtheorist.models.TheoristSubject;

import java.util.List;

public class SubjectsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<TheoristSubject> subjectList;

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecyclerViewBinding binding;

        public ViewHolder(ItemRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public SubjectsRecyclerViewAdapter(List<TheoristSubject> subjectList){
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerViewBinding binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()));
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new SubjectsRecyclerViewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = subjectList.get(position).name;
        Context context = holder.itemView.getContext();
        ((ViewHolder)holder).binding.recyclerViewTextView.setText((name == null || name.isEmpty()) ?
                context.getString(R.string.noName) : name);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SubjectActivity.class);
            intent.putExtra("subject", new Gson().toJson(subjectList.get(position)));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (subjectList == null) ? 0 : subjectList.size();
    }
}
