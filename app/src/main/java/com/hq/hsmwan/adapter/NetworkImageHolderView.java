package com.hq.hsmwan.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 图片轮播适配器
 */
public class NetworkImageHolderView implements Holder<String> {
    private SimpleDraweeView imageView;

    @Override
    public View createView(Context context) {
        return imageView = new SimpleDraweeView(context);
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageURI(Uri.parse(data));
    }
}
