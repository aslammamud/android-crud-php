package com.aslam.androidcrudphp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aslam.androidcrudphp.databinding.PurchaseRowBinding;
import com.aslam.androidcrudphp.models.PurchaseItem;

public class PurchaseListAdapter extends ListAdapter<PurchaseItem, PurchaseListAdapter.PurchaseViewHolder> {

    PurchaseInterface purchaseInterface;
    public PurchaseListAdapter(PurchaseInterface purchaseInterface) {
        super(PurchaseItem.itemCallback);
        this.purchaseInterface = purchaseInterface;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PurchaseRowBinding purchaseRowBinding = PurchaseRowBinding.inflate(layoutInflater,parent,false);
        purchaseRowBinding.setPurchaseInterface(purchaseInterface);

        return new PurchaseViewHolder(purchaseRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        PurchaseItem purchaseItem = getItem(position);

        holder.purchaseRowBinding.setShipment(purchaseItem);
    }

    class PurchaseViewHolder extends RecyclerView.ViewHolder{

        PurchaseRowBinding purchaseRowBinding;

        public PurchaseViewHolder(PurchaseRowBinding binding) {
            super(binding.getRoot());
            this.purchaseRowBinding = binding;

            purchaseRowBinding.cancelOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    purchaseInterface.requestCancel(getItem(getAdapterPosition()));
                }
            });
        }
    }

    public interface PurchaseInterface {
        void requestCancel(PurchaseItem purchaseItem);
        void onItemClick(PurchaseItem purchaseItem);
    }
}
