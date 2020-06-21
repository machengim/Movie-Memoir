package com.fit5046.m3.lib;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Used to control the row gap in recycler views.
public class VerticleItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public VerticleItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.bottom = space;
    }
}
