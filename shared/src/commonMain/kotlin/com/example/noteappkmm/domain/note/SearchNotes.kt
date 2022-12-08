package com.example.noteappkmm.domain.note

import com.example.noteappkmm.domain.time.DateTimeUtil

class SearchNotes {

    fun searchNotesExecute(notes: List<Note>, query: String): List<Note> {
        if (query.isBlank()) return notes

        return notes.filter {
            it.title.replace(" ", "").lowercase().contains(query.replace(" ", "").lowercase())
        }.sortedBy { DateTimeUtil.toEpochMillis(it.createdTime) }
    }
}