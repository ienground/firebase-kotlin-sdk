package zone.ien.firebase.storage

import android.net.Uri

public actual class File(public val uri: Uri) {
    public constructor(file: java.io.File) : this(Uri.fromFile(file))
    public constructor(path: String) : this(java.io.File(path))
}
