package com.aslam.androidcrudphp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.adapters.ShopListAdapter;
import com.aslam.androidcrudphp.databinding.FragmentShopBinding;
import com.aslam.androidcrudphp.models.Product;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment implements ShopListAdapter.ShopInterface {

    private static final String TAG = "ShopFragment";
    FragmentShopBinding fragmentShopBinding;
    private ShopListAdapter shopListAdapter;
    private ShopViewModel shopViewModel;
    private NavController navController;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentShopBinding = FragmentShopBinding.inflate(inflater, container, false);
        return fragmentShopBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shopListAdapter = new ShopListAdapter(this);
        fragmentShopBinding.shopRecyclerView.setAdapter(shopListAdapter);
        //fragmentShopBinding.shopRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        //fragmentShopBinding.shopRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL));
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                List<Product> customlist = new ArrayList<>();
                for(int i=0; i<products.size();i++){
                    if(products.get(i).getCid().equals(shopViewModel.getCategory().getValue().getId())){
                        customlist.add(products.get(i));
                        //System.out.println("pcid: "+products.get(i).getCid());
                        //System.out.println("cid: "+shopViewModel.getCategory().getValue().getId());
                        //System.out.println(products.get(i));
                    }
                }
                shopListAdapter.submitList(customlist);
                //System.out.println("custom: "+ customlist);

            }
        });

        fragmentShopBinding.setShopViewModel(shopViewModel);
        navController = Navigation.findNavController(view);

    }

    @Override
    public void addItem(Product product) {
        boolean isAdded = shopViewModel.addItemToCart(product);
        if (isAdded) {
            Snackbar.make(requireView(), product.getName() + " added to cart.", Snackbar.LENGTH_LONG)
                    .setAction("Checkout", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navController.navigate(R.id.action_shopFragment_to_cartFragment);
                        }
                    })
                    .show();
        } else {
            Snackbar.make(requireView(), "Already have the max quantity in cart.", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onItemClick(Product product) {
        shopViewModel.setProduct(product);
        navController.navigate(R.id.action_shopFragment_to_productDetailFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        //catg = shopViewModel.getCategory().getValue().getName();
        actionBar.setTitle(shopViewModel.getCategory().getValue().getName());
    }

}