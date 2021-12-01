package com.example.plater.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plater.R;
import com.example.plater.Recipe;
import com.example.plater.adapters.FilterAdapter;
import com.example.plater.adapters.RecipeAdapter;
import com.example.plater.models.FilterViewModel;
import com.example.plater.models.MainActivityViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //  exibindo a área de filtros -> aqui pegamos as listas de filtros disponiveis
        FilterViewModel filterViewModel = new ViewModelProvider(getActivity()).get(FilterViewModel.class);
        List<Integer> filterIconsList = filterViewModel.getFilterIconsList();
        List<Integer> filterIconsSelectedList = filterViewModel.getFilterIconsSelectedList();
        FilterAdapter filterAdapter = new FilterAdapter(getContext(), filterIconsList, filterIconsSelectedList);

        //  setando recyclerview de filtros
        RecyclerView rvFilters = getView().findViewById(R.id.rv_filtersHome);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFilters.setLayoutManager(linearLayoutManager);
        rvFilters.setAdapter(filterAdapter);

        //  setando o recyclerview de receitas
        float w = getResources().getDimension(R.dimen.recipe_item_width);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        RecyclerView rvRecipes = getView().findViewById(R.id.rv_home);
        rvRecipes.setLayoutManager(gridLayoutManager);

        //  exibindo as receitas
            //  pegar as receitas deve estar em MainViewModel
        MainActivityViewModel viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        LiveData<List<Recipe>> recipeList = viewModel.getRecipeList();
        recipeList.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), recipes);
                rvRecipes.setAdapter(recipeAdapter);
            }
        });
    }
}