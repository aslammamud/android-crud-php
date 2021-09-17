package com.aslam.androidcrudphp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.adapters.CategoryListAdapter;
import com.aslam.androidcrudphp.databinding.FragmentCategoryBinding;
import com.aslam.androidcrudphp.databinding.FragmentHomeBinding;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.Product;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryListAdapter.CategoryInterface {
    FragmentCategoryBinding fragmentCategoryBinding;
    private CategoryListAdapter categoryListAdapter;
    private ShopViewModel shopViewModel;
    private NavController navController;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater,container,false);

        return fragmentCategoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryListAdapter = new CategoryListAdapter(this);
        fragmentCategoryBinding.categoryRecyclerView.setAdapter(categoryListAdapter);

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                List<CategoryItem> customlist = new ArrayList<>();
                for(int i=0; i<categoryItems.size();i++){
                    if(categoryItems.get(i).getCompany_id().equals(shopViewModel.getCompany().getValue().getId())){
                        customlist.add(categoryItems.get(i));
                    }
                }

                categoryListAdapter.submitList(customlist);
            }
        });
        fragmentCategoryBinding.setCatViewModel(shopViewModel);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onItemClick(CategoryItem categoryItem) {
        Log.d("categoryItem", categoryItem.toString());

        shopViewModel.setCategory(categoryItem);
        navController.navigate(R.id.action_categoryFragment_to_shopFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(shopViewModel.getCompany().getValue().getName());
    }
}