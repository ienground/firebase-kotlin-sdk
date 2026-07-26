package zone.ien.firebase.storage

import platform.Foundation.NSURL

public actual class File(public val url: NSURL) {
    public constructor(path: String) : this(NSURL.fileURLWithPath(path))
}
