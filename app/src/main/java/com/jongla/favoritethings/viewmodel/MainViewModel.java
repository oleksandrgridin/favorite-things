package com.jongla.favoritethings.viewmodel;

import android.databinding.ObservableInt;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jongla.favoritethings.BrowsableFragmentPagerAdapter;
import com.jongla.favoritethings.view.BrowsableFragment;

import rx.Subscription;

/**
 * View model for the MainActivity
 */
public class MainViewModel implements ViewModel, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainViewModel";
    private int currentPage = 0;
    BrowsableFragmentPagerAdapter adapter;

    public ObservableInt searchButtonVisibility;
    public ObservableInt favoriteVisibility;
    public ObservableInt pickerVisibility;

    private Subscription subscription;
    private String editTextUsernameValue;

    public MainViewModel() {
        favoriteVisibility = new ObservableInt(View.VISIBLE);
        pickerVisibility = new ObservableInt(View.GONE);
        searchButtonVisibility = new ObservableInt(View.GONE);
    }

    public void setupViewPager(ViewPager viewPager, FragmentManager fragmentManager) {
        adapter = new BrowsableFragmentPagerAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void destroy() {
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        subscription = null;
    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String inputQuery = view.getText().toString();
            if (inputQuery.length() > 0) doQueryAction(inputQuery);
            return true;
        }
        return false;
    }

    public void onClickSearch(View view) {
        doQueryAction(editTextUsernameValue);
    }

    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editTextUsernameValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void doQueryAction(String reponame) {
        ((BrowsableFragment)adapter.getItem(currentPage)).getModel().loadFavoriteThings(reponame);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        if (position == 0) {
            pickerVisibility.set(View.GONE);
            favoriteVisibility.set(View.VISIBLE);
        } else {
            pickerVisibility.set(View.VISIBLE);
            favoriteVisibility.set(View.GONE);
        }
        searchButtonVisibility.set(View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
