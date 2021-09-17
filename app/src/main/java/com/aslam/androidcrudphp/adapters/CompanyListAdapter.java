package com.aslam.androidcrudphp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aslam.androidcrudphp.databinding.CategoryRowBinding;
import com.aslam.androidcrudphp.databinding.HomeRowBinding;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.CompanyItem;

public class CompanyListAdapter extends ListAdapter<CompanyItem, CompanyListAdapter.CategoryViewHolder> {

    CompanyInterface companyInterface;
    public CompanyListAdapter(CompanyInterface homeInterface) {
        super(CompanyItem.itemCallback);
        this.companyInterface = homeInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HomeRowBinding homeRowBinding = HomeRowBinding.inflate(layoutInflater,parent,false);
        homeRowBinding.setCompanyInterface(companyInterface);

        return new CategoryViewHolder(homeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CompanyItem companyItem = getItem(position);

        holder.homeRowBinding.setCompany(companyItem);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        HomeRowBinding homeRowBinding;

        public CategoryViewHolder(HomeRowBinding binding) {
            super(binding.getRoot());
            this.homeRowBinding = binding;
        }
    }

    public interface CompanyInterface {
        void onItemClick(CompanyItem companyItem);
    }
}
