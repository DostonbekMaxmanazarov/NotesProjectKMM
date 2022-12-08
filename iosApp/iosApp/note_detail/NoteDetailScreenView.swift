import SwiftUI
import shared

struct NoteDetailScreenView: View {
    
    private var noteDataSource: INoteDataSource
    private var noteId: Int64? = nil
    
    @StateObject var viewModel = NoteDetailViewModel(noteDataSource: nil)
    
    @Environment(\.presentationMode) var presentation
    
    init(noteDataSource: INoteDataSource, noteId: Int64?) {
        self.noteDataSource = noteDataSource
        self.noteId = noteId
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            TextField("Enter a title ...", text: $viewModel.noteTitle)
                .font(.title)
            
            TextField("Enter some content ...", text: $viewModel.noteContent)
                .font(.title2)
            
            Spacer()
            
        }.toolbar(content: {
            Button(action: {
                viewModel.saveNote {
                    self.presentation.wrappedValue.dismiss()
                }
            }){
                Image(systemName: "checkmark")
            }
        })
            .padding()
            .background(Color(hex: viewModel.noteColor))
            .onAppear {
                viewModel.setParamsAndLoadNote(noteDataSource: noteDataSource, noteId: noteId)
            }.navigationBarTitle("", displayMode: .inline)
    }
}

struct NoteDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
