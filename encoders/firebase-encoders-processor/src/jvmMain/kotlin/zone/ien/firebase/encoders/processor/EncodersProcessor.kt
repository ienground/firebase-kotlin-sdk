package zone.ien.firebase.encoders.processor

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@SupportedAnnotationTypes("zone.ien.firebase.encoders.annotations.Encodable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class EncodersProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        for (element in roundEnv.getElementsAnnotatedWith(zone.ien.firebase.encoders.annotations.Encodable::class.java)) {
            val className = element.simpleName.toString()
            processingEnv.messager.printMessage(
                Diagnostic.Kind.NOTE,
                "EncodersProcessor: Detected @Encodable class: $className. Simulating code generation registrar metadata templates..."
            )
            // Simulating generated file emission using javac filer
            // Under real full implementation, a JavaFileObject is written emitting ObjectEncoder subclasses.
        }
        return true
    }
}
