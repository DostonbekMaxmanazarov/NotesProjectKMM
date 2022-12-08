package com.example.noteappkmm.android.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappkmm.android.note_list.NoteListState
import com.example.noteappkmm.data.NoteDataSourceImpl
import com.example.noteappkmm.domain.note.INoteDataSource
import com.example.noteappkmm.domain.note.Note
import com.example.noteappkmm.domain.note.SearchNotes
import com.example.noteappkmm.domain.time.DateTimeUtil
import com.example.noteappkmm.presentation.RedOrangeHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: INoteDataSource, private val savedState: SavedStateHandle
) : ViewModel() {
    private val searchNotes = SearchNotes()

    private val notes = savedState.getStateFlow("notes", emptyList<Note>())
    private val searchText = savedState.getStateFlow("searchText", "")
    private val isSearchActive = savedState.getStateFlow("isSearchActive", false)

    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchText ->
        NoteListState(
            notes = searchNotes.searchNotesExecute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    fun loadNotes() {
        viewModelScope.launch {
            savedState["notes"] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        savedState["searchText"] = text
    }

    fun onSelectedToggleSearch() {
        savedState["isSearchActive"] = !isSearchActive.value
        if (!isSearchActive.value) {
            savedState["searchText"] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes()
        }
    }
}