package org.mule.devkit.apt.generator.spring;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import org.mule.config.spring.handlers.AbstractPojoNamespaceHandler;
import org.mule.config.spring.parsers.specific.MessageProcessorDefinitionParser;
import org.mule.devkit.annotations.Processor;
import org.mule.devkit.annotations.Transformer;
import org.mule.devkit.apt.AnnotationProcessorContext;
import org.mule.devkit.apt.generator.AbstractCodeGenerator;
import org.mule.devkit.apt.generator.GenerationException;
import org.mule.devkit.apt.util.CodeModelUtils;
import org.mule.devkit.apt.util.NameUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.List;

public class NamespaceHandlerGenerator extends AbstractCodeGenerator {
    public NamespaceHandlerGenerator(AnnotationProcessorContext context) {
        super(context);
    }

    public void generate(TypeElement type) throws GenerationException {
        String namespaceHandlerName = getContext().getElements().getBinaryName(type) + "NamespaceHandler";

        JDefinedClass namespaceHandlerClass = getOrCreateClass(namespaceHandlerName);
        namespaceHandlerClass._extends(AbstractPojoNamespaceHandler.class);

        JMethod init = namespaceHandlerClass.method(JMod.PUBLIC, getContext().getCodeModel().VOID, "init");

        generateRegisterPojo(init, type.asType());
        registerBeanDefinitionParserForEachProcessor(type, init);
        registerBeanDefinitionParserForEachTransformer(type, init);
    }

    private void generateRegisterPojo(JMethod init, TypeMirror pojo) {
        JInvocation registerPojo = JExpr.invoke("registerPojo");
        registerPojo.arg("config");
        registerPojo.arg(JExpr.dotclass(ref(pojo).boxify()));
        init.body().add(registerPojo);
    }

    private void registerBeanDefinitionParserForEachProcessor(TypeElement type, JMethod init) {
        List<ExecutableElement> executableElements = ElementFilter.methodsIn(type.getEnclosedElements());
        for (ExecutableElement executableElement : executableElements) {
            Processor processor = executableElement.getAnnotation(Processor.class);

            if (processor == null)
                continue;

            registerBeanDefinitionParser(init, executableElement);
        }
    }

    private void registerBeanDefinitionParserForEachTransformer(TypeElement type, JMethod init) {
        List<ExecutableElement> executableElements = ElementFilter.methodsIn(type.getEnclosedElements());
        for (ExecutableElement executableElement : executableElements) {
            Transformer transformer = executableElement.getAnnotation(Transformer.class);

            if (transformer == null)
                continue;

            JInvocation registerMuleBeanDefinitionParser = init.body().invoke("registerMuleBeanDefinitionParser");
            registerMuleBeanDefinitionParser.arg(JExpr.lit(NameUtils.uncamel(executableElement.getSimpleName().toString())));
            registerMuleBeanDefinitionParser.arg(JExpr._new(ref(MessageProcessorDefinitionParser.class)).arg(ref(getTransformerNameFor(executableElement)).boxify().dotclass()));
       }
    }

    private void registerBeanDefinitionParser(JMethod init, ExecutableElement executableElement) {
        JDefinedClass beanDefinitionParser = getBeanDefinitionParserClass(executableElement);
        JDefinedClass messageProcessor = getMessageProcessorClass(executableElement);

        Processor processor = executableElement.getAnnotation(Processor.class);
        String elementName = executableElement.getSimpleName().toString();
        if (processor.name().length() != 0)
            elementName = processor.name();

        init.body().invoke("registerMuleBeanDefinitionParser").arg(JExpr.lit(NameUtils.uncamel(elementName))).arg(JExpr._new(beanDefinitionParser));

        for (VariableElement variable : executableElement.getParameters()) {
            if (CodeModelUtils.isXmlType(variable)) {
                JInvocation newAnyXmlChildDefinitionParser = JExpr._new(getAnyXmlChildDefinitionParserClass(executableElement));
                newAnyXmlChildDefinitionParser.arg(JExpr.lit(variable.getSimpleName().toString()));
                newAnyXmlChildDefinitionParser.arg(JExpr.dotclass(messageProcessor));

                init.body().invoke("registerMuleBeanDefinitionParser").arg(JExpr.lit(variable.getSimpleName().toString())).arg(newAnyXmlChildDefinitionParser);
            }
        }
    }
}
