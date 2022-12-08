import SwiftUI
import shared

struct NoteListScreenView: View {
   
    private var noteDataSource: INoteDataSource
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil)
    
    @State private var isNoteItemSelected = false
    @State private var selectedNoteId: Int64? = nil
    
    init(noteDataSource: INoteDataSource) {
        self.noteDataSource = noteDataSource
    }
    
    var body: some View {
        
        VStack {
            
            ZStack {
                
                NavigationLink(destination: NoteDetailScreenView(noteDataSource: self.noteDataSource, noteId: self.selectedNoteId), isActive: $isNoteItemSelected, label: {})
                
                HideableSearchTextView<NoteDetailScreenView>(onSearchToggled: {
                    viewModel.toggleIsSearchActive()
                }, destinationProvider: {
                    NoteDetailScreenView(noteDataSource: noteDataSource, noteId: selectedNoteId)
                }, isSearchActive: viewModel.isSearchActive, searchText: $viewModel.searchText)
                    .frame(maxWidth: .infinity, minHeight: 40)
                    .padding()
                
                if !viewModel.isSearchActive {
                    Text("All notes")
                        .font(.title2)
                }
            }
            
            List {
                ForEach(viewModel.filteredNote, id: \.self.id) { note in
                    Button(action: {
                        self.isNoteItemSelected = true
                        self.selectedNoteId = note.id?.int64Value
                    }){
                        NoteItemView(note: note, onDeleteClick: {
                            viewModel.deleteById(id: note.id?.int64Value)
                        })
                    }
                }
            }.onAppear {
                viewModel.loadNotes()
            }.listStyle(.plain)
            .listRowSeparator(.hidden)
            
        }
        .onAppear{
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
    }
}

struct NoteListScreenView_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
