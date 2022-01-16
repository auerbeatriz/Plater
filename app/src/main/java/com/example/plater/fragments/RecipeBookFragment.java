package com.example.plater.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.RecipeAdapter;
import com.example.plater.models.RecipeBookViewModel;
import com.example.plater.utils.Config;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeBookFragment extends Fragment {

    public RecipeBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeBookFragment.
     */
    public static RecipeBookFragment newInstance() {
        RecipeBookFragment fragment = new RecipeBookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_book, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        RecyclerView rvRecipes = getView().findViewById(R.id.rvRecipeBook);
        rvRecipes.setLayoutManager(gridLayoutManager);

        final String username = Config.getLogin(getActivity());
        final String senha = Config.getPassword(getActivity());
        RecipeBookViewModel viewModel = new ViewModelProvider(getActivity()).get(RecipeBookViewModel.class);
        LiveData<List<Recipe>> favoriteRecipesList = viewModel.getFavoriteRecipes();
        favoriteRecipesList.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(), recipes);
                rvRecipes.setAdapter(recipeAdapter);
            }
        });

    }
}