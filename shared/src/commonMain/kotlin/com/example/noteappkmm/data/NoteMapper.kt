package com.example.noteappkmm.data

import com.example.noteappkmm.domain.note.Note
import database.NoteEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        colorHex = colorHex,
        content = content,
        createdTime = Instant.fromEpochMilliseconds(createdTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}