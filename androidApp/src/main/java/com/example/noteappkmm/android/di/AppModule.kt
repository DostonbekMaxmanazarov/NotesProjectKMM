package com.example.noteappkmm.android.di

import android.app.Application
import com.example.noteappkmm.data.NoteDataSourceImpl
import com.example.noteappkmm.database.NoteDatabase
import com.example.noteappkmm.database_driver.DatabaseDriverFactory
import com.example.noteappkmm.domain.note.INoteDataSource
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).createDriver()
    }

    @Provides
    @Singleton
    fun provideNoteDataSource(driver: SqlDriver): INoteDataSource {
        return NoteDataSourceImpl(NoteDatabase(driver))
    }
}