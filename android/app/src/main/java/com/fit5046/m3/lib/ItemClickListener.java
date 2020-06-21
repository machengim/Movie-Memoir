package com.fit5046.m3.lib;

import android.view.View;

// Used for listening the click action on recycler view.
// Needs to be implemented on adapter class.
public interface ItemClickListener {
    void onItemClick(View view, int position);
}

