package zone.ien.firebase.database

import zone.ien.firebase.FirebaseApp

public expect class FirebaseDatabase {
    public fun reference(): DatabaseReference
    public fun reference(path: String): DatabaseReference

    public companion object {
        public fun getInstance(): FirebaseDatabase
        public fun getInstance(app: FirebaseApp): FirebaseDatabase
        public fun getInstance(url: String): FirebaseDatabase
    }
}
