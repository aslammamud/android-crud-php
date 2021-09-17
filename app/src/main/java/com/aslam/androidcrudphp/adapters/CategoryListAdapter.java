package com.aslam.androidcrudphp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aslam.androidcrudphp.databinding.CategoryRowBinding;
import com.aslam.androidcrudphp.databinding.HomeRowBinding;
import com.aslam.androidcrudphp.models.CategoryItem;

public class CategoryListAdapter extends ListAdapter<CategoryItem, CategoryListAdapter.CategoryViewHolder> {

    CategoryInterface categoryInterface;
    public CategoryListAdapter(CategoryInterface homeInterface) {
        super(CategoryItem.itemCallback);
        this.categoryInterface = homeInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CategoryRowBinding categoryRowBinding = CategoryRowBinding.inflate(layoutInflater,parent,false);
        categoryRowBinding.setCategoryInterface(categoryInterface);

        return new CategoryViewHolder(categoryRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem categoryItem = getItem(position);

        holder.categoryRowBinding.setCategory(categoryItem);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        CategoryRowBinding categoryRowBinding;

        public CategoryViewHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.categoryRowBinding = binding;
        }
    }

    public interface CategoryInterface {
        void onItemClick(CategoryItem categoryItem);
    }
}
