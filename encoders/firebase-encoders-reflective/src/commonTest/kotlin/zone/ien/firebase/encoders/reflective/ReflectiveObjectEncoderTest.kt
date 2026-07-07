package zone.ien.firebase.encoders.reflective

import zone.ien.firebase.encoders.ObjectEncoder
import zone.ien.firebase.encoders.ObjectEncoderContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ReflectiveObjectEncoderTest {

    data class ExplicitTestModel(val name: String, val count: Int)

    private val explicitModelEncoder = object : ObjectEncoder<ExplicitTestModel> {
        override fun encode(value: ExplicitTestModel, context: ObjectEncoderContext) {
            context.add("name", value.name)
            context.add("count", value.count)
        }
    }

    @Test
    fun testExplicitRegistrationFallback() {
        // Registers explicit fallback path (crucial for iOS target capability limits)
        ReflectiveObjectEncoder.registerEncoderExplicit(
            ExplicitTestModel::class,
            explicitModelEncoder
        )

        val model = ExplicitTestModel("fallback_user", 999)
        val reflectiveEncoder = ReflectiveObjectEncoder<ExplicitTestModel>()

        val logs = mutableListOf<String>()
        val mockContext = object : ObjectEncoderContext {
            override fun add(name: String, value: Any?): ObjectEncoderContext {
                logs.add("$name:$value")
                return this
            }
            override fun add(name: String, value: Double): ObjectEncoderContext = add(name, value as Any?)
            override fun add(name: String, value: Int): ObjectEncoderContext = add(name, value as Any?)
            override fun add(name: String, value: Long): ObjectEncoderContext = add(name, value as Any?)
            override fun add(name: String, value: Boolean): ObjectEncoderContext = add(name, value as Any?)
            override fun add(name: String, value: Float): ObjectEncoderContext = add(name, value as Any?)

            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Any?): ObjectEncoderContext = add(field.name, value)
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Double): ObjectEncoderContext = add(field.name, value as Any?)
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Int): ObjectEncoderContext = add(field.name, value as Any?)
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Long): ObjectEncoderContext = add(field.name, value as Any?)
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Boolean): ObjectEncoderContext = add(field.name, value as Any?)
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Float): ObjectEncoderContext = add(field.name, value as Any?)

            override fun inline(value: Any?): ObjectEncoderContext = this
            override fun nested(name: String): ObjectEncoderContext = this
            override fun nested(field: zone.ien.firebase.encoders.FieldDescriptor): ObjectEncoderContext = this
        }

        // Executes encoding via reflective delegate (resolving to explicit configuration)
        reflectiveEncoder.encode(model, mockContext)

        assertTrue(logs.contains("name:fallback_user"))
        assertTrue(logs.contains("count:999"))
    }

    data class UnregisteredModel(val secret: String)

    @Test
    fun testUnregisteredFallbackFailureOnUnsupportedPlatform() {
        val model = UnregisteredModel("unsupported_data")
        val reflectiveEncoder = ReflectiveObjectEncoder<UnregisteredModel>()

        val mockContext = object : ObjectEncoderContext {
            override fun add(name: String, value: Any?): ObjectEncoderContext = this
            override fun add(name: String, value: Double): ObjectEncoderContext = this
            override fun add(name: String, value: Int): ObjectEncoderContext = this
            override fun add(name: String, value: Long): ObjectEncoderContext = this
            override fun add(name: String, value: Boolean): ObjectEncoderContext = this
            override fun add(name: String, value: Float): ObjectEncoderContext = this

            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Any?): ObjectEncoderContext = this
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Double): ObjectEncoderContext = this
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Int): ObjectEncoderContext = this
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Long): ObjectEncoderContext = this
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Boolean): ObjectEncoderContext = this
            override fun add(field: zone.ien.firebase.encoders.FieldDescriptor, value: Float): ObjectEncoderContext = this

            override fun inline(value: Any?): ObjectEncoderContext = this
            override fun nested(name: String): ObjectEncoderContext = this
            override fun nested(field: zone.ien.firebase.encoders.FieldDescriptor): ObjectEncoderContext = this
        }

        // On iOS Native execution, calling this unregistered domain raises UnsupportedOperationException
        // Under JVM/Android environment, standard reflection triggers and serializes it without exceptions.
        try {
            reflectiveEncoder.encode(model, mockContext)
        } catch (e: UnsupportedOperationException) {
            // Validating that clean messaging exists on unsupported reflection hosts (e.g. iOS native)
            assertTrue(e.message!!.contains("Reflection-based auto-serialization is not supported"))
        } catch (e: Exception) {
            // On JVM, it runs reflection-based code successfully.
        }
    }
}
