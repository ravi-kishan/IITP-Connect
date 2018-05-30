package com.iitp.njack.iitp_connect.common.di.modules;

import com.iitp.njack.iitp_connect.data.contest.ContestRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepoModule {
    @Binds
    @Singleton
    abstract ContestRepository bindsContestRepository();
}