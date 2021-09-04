package com.example.plater;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    // TODO: Rename and change types and number of parameters
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
}