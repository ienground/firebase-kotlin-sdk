package zone.ien.firebase.encoders.protoc

public object NamingHelper {
    public fun toCamelCase(snakeCase: String): String {
        val camel = if (!snakeCase.contains('_')) {
            snakeCase
        } else {
            val parts = snakeCase.split('_')
            val sb = StringBuilder(parts[0])
            for (i in 1 until parts.size) {
                val part = parts[i]
                if (part.isNotEmpty()) {
                    sb.append(part[0].uppercaseChar())
                    sb.append(part.substring(1))
                }
            }
            sb.toString()
        }
        if (camel.isEmpty()) return camel
        return camel[0].lowercaseChar() + camel.substring(1)
    }

    public fun toUpperCamelCase(name: String): String {
        val camel = toCamelCase(name)
        if (camel.isEmpty()) return camel
        return camel[0].uppercaseChar() + camel.substring(1)
    }
}
