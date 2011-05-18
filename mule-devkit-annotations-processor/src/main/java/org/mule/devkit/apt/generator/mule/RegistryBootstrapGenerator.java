package org.mule.devkit.apt.generator.mule;

import com.sun.codemodel.JDefinedClass;
import org.mule.devkit.apt.AnnotationProcessorContext;
import org.mule.devkit.apt.generator.AbstractCodeGenerator;
import org.mule.devkit.apt.generator.GenerationException;

import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RegistryBootstrapGenerator extends AbstractCodeGenerator {
    public RegistryBootstrapGenerator(AnnotationProcessorContext context) {
        super(context);
    }

    public void generate(TypeElement element) throws GenerationException {

        try {
            OutputStream registryBootstrapStream = getContext().getCodeWriter().openBinary(null, "META-INF/services/org/mule/config/registry-bootstrap.properties");
            OutputStreamWriter registryBootstrapStreamOut = new OutputStreamWriter(registryBootstrapStream, "UTF-8");

            for (JDefinedClass clazz : getContext().getClassesToRegisterAtBoot()) {
                registryBootstrapStreamOut.write(clazz.name() + "=" + clazz.fullName() + "\n");

            }

            registryBootstrapStreamOut.flush();
            registryBootstrapStreamOut.close();
        } catch (IOException ioe) {
            throw new GenerationException(ioe);
        }
    }
}
