package com.shang.protodatastore.factory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.shang.protodatastore.Preferences
import com.shang.protodatastore.Session
import com.shang.protodatastore.serializer.PreferencesSerializer
import com.shang.protodatastore.serializer.SessionSerializer

val Context.sessionDataStore: DataStore<Session> by dataStore(
    fileName = "session.pb",
    serializer = SessionSerializer,
)

val Context.preferencesDataStore: DataStore<Preferences> by dataStore(
    fileName = "preferences.pb",
    serializer = PreferencesSerializer,
)
