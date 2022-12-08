import SwiftUI
import shared

struct NoteItemView: View {
    
    var note: Note
    var onDeleteClick: () -> Void
    
    var body: some View {
        
        VStack(alignment: .leading) {
            
            HStack {
                
                Text(note.title)
                    .font(.title3)
                    .fontWeight(.semibold)
                
                Spacer()
                
                Button(action: onDeleteClick) {
                    Image(systemName: "xmark").foregroundColor(.black.opacity(0.8))
                }
            }.padding(.bottom, 3)
            
            Text(note.content)
                .fontWeight(.light)
                .padding(.bottom, 3)
            
            HStack {
                Spacer()
                
                Text(DateTimeUtil().formatDate(dateTime: note.createdTime))
                    .font(.footnote)
                    .fontWeight(.light)
            }
        }
        .padding()
        .background(Color(hex: note.colorHex))
        .clipShape(RoundedRectangle(cornerRadius: 10))
    }
}

struct NoteItemView_Previews: PreviewProvider {
    static var previews: some View {
        NoteItemView(note: Note(id: nil, title: "Note", content: "Note content", colorHex: 0xFF324, createdTime: DateTimeUtil().nowTime()), onDeleteClick: {})
    }
}
