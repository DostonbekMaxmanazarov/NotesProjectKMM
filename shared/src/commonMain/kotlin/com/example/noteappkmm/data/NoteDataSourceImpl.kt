package com.example.noteappkmm.data

import com.example.noteappkmm.database.NoteDatabase
import com.example.noteappkmm.domain.note.INoteDataSource
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.time.DateTimeUtil

class NoteDataSourceImpl(db: NoteDatabase) : INoteDataSource {

    private val queries = db.noteQueries

    override suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            colorHex = note.colorHex,
            content = note.content,
            createdTime = DateTimeUtil.toEpochMillis(note.createdTime)
        )
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteById(id)
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries.getNoteById(id).executeAsOneOrNull()?.toNote()
    }

    override suspend fun getAllNotes(): List<Note> {
        return queries.getAllNotes().executeAsList().map { it.toNote() }
    }
}