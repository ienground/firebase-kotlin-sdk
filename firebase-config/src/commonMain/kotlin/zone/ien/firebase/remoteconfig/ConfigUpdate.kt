package zone.ien.firebase.remoteconfig

public expect class ConfigUpdate {
    public val updatedKeys: Set<String>
}
