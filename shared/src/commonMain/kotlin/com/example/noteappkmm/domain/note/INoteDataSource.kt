package com.example.noteappkmm.domain.note

interface INoteDataSource {
    suspend fun insertNote(note: Note)
    suspend fun deleteNoteById(id: Long)
    suspend fun getNoteById(id: Long): Note?
    suspend fun getAllNotes(): List<Note>
}