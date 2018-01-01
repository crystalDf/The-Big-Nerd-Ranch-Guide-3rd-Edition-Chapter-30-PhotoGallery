package com.star.photogallery;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class PhotoPageActivity extends SingleFragmentActivity {

    private PhotoPageFragment mPhotoPageFragment;

    public static Intent newIntent(Context packageContext, Uri photoPageUri) {
        Intent intent = new Intent(packageContext, PhotoPageActivity.class);
        intent.setData(photoPageUri);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mPhotoPageFragment = PhotoPageFragment.newInstance(getIntent().getData());
        return mPhotoPageFragment;
    }

    @Override
    public void onBackPressed() {

        if (mPhotoPageFragment.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }
}
