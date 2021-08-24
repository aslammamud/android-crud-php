package com.aslam.androidcrudphp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aslam.androidcrudphp.models.CartItem;
import com.aslam.androidcrudphp.models.CategoryItem;
import com.aslam.androidcrudphp.models.Product;
import com.aslam.androidcrudphp.models.PurchaseItem;
import com.aslam.androidcrudphp.repositories.CartRepo;
import com.aslam.androidcrudphp.repositories.CategoryRepo;
import com.aslam.androidcrudphp.repositories.PurchaseRepo;
import com.aslam.androidcrudphp.repositories.ShopRepo;

import java.util.List;

public class ShopViewModel extends ViewModel {

    ShopRepo shopRepo = new ShopRepo();
    CartRepo cartRepo = new CartRepo();
    CategoryRepo categoryRepo = new CategoryRepo();
    PurchaseRepo purchaseRepo = new PurchaseRepo();

    MutableLiveData<Product> mutableProduct = new MutableLiveData<>();
    MutableLiveData<CategoryItem> mutableCategory = new MutableLiveData<>();
    MutableLiveData<PurchaseItem> mutablePurchase = new MutableLiveData<>();

    public LiveData<List<PurchaseItem>> getPurchaseHistories(){
        return purchaseRepo.getPurchaseHistories();
    }

    public void setPurchase(PurchaseItem purchaseItem){
        mutablePurchase.setValue(purchaseItem);
    }

    public LiveData<PurchaseItem> getPurchase(){
        return mutablePurchase;
    }

    public LiveData<List<CategoryItem>> getCategories(){
        return categoryRepo.getCategories();
    }

    public void setCategory(CategoryItem categoryItem){
        mutableCategory.setValue(categoryItem);
    }

    public LiveData<CategoryItem> getCategory(){
        return mutableCategory;
    }

    public LiveData<List<Product>> getProducts() {
        return shopRepo.getProducts();
    }

    public void setProduct(Product product) {
        mutableProduct.setValue(product);
    }

    public LiveData<Product> getProduct() {
        return mutableProduct;
    }

    public LiveData<List<CartItem>> getCart() {
        return cartRepo.getCart();
    }

    public boolean addItemToCart(Product product) {
        return cartRepo.addItemToCart(product);
    }

    public void removeItemFromCart(CartItem cartItem) {
        cartRepo.removeItemFromCart(cartItem);
    }

    public void requestCancelOrderFromPurchase(PurchaseItem purchaseItem) {
        PurchaseRepo.requestCancelOrderFromPurchase(purchaseItem);
    }

    public void changeQuantity(CartItem cartItem, int quantity) {
        cartRepo.changeQuantity(cartItem, quantity);
    }

    public LiveData<Double> getTotalPrice() {
        return cartRepo.getTotalPrice();
    }

    public void resetCart() {
        cartRepo.initCart();
    }

    public void reloadPurchaseHistory(){
        purchaseRepo.reloadPurchaseHistory();
    }

}
