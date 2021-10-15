package com.example.plater;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterViewModel extends ViewModel {

    List<Integer> filterIconsList = new ArrayList<>();
    List<Integer> filterIconsSelectedList = new ArrayList<>();

    public FilterViewModel() {
        filterIconsList.add(R.drawable.ic_gluten_free);
        filterIconsList.add(R.drawable.ic_sugar_free);
        filterIconsList.add(R.drawable.ic_meat_free);
        filterIconsList.add(R.drawable.ic_eggs_free);
        filterIconsList.add(R.drawable.ic_nuts_free);

        filterIconsSelectedList.add(R.drawable.ic_gluten_free_selected);
        filterIconsSelectedList.add(R.drawable.ic_sugar_free_selected);
        filterIconsSelectedList.add(R.drawable.ic_meat_free_selected);
        filterIconsSelectedList.add(R.drawable.ic_eggs_free_selected);
        filterIconsSelectedList.add(R.drawable.ic_nuts_free_selected);
    }

    public List<Integer> getFilterIconsList() {
        return filterIconsList;
    }
    public List<Integer> getFilterIconsSelectedList() {
        return filterIconsSelectedList;
    }
}
