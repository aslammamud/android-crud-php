package com.aslam.androidcrudphp.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CompanyItem {
    private String id;
    private String name;
    private String imageUrl;

    public CompanyItem() {
    }

    public CompanyItem(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "CompanyItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyItem that = (CompanyItem) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName()) &&
                getImageUrl().equals(that.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImageUrl());
    }


    public static DiffUtil.ItemCallback<CompanyItem> itemCallback = new DiffUtil.ItemCallback<CompanyItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CompanyItem oldItem, @NonNull CompanyItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CompanyItem oldItem, @NonNull CompanyItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}



