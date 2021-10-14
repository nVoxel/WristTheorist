package com.voxeldev.wristtheorist.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.BookActivity;
import com.voxeldev.wristtheorist.BookMenuActivity;
import com.voxeldev.wristtheorist.R;
import com.voxeldev.wristtheorist.databinding.ItemRecyclerViewBinding;
import com.voxeldev.wristtheorist.models.TheoristBook;

import java.util.List;

public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Activity parentActivity;
    private final List<TheoristBook> bookList;

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRecyclerViewBinding binding;

        public ViewHolder(ItemRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public BooksRecyclerViewAdapter(Activity parentActivity, List<TheoristBook> bookList){
        this.parentActivity = parentActivity;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerViewBinding binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()));
        binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = bookList.get(position).title;
        Context context = holder.itemView.getContext();
        ((ViewHolder)holder).binding.recyclerViewTextView.setText((title == null || title.isEmpty()) ?
                context.getString(R.string.noTitle) : title);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, BookActivity.class);
            intent.putExtra("book", new Gson().toJson(bookList.get(position)));
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(view -> {
            Intent intent = new Intent(context, BookMenuActivity.class);
            intent.putExtra("book", position);
            context.startActivity(intent);
            parentActivity.finish();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}