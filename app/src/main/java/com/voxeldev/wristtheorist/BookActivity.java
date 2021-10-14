package com.voxeldev.wristtheorist;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.databinding.ActivityBookBinding;
import com.voxeldev.wristtheorist.models.TheoristBook;
import com.voxeldev.wristtheorist.models.adapters.SubjectsRecyclerViewAdapter;
import com.voxeldev.wristtheorist.ui.TheoristLayoutCallback;

public class BookActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBookBinding binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AmbientModeSupport.attach(this);

        WearableRecyclerView subjectsRecyclerView = binding.subjectsRecyclerView;
        subjectsRecyclerView.setEdgeItemsCenteringEnabled(true);
        subjectsRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this, new TheoristLayoutCallback()));

        try{
            TheoristBook theoristBook = new Gson().fromJson(getIntent().getStringExtra("book"), TheoristBook.class);
            binding.bookActivityNameTextView.setText((theoristBook.title == null ||
                    theoristBook.title.isEmpty()) ? getString(R.string.noTitle) : theoristBook.title);
            subjectsRecyclerView.setAdapter(new SubjectsRecyclerViewAdapter(theoristBook.subjectList));
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while loading subjects: " + e.getMessage());
            finish();
        }
    }
}
