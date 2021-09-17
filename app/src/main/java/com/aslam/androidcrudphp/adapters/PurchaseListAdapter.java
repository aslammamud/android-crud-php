package com.aslam.androidcrudphp.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aslam.androidcrudphp.databinding.PurchaseRowBinding;
import com.aslam.androidcrudphp.models.PurchaseItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

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

        if (!purchaseItem.getStatus() && purchaseItem.getDestroyed()){
            holder.purchaseRowBinding.orderStatus.setText("Order Canceled");
            holder.purchaseRowBinding.orderStatus.setTextColor(Color.parseColor("#ff0000"));

            holder.purchaseRowBinding.cancelOrderButton.setEnabled(false);
            holder.purchaseRowBinding.cancelOrderButton.setTextColor(Color.parseColor("#6F6A6566"));
        }else if(purchaseItem.getStatus()){
            holder.purchaseRowBinding.cancelOrderButton.setEnabled(false);
            holder.purchaseRowBinding.cancelOrderButton.setTextColor(Color.parseColor("#6F6A6566"));
            holder.purchaseRowBinding.orderStatus.setText("Order Delivered");
            holder.purchaseRowBinding.orderStatus.setTextColor(Color.parseColor("#00C853"));
        }


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
