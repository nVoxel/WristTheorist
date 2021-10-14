package com.voxeldev.wristtheorist;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.databinding.ActivitySubjectBinding;
import com.voxeldev.wristtheorist.models.TheoristSubject;
import com.voxeldev.wristtheorist.models.adapters.NotesRecyclerViewAdapter;
import com.voxeldev.wristtheorist.ui.TheoristLayoutCallback;

public class SubjectActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySubjectBinding binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AmbientModeSupport.attach(this);

        WearableRecyclerView notesRecyclerView = binding.notesRecyclerView;
        notesRecyclerView.setEdgeItemsCenteringEnabled(true);
        notesRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this, new TheoristLayoutCallback()));

        try{
            TheoristSubject theoristSubject = new Gson().fromJson(getIntent().getStringExtra("subject"), TheoristSubject.class);
            binding.subjectActivityNameTextView.setText((theoristSubject.name == null ||
                    theoristSubject.name.isEmpty()) ? getString(R.string.noName) : theoristSubject.name);
            notesRecyclerView.setAdapter(new NotesRecyclerViewAdapter(theoristSubject.noteList));
        }
        catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "Error while loading notes: " + e.getMessage());
            finish();
        }
    }
}
