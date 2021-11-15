package com.example.plater.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plater.R;
import com.example.plater.RecipeAdapter;
import com.example.plater.Recipe;
import com.example.plater.models.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment {

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyRecipesFragment.
     */
    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
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
        return inflater.inflate(R.layout.fragment_my_recipes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivityViewModel viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        List<Recipe> recipeList = (List<Recipe>) viewModel.getRecipeList();
        RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipeList);

        float w = getResources().getDimension(R.dimen.recipe_item_width);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        RecyclerView rvRecipes = getView().findViewById(R.id.rvMyRecipes);
        rvRecipes.setAdapter(recipeAdapter);
        rvRecipes.setLayoutManager(gridLayoutManager);
    }

}