import SwiftUI

struct HideableSearchTextView<Destination: View>: View {
    
    var onSearchToggled: () -> Void
    var destinationProvider: () -> Destination
    var isSearchActive: Bool
    @Binding var searchText: String
    
    var body: some View {

        HStack {
            TextField("Searching . . .", text: $searchText)
                .textFieldStyle(.roundedBorder)
                .opacity(isSearchActive ? 1 : 0)
            
            if !isSearchActive {
                Spacer()
            }
            
            Button(action: onSearchToggled){
                Image(systemName: isSearchActive ? "xmark" : "magnifyingglass")
            }
            
            NavigationLink(destination: destinationProvider) {
                Image(systemName: "plus")
            }
        }
        
    }
}

struct HideableSearchTextView_Previews: PreviewProvider {
    static var previews: some View {
        HideableSearchTextView(onSearchToggled: { },
                               destinationProvider: {EmptyView()},
                               isSearchActive: false,
                               searchText: .constant("Hello world"))
    }
}
