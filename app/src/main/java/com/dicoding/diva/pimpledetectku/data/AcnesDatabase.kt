package com.dicoding.diva.pimpledetectku.data

import androidx.room.RoomDatabase

abstract class AcnesDatabase : RoomDatabase() {
    abstract fun remoteKeysDao(): RemoteKeysDao
}