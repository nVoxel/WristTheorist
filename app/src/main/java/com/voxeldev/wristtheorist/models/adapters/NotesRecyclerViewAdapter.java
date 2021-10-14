package com.voxeldev.wristtheorist.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.NoteActivity;
import com.voxeldev.wristtheorist.R;
import com.voxeldev.wristtheorist.databinding.ItemRecyclerViewBinding;
import com.voxeldev.wristtheorist.models.TheoristNote;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<TheoristNote> noteList;

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecyclerViewBinding binding;

        public ViewHolder(ItemRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public NotesRecyclerViewAdapter(List<TheoristNote> noteList){
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerViewBinding binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()));
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new NotesRecyclerViewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = noteList.get(position).title;
        Context context = holder.itemView.getContext();
        ((NotesRecyclerViewAdapter.ViewHolder)holder).binding.recyclerViewTextView.setText((title == null || title.isEmpty()) ?
                context.getString(R.string.noTitle) : title);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("note", new Gson().toJson(noteList.get(position)));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (noteList == null) ? 0 : noteList.size();
    }
}
