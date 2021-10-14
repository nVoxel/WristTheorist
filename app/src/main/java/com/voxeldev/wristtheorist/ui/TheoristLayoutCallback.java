package com.voxeldev.wristtheorist.ui;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;

public class TheoristLayoutCallback extends WearableLinearLayoutManager.LayoutCallback {

    private static final float MAX_ICON_PROGRESS = 0.65f;

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        float progressToCenter = Math.min(Math.abs(0.5f - yRelativeToCenterOffset), MAX_ICON_PROGRESS);

        child.setScaleX(1 - progressToCenter);
        child.setScaleY(1 - progressToCenter);
    }
}
