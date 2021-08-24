package com.aslam.androidcrudphp.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CategoryItem {
    private String id;
    private String name;
    private String imageUrl;
    private Boolean isAvailable;

    public CategoryItem(String id, String name, String imageUrl, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }

    public CategoryItem() {
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryItem that = (CategoryItem) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName()) &&
                getImageUrl().equals(that.getImageUrl()) &&
                getIsAvailable().equals(that.getIsAvailable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getImageUrl(), getIsAvailable());
    }

    public static DiffUtil.ItemCallback<CategoryItem> itemCallback = new DiffUtil.ItemCallback<CategoryItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryItem oldItem, @NonNull CategoryItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryItem oldItem, @NonNull CategoryItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}
