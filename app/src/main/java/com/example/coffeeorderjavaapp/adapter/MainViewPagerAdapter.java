package com.example.coffeeorderjavaapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coffeeorderjavaapp.TestSignOut;
import com.example.coffeeorderjavaapp.fragment.CartFragment;
import com.example.coffeeorderjavaapp.fragment.ProductListFragment;
import com.example.coffeeorderjavaapp.fragment.ProfileFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {
    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ProductListFragment();
            case 1:
                return new CartFragment();
            case 2:
                return new ProductListFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new ProductListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
