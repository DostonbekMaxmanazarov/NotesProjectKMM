package com.example.noteappkmm.android.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappkmm.domain.note.INoteDataSource
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: INoteDataSource, private val savedState: SavedStateHandle
) : ViewModel() {

    private val noteTitle = savedState.getStateFlow("noteTitle", "")
    private val isNoteTitleTextFocused = savedState.getStateFlow("isNoteTitleTextFocused", false)
    private val noteContent = savedState.getStateFlow("noteContent", "")
    private val isNoteContentTextFocused =
        savedState.getStateFlow("isNoteContentTextFocused", false)
    private val noteColor = savedState.getStateFlow("noteColor", Note.generateRandomColor())

    val state = combine(
        noteTitle, isNoteTitleTextFocused, noteContent, isNoteContentTextFocused, noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintFocused = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintFocused = content.isEmpty() && !isContentFocused,
            noteColor = color
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved.asStateFlow()

    private var existingNoteId: Long? = null

    init {
        savedState.get<Long>("noteId")?.let { existingNoteId ->
            if (existingNoteId == -1L) {
                return@let
            }
            this.existingNoteId = existingNoteId
            viewModelScope.launch {
                noteDataSource.getNoteById(existingNoteId)?.let { note ->
                    savedState["noteTitle"] = note.title
                    savedState["noteContent"] = note.content
                    savedState["noteColor"] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChange(text: String) {
        savedState["noteTitle"] = text
    }

    fun onNoteContentChange(text: String) {
        savedState["noteContent"] = text
    }

    fun onNoteTitleFocusedChange(isFocused: Boolean) {
        savedState["isNoteTitleHintFocused"] = isFocused
    }

    fun onNoteContentFocusedChange(isFocused: Boolean) {
        savedState["isNoteContentHintFocused"] = isFocused
    }

    fun saved() {
        viewModelScope.launch {
            noteDataSource
                .insertNote(
                    Note(
                        id = existingNoteId,
                        title = noteTitle.value,
                        content = noteContent.value,
                        colorHex = noteColor.value,
                        createdTime = DateTimeUtil.nowTime()
                    )
                )

            _hasNoteBeenSaved.value = true
        }
    }
}