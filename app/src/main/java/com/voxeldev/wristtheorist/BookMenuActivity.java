package com.voxeldev.wristtheorist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.wear.ambient.AmbientModeSupport;

import com.voxeldev.wristtheorist.databinding.ActivityBookMenuBinding;
import com.voxeldev.wristtheorist.models.TheoristBook;
import com.voxeldev.wristtheorist.models.TheoristStorage;

public class BookMenuActivity extends ToolActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBookMenuBinding binding = ActivityBookMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AmbientModeSupport.attach(this);

        try{
            int bookIndex = getIntent().getIntExtra("book", -1);

            TheoristStorage storage = new TheoristStorage(this);

            if (bookIndex < 0 || bookIndex > storage.bookList.size()){
                throw new Exception("Incorrect bookIndex");
            }

            TheoristBook book = storage.bookList.get(bookIndex);

            binding.bookMenuActivityNameTextView.setText((book.title == null ||
                    book.title.isEmpty()) ? getString(R.string.noTitle) : book.title);

            binding.refreshBookLayout.setOnClickListener(view -> {
                binding.bookMenuScrollView.setVisibility(View.GONE);
                binding.bookMenuLoader.setVisibility(View.VISIBLE);
                new Thread(() ->
                        confirmAndReturnAsync(storage.refreshBook(bookIndex))).start();
            });

            binding.deleteBookLayout.setOnClickListener(view -> new Thread(() ->
                    confirmAndReturnAsync(storage.deleteBook(bookIndex))).start());

            binding.returnLayout.setOnClickListener(view -> returnToMainActivity());
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while loading book menu: " + e.getMessage());
            returnToMainActivity();
        }
    }
}
