package com.iitp.njack.iitp_connect.core.fb.feed;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.iitp.njack.iitp_connect.R;
import com.iitp.njack.iitp_connect.core.calendar.detail.ContestDetailActivity;
import com.iitp.njack.iitp_connect.data.facebook.FacebookPost;
import com.iitp.njack.iitp_connect.data.network.Status;
import com.iitp.njack.iitp_connect.databinding.ActivityFacebookFeedBinding;
import com.iitp.njack.iitp_connect.ui.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import static com.iitp.njack.iitp_connect.ui.ViewUtils.showView;

public class FacebookFeedActivity extends AppCompatActivity implements FacebookFeedView {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private FacebookFeedViewModel facebookFeedViewModel;
    private ActivityFacebookFeedBinding binding;
    private SwipeRefreshLayout refreshLayout;
    private FacebookFeedAdapter facebookFeedAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook_feed);
        facebookFeedViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FacebookFeedViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        facebookFeedViewModel.getSelectedFacebookPost().observe(this, this::openFacebookPostDetails);
        loadFacebookPosts(false);

        setupRecyclerView();
        setupRefreshListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshLayout.setOnRefreshListener(null);
    }

    private void setupRecyclerView() {
        facebookFeedAdapter = new FacebookFeedAdapter(facebookFeedViewModel);

        RecyclerView recyclerView = binding.facebookPostsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(facebookFeedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadFacebookPosts(boolean reload) {
        facebookFeedViewModel.loadFacebookPosts(reload).observe(this, facebookPostResource -> {
            if (facebookPostResource != null) {
                if (facebookPostResource.status == Status.SUCCESS) {
                    showResults(facebookPostResource.data);
                    showProgress(false);
                } else if (facebookPostResource.status == Status.ERROR) {
                    showError(facebookPostResource.message);
                    showProgress(false);
                } else {
                    showProgress(true);
                }
            } else {
                showProgress(true);
            }
        });
    }

    private void setupRefreshListener() {
        refreshLayout = binding.swipeContainer;
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            loadFacebookPosts(true);
        });
    }

    @Override
    public void showResults(List<FacebookPost> items) {
        facebookFeedAdapter.setFacebookPosts(items);
    }

    @Override
    public void showEmptyView(boolean show) {
        showView(binding.emptyView, show);
    }

    @Override
    public void showError(String error) {
        ViewUtils.showSnackbar(binding.getRoot(), error);
    }

    @Override
    public void showProgress(boolean show) {
        showView(binding.progressBar, show);
    }

    @Override
    public void onRefreshComplete(boolean success) {
        if (success)
            ViewUtils.showSnackbar(binding.facebookPostsRecyclerView, R.string.refresh_complete);
    }

    @Override
    public void openFacebookPostDetails(Long id) {
        Intent intent = new Intent(this, ContestDetailActivity.class);
        //intent.putExtra(CONTEST_ID, id);
      //  startActivity(intent);
    }
}
