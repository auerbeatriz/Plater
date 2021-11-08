package com.example.plater.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.plater.fragments.MyFavoriteRecipesFragment;
import com.example.plater.fragments.MyRecipesFragment;

public class RBFragmentsAdapter extends FragmentStateAdapter {

    public RBFragmentsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MyFavoriteRecipesFragment();
        }
        return new MyRecipesFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
