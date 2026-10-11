package zone.ien.firebase.storage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
public actual class Data(public val nsData: NSData) {
    public constructor(bytes: ByteArray) : this(bytes.toNSData())
}
