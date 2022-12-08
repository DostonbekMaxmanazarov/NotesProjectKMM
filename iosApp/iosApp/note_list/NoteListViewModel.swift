import Foundation
import shared

extension NoteListScreenView {
    
    @MainActor class NoteListViewModel: ObservableObject {
        
        private var noteDataSource: INoteDataSource? = nil
        private let searchNote = SearchNotes()
        
        private var notes = [Note]()
        @Published private(set) var filteredNote = [Note]()
        @Published private(set) var isSearchActive = false
        @Published var searchText = "" {
            didSet {
                self.filteredNote = searchNote.searchNotesExecute(notes: self.notes, query: searchText)
            }
        }
        
        init(noteDataSource: INoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }
        
        func setNoteDataSource(noteDataSource: INoteDataSource) {
            self.noteDataSource = noteDataSource
        }
        
        func loadNotes() {
            noteDataSource?.getAllNotes(completionHandler: { notes, error in
                self.notes = notes ?? []
                self.filteredNote = self.notes
            })
        }
        
        func deleteById(id: Int64?) {
            if id != nil {
                noteDataSource?.deleteNoteById(id: id!, completionHandler: { error in
                    self.loadNotes()
                })
            }
        }
        
        func toggleIsSearchActive() {
            self.isSearchActive = !self.isSearchActive
            if !self.isSearchActive {
                searchText = ""
            }
        }
    }
}
