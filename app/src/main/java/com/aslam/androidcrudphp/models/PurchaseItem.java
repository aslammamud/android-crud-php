package com.aslam.androidcrudphp.models;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class PurchaseItem {
    private String id;
    private String token;
    private String price;
    private Boolean status;
    private Boolean destroyed;
    private String date;

    public PurchaseItem() {
    }

    public PurchaseItem(String id, String token, String price, Boolean status, Boolean destroyed, String date) {
        this.id = id;
        this.token = token;
        this.price = price;
        this.status = status;
        this.destroyed = destroyed;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getDestroyed() {
        return destroyed;
    }

    public void setDestroyed(Boolean destroyed) {
        this.destroyed = destroyed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static DiffUtil.ItemCallback<PurchaseItem> getItemCallback() {
        return itemCallback;
    }

    public static void setItemCallback(DiffUtil.ItemCallback<PurchaseItem> itemCallback) {
        PurchaseItem.itemCallback = itemCallback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseItem that = (PurchaseItem) o;
        return getId().equals(that.getId()) &&
                getToken().equals(that.getToken()) &&
                getPrice().equals(that.getPrice()) &&
                getStatus().equals(that.getStatus()) &&
                getDestroyed().equals(that.getDestroyed()) &&
                getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getPrice(), getStatus(), getDestroyed(), getDate());
    }

    public static DiffUtil.ItemCallback<PurchaseItem> itemCallback = new DiffUtil.ItemCallback<PurchaseItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull PurchaseItem oldItem, @NonNull PurchaseItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PurchaseItem oldItem, @NonNull PurchaseItem newItem) {
            return oldItem.equals(newItem);
        }
    };

}
