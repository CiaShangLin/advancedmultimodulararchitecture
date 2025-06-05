package com.shang.protodatastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.shang.protodatastore.Preferences
import com.shang.protodatastore.Session
import com.shang.protodatastore.factory.preferencesDataStore
import com.shang.protodatastore.factory.sessionDataStore
import com.shang.protodatastore.manager.preferences.PreferencesDataStoreImp
import com.shang.protodatastore.manager.preferences.PreferencesDataStoreInterface
import com.shang.protodatastore.manager.session.SessionDataStoreImp
import com.shang.protodatastore.manager.session.SessionDataStoreInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Session> {
        return context.sessionDataStore
    }

    @Provides
    @Singleton
    fun provideSessionDataManager(sessionDataStore: DataStore<Session>): SessionDataStoreInterface {
        return SessionDataStoreImp(sessionDataStore)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.preferencesDataStore
    }

    @Provides
    @Singleton
    fun providePreferencesDataManager(preferencesDataStore: DataStore<Preferences>): PreferencesDataStoreInterface {
        return PreferencesDataStoreImp(preferencesDataStore)
    }
}
