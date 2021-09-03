package com.example.plater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ScreenItem> mListScreen;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, container, false);

        ImageView imgSlide = layoutScreen.findViewById(R.id.imv_intro_image);
        TextView tvTitle = layoutScreen.findViewById(R.id.tv_intro_title);
        TextView tvDescription = layoutScreen.findViewById(R.id.tv_intro_description);

        tvTitle.setText(mListScreen.get(position).getTitle());
        tvDescription.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).screenImg);

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
