package zone.ien.firebase.ml.modeldownloader

public class CustomModelDownloadConditions private constructor(
    public val requireWifi: Boolean,
    public val requireDeviceIdle: Boolean,
    public val requireCharging: Boolean
) {
    public class Builder {
        private var requireWifi = false
        private var requireDeviceIdle = false
        private var requireCharging = false

        public fun requireWifi(): Builder {
            this.requireWifi = true
            return this
        }

        public fun requireDeviceIdle(): Builder {
            this.requireDeviceIdle = true
            return this
        }

        public fun requireCharging(): Builder {
            this.requireCharging = true
            return this
        }

        public fun build(): CustomModelDownloadConditions {
            return CustomModelDownloadConditions(requireWifi, requireDeviceIdle, requireCharging)
        }
    }
}
