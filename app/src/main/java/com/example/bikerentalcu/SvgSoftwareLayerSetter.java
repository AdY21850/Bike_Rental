package com.example.bikerentalcu;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class SvgSoftwareLayerSetter {

    public static void loadSvgImage(@NonNull Context context, @NonNull String imageUrl, @NonNull ImageView imageView) {
        Uri uri = Uri.parse(imageUrl);

        RequestBuilder<PictureDrawable> requestBuilder = Glide.with(context)
                .as(PictureDrawable.class)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().centerCrop());

        requestBuilder.load(uri).into(imageView);
    }
}
