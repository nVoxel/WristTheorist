package com.voxeldev.wristtheorist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.wear.ambient.AmbientModeSupport;

import com.voxeldev.wristtheorist.databinding.ActivityAddBookBinding;
import com.voxeldev.wristtheorist.models.TheoristStorage;

public class AddBookActivity extends ToolActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddBookBinding binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AmbientModeSupport.attach(this);

        binding.addBookButton.setOnClickListener(view -> {
            if (binding.addBookEditText.getText().toString().isEmpty()){
                binding.addBookEditText.setError(getString(R.string.EmptyUrl));
                return;
            }

            binding.addBookScrollView.setVisibility(View.GONE);
            binding.addBookLoader.setVisibility(View.VISIBLE);
            new Thread(() -> confirmAndReturnAsync(
                    new TheoristStorage(this)
                            .addBook(binding.addBookEditText.getText().toString()
                                    .replace("!", "/")
                                    .replace(",", ":")))).start();
        });

        binding.returnButton.setOnClickListener(view -> returnToMainActivity());
    }
}
