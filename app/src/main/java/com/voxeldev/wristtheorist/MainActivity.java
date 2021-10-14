package com.voxeldev.wristtheorist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.voxeldev.wristtheorist.databinding.ActivityMainBinding;
import com.voxeldev.wristtheorist.models.TheoristStorage;
import com.voxeldev.wristtheorist.models.adapters.BooksRecyclerViewAdapter;
import com.voxeldev.wristtheorist.ui.TheoristLayoutCallback;

public class MainActivity extends FragmentActivity {

    public static final String LOG_TAG = "TheoristException";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AmbientModeSupport.attach(this);

        WearableRecyclerView booksRecyclerView = binding.booksRecyclerView;
        booksRecyclerView.setEdgeItemsCenteringEnabled(true);
        booksRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this, new TheoristLayoutCallback()));

        try{
            TheoristStorage theoristStorage = new TheoristStorage(this);
            booksRecyclerView.setAdapter(new BooksRecyclerViewAdapter(this, theoristStorage.bookList));
        }
        catch (Exception e){
            Log.e(LOG_TAG, "Error while loading books: " + e.getMessage());
        }

        binding.addBookButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddBookActivity.class));
            finish();
        });

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1){
            binding.addBookButton.setOnLongClickListener(view -> {
                Toast.makeText(getApplicationContext(), getString(R.string.addBook), Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }
}