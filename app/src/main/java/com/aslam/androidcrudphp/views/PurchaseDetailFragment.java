package com.aslam.androidcrudphp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aslam.androidcrudphp.adapters.PurchaseListAdapter;
import com.aslam.androidcrudphp.adapters.PurchasedItemsListAdapter;
import com.aslam.androidcrudphp.databinding.FragmentPurchaseDetailBinding;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.PurchaseItem;
import com.aslam.androidcrudphp.models.PurchasedProductItem;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PurchaseDetailFragment extends Fragment implements PurchasedItemsListAdapter.PurchasedItemsInterface{
    private PurchasedProductItem item;
    FragmentPurchaseDetailBinding fragmentPurchaseDetailBinding;
    private ShopViewModel shopViewModel;
    private PurchasedItemsListAdapter adapter;

    public PurchaseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPurchaseDetailBinding = FragmentPurchaseDetailBinding.inflate(inflater, container, false);
        return fragmentPurchaseDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PurchasedItemsListAdapter(this);
        fragmentPurchaseDetailBinding.purchasedetailsRecyclerView.setAdapter(adapter);

        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        String token = shopViewModel.getPurchase().getValue().getToken();

        shopViewModel.reloadPurchasedItemsHistory(token);
        shopViewModel.getPurchasedItemsHistories(token).observe(getViewLifecycleOwner(), new Observer<List<PurchasedProductItem>>() {
            @Override
            public void onChanged(List<PurchasedProductItem> purchasedProductItems) {

                List<PurchasedProductItem> items = new ArrayList<>(purchasedProductItems);

                adapter.submitList(items);

            }
        });
        fragmentPurchaseDetailBinding.purchasedetailsRecyclerView.setAdapter(adapter);
        fragmentPurchaseDetailBinding.setPurchaseViewModel(shopViewModel);
    }

}