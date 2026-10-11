package zone.ien.firebase.encoders.protoc

import com.google.protobuf.DescriptorProtos
import com.google.protobuf.compiler.PluginProtos
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProtocGenFirebaseEncodersTest {

    @Test
    fun testNamingHelperCamelCase() {
        assertEquals("userProfile", NamingHelper.toCamelCase("user_profile"))
        assertEquals("UserProfile", NamingHelper.toUpperCamelCase("user_profile"))
        assertEquals("simple", NamingHelper.toCamelCase("simple"))
    }

    @Test
    fun testCodeGenerationTemplate() {
        // Mock a CodeGeneratorRequest
        val field = DescriptorProtos.FieldDescriptorProto.newBuilder()
            .setName("phone_number")
            .setNumber(3)
            .build()

        val message = DescriptorProtos.DescriptorProto.newBuilder()
            .setName("ContactInfo")
            .addField(field)
            .build()

        val fileProto = DescriptorProtos.FileDescriptorProto.newBuilder()
            .setName("contacts.proto")
            .setPackage("zone.ien.test")
            .addMessageType(message)
            .build()

        val request = PluginProtos.CodeGeneratorRequest.newBuilder()
            .addFileToGenerate("contacts.proto")
            .addProtoFile(fileProto)
            .build()

        val response = generate(request)

        assertEquals(1, response.fileCount)
        val responseFile = response.getFile(0)

        assertEquals("zone/ien/test/ContactInfoEncoder.kt", responseFile.name)
        val content = responseFile.content

        assertTrue(content.contains("package zone.ien.test"))
        assertTrue(content.contains("public class ContactInfoEncoder : ObjectEncoder<ContactInfo>"))
        assertTrue(content.contains("private val phoneNumberDescriptor = FieldDescriptor.builder(\"phoneNumber\")"))
        assertTrue(content.contains("tag = 3"))
        assertTrue(content.contains("context.add(phoneNumberDescriptor, value.phoneNumber)"))
    }
}
