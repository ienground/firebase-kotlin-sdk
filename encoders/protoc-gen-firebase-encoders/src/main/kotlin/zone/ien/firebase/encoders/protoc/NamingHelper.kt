package zone.ien.firebase.encoders.protoc

public object NamingHelper {
    public fun toCamelCase(snakeCase: String): String {
        if (!snakeCase.contains('_')) return snakeCase
        val parts = snakeCase.split('_')
        val sb = StringBuilder(parts[0])
        for (i in 1 until parts.size) {
            val part = parts[i]
            if (part.isNotEmpty()) {
                sb.append(part[0].uppercaseChar())
                sb.append(part.substring(1))
            }
        }
        return sb.toString()
    }

    public fun toUpperCamelCase(name: String): String {
        val camel = toCamelCase(name)
        if (camel.isEmpty()) return camel
        return camel[0].uppercaseChar() + camel.substring(1)
    }
}
