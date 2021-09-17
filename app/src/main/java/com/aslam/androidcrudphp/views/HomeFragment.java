package com.aslam.androidcrudphp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.adapters.CategoryListAdapter;
import com.aslam.androidcrudphp.adapters.CompanyListAdapter;
import com.aslam.androidcrudphp.databinding.FragmentHomeBinding;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.CompanyItem;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements CompanyListAdapter.CompanyInterface {
    FragmentHomeBinding fragmentHomeBinding;
    private CompanyListAdapter companyListAdapter;
    private ShopViewModel shopViewModel;
    private NavController navController;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        companyListAdapter = new CompanyListAdapter(this);
        fragmentHomeBinding.homeRecyclerView.setAdapter(companyListAdapter);
        fragmentHomeBinding.homeRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getCompanies().observe(getViewLifecycleOwner(), new Observer<List<CompanyItem>>() {
            @Override
            public void onChanged(List<CompanyItem> companyItems) {
                companyListAdapter.submitList(companyItems);
            }
        });

        navController = Navigation.findNavController(view);
    }

    @Override
    public void onItemClick(CompanyItem companyItem) {
        Log.d("companyItem", companyItem.toString());

        shopViewModel.setCompany(companyItem);
        navController.navigate(R.id.action_homeFragment_to_categoryFragment);
    }
}