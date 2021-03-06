package com.iitp.njack.iitp_connect.core.calendar.list;

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
import com.iitp.njack.iitp_connect.data.contest.Contest;
import com.iitp.njack.iitp_connect.data.network.Status;
import com.iitp.njack.iitp_connect.databinding.ActivityCodingCalendarBinding;
import com.iitp.njack.iitp_connect.ui.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import static com.iitp.njack.iitp_connect.core.calendar.detail.ContestDetailActivity.CONTEST_ID;
import static com.iitp.njack.iitp_connect.ui.ViewUtils.showView;

public class CodingCalendarActivity extends AppCompatActivity implements CodingCalendarView {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private CodingCalendarViewModel codingCalendarViewModel;
    private ActivityCodingCalendarBinding binding;
    private SwipeRefreshLayout refreshLayout;
    private CodingCalendarAdapter codingCalendarAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_coding_calendar);
        codingCalendarViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(CodingCalendarViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        codingCalendarViewModel.getSelectedContest().observe(this, this::openContestDetails);
        loadContests(false);

        setupRecyclerView();
        setupRefreshListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshLayout.setOnRefreshListener(null);
    }

    private void setupRecyclerView() {
        codingCalendarAdapter = new CodingCalendarAdapter(codingCalendarViewModel);

        RecyclerView recyclerView = binding.contestsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(codingCalendarAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadContests(boolean reload) {
        codingCalendarViewModel.loadContests(reload).observe(this, contestResource -> {
            if (contestResource != null) {
                if (contestResource.status == Status.SUCCESS) {
                    showResults(contestResource.data);
                    showProgress(false);
                } else if (contestResource.status == Status.ERROR) {
                    showError(contestResource.message);
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
            loadContests(true);
        });
    }

    @Override
    public void showResults(List<Contest> items) {
        codingCalendarAdapter.setContests(items);
    }

    @Override
    public void showEmptyView(boolean show) {
        ViewUtils.showView(binding.emptyView, show);
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
            ViewUtils.showSnackbar(binding.contestsRecyclerView, R.string.refresh_complete);
    }

    @Override
    public void openContestDetails(Long id) {
        Intent intent = new Intent(this, ContestDetailActivity.class);
        intent.putExtra(CONTEST_ID, id);
        startActivity(intent);
    }
}
