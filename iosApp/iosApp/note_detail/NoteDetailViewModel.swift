import Foundation
import shared

extension NoteDetailScreenView {
    @MainActor class NoteDetailViewModel : ObservableObject {
        
        private var noteDataSource: INoteDataSource?
        private var noteId: Int64? = nil
        
        @Published var noteTitle = ""
        @Published var noteContent = ""
        @Published var noteColor = Note.Companion().generateRandomColor()
        
        init(noteDataSource: INoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }
        
        func loadNoteIfExists(id: Int64?) {
            if id != nil {
                self.noteId = id
                
                noteDataSource?.getNoteById(id: id!, completionHandler: { note, error in
                    self.noteTitle = note?.title ?? ""
                    self.noteContent = note?.content ?? ""
                    self.noteColor = note?.colorHex ?? Note.Companion().generateRandomColor()
                })
            }
        }
        
        func saveNote(onSaved: @escaping () -> Void) {
            noteDataSource?.insertNote(note: Note(id: noteId == nil ? nil : KotlinLong(value: noteId!), title: self.noteTitle, content: self.noteContent, colorHex: self.noteColor, createdTime: DateTimeUtil().nowTime()), completionHandler: {error in
                onSaved()
            })
        }
        
        func setParamsAndLoadNote(noteDataSource: INoteDataSource, noteId: Int64?) {
            self.noteDataSource = noteDataSource
            loadNoteIfExists(id: noteId)
        }
    }
}
