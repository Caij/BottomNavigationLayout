package com.caij.nav;

import android.widget.ImageView;

public interface ImageLoader {

    void loadTabImage(ImageView imageView, String url);

    void loadSelectTabImage(ImageView ivIcon, String url);
}
