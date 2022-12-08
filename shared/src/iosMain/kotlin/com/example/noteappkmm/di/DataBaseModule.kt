package com.example.noteappkmm.di

import com.example.noteappkmm.data.NoteDataSourceImpl
import com.example.noteappkmm.database.NoteDatabase
import com.example.noteappkmm.database_driver.DatabaseDriverFactory

class DataBaseModule {
    private val factory by lazy { DatabaseDriverFactory() }
    val noteDataSource by lazy { NoteDataSourceImpl(NoteDatabase(factory.createDriver())) }
}