package com.aslam.androidcrudphp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aslam.androidcrudphp.databinding.HomeRowBinding;
import com.aslam.androidcrudphp.models.CategoryItem;

public class CategoryListAdapter extends ListAdapter<CategoryItem, CategoryListAdapter.CategoryViewHolder> {

    HomeInterface homeInterface;
    public CategoryListAdapter(HomeInterface homeInterface) {
        super(CategoryItem.itemCallback);
        this.homeInterface = homeInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HomeRowBinding homeRowBinding = HomeRowBinding.inflate(layoutInflater,parent,false);
        homeRowBinding.setHomeInterface(homeInterface);

        return new CategoryViewHolder(homeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem categoryItem = getItem(position);

        holder.homeRowBinding.setCategory(categoryItem);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        HomeRowBinding homeRowBinding;

        public CategoryViewHolder(HomeRowBinding binding) {
            super(binding.getRoot());
            this.homeRowBinding = binding;
        }
    }

    public interface HomeInterface {
        void onItemClick(CategoryItem categoryItem);
    }
}
