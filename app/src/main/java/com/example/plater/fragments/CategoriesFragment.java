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

import com.example.plater.Category;
import com.example.plater.R;
import com.example.plater.adapters.CategoryAdapter;
import com.example.plater.models.CategoriesFragmentViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
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
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        RecyclerView rvCategories = getView().findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(layoutManager);

        CategoriesFragmentViewModel viewModel = new ViewModelProvider(getActivity()).get(CategoriesFragmentViewModel.class);
        LiveData<List<Category>> categories = viewModel.getCategoriesList();
        categories.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);
                rvCategories.setAdapter(categoryAdapter);
            }
        });

    }
}