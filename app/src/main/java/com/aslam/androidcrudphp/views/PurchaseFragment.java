package com.aslam.androidcrudphp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aslam.androidcrudphp.R;
import com.aslam.androidcrudphp.adapters.PurchaseListAdapter;
import com.aslam.androidcrudphp.databinding.FragmentPurchaseBinding;
import com.aslam.androidcrudphp.models.PurchaseItem;
import com.aslam.androidcrudphp.viewmodels.ShopViewModel;

import java.util.List;

public class PurchaseFragment extends Fragment implements PurchaseListAdapter.PurchaseInterface{

    FragmentPurchaseBinding fragmentPurchaseBinding;
    private PurchaseListAdapter purchaseListAdapter;
    private ShopViewModel shopViewModel;
    private NavController navController;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPurchaseBinding = FragmentPurchaseBinding.inflate(inflater,container,false);

        return fragmentPurchaseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        purchaseListAdapter = new PurchaseListAdapter(this);
        fragmentPurchaseBinding.purchaseRecyclerView.setAdapter(purchaseListAdapter);
        fragmentPurchaseBinding.purchaseRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        shopViewModel = new ViewModelProvider(requireActivity()).get(ShopViewModel.class);
        shopViewModel.getPurchaseHistories().observe(getViewLifecycleOwner(), new Observer<List<PurchaseItem>>() {
            @Override
            public void onChanged(List<PurchaseItem> purchaseItems) {
                purchaseListAdapter.submitList(purchaseItems);
            }
        });

        navController = Navigation.findNavController(view);
    }

    @Override
    public void requestCancel(PurchaseItem purchaseItem) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setMessage("Do you really want to cancel this order?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //Toast.makeText(getContext(),"You clicked yes button",Toast.LENGTH_LONG).show();
                                shopViewModel.requestCancelOrderFromPurchase(purchaseItem);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(),"You clicked no button",Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(PurchaseItem purchaseItem) {

    }

}