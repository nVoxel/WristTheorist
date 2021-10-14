package com.voxeldev.wristtheorist;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.wear.activity.ConfirmationActivity;

public abstract class ToolActivity extends FragmentActivity {

    public void confirmAndReturnAsync(boolean success){
        runOnUiThread(() -> showConfirmation(success));
        try {
            Thread.sleep(1200);
        } catch (Exception ignored) { }
        runOnUiThread(this::returnToMainActivity);
    }

    public void showConfirmation(boolean success){
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, (success) ?
                ConfirmationActivity.SUCCESS_ANIMATION : ConfirmationActivity.FAILURE_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, (success) ?
                getString(R.string.successful) : getString(R.string.failed));
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS, 1200);
        startActivity(intent);
    }

    public void returnToMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
