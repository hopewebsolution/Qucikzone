package com.quikzon.add.restapi;

import com.quikzon.add.helper.ScrollViewExt;

public interface ScrollViewListener {
    void onScrollChanged(ScrollViewExt scrollView,
                         int x, int y, int oldx, int oldy);
}
