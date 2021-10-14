package com.voxeldev.wristtheorist;

import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.wear.ambient.AmbientModeSupport;

import com.google.gson.Gson;
import com.voxeldev.wristtheorist.databinding.ActivityNoteBinding;
import com.voxeldev.wristtheorist.models.TheoristNote;

public class NoteActivity extends FragmentActivity implements AmbientModeSupport.AmbientCallbackProvider {

    private ActivityNoteBinding binding;
    private BatteryManager batteryManager;
    private boolean isAmbient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try{
            TheoristNote theoristNote = new Gson().fromJson(getIntent().getStringExtra("note"), TheoristNote.class);
            binding.noteActivityNameTextView.setText((theoristNote.title == null ||
                    theoristNote.title.isEmpty()) ? getString(R.string.noTitle) : theoristNote.title);
            if (theoristNote.text != null){
                binding.noteTextView.setText(theoristNote.text);
            }
        }
        catch (Exception e){
            Log.e("TheoristError", "Error while loading note: " + e.getMessage());
            finish();
            return;
        }

        AmbientModeSupport.attach(this);

        batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        isAmbient = false;
        new Thread(this::updateBattery).start();
    }

    private void updateBattery(){
        while (!isAmbient){
            try {
                runOnUiThread(() -> binding.batteryTextView.setText(
                        String.format(getString(R.string.batteryPercentage),
                                batteryManager.getIntProperty(
                                        BatteryManager.BATTERY_PROPERTY_CAPACITY))));
                Thread.sleep(10000);
            } catch (Exception e) {
                Log.e("TheoristError", "Error while updating battery: " + e.getMessage());
            }
        }
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new AmbientModeSupport.AmbientCallback() {
            @Override
            public void onEnterAmbient(Bundle ambientDetails) {
                super.onEnterAmbient(ambientDetails);
                isAmbient = true;
            }

            @Override
            public void onUpdateAmbient() {
                super.onUpdateAmbient();
                runOnUiThread(() -> binding.batteryTextView.setText(
                        String.format(getString(R.string.batteryPercentage),
                                batteryManager.getIntProperty(
                                        BatteryManager.BATTERY_PROPERTY_CAPACITY))));
            }

            @Override
            public void onExitAmbient() {
                super.onExitAmbient();
                isAmbient = false;
                new Thread(NoteActivity.this::updateBattery).start();
            }
        };
    }
}
