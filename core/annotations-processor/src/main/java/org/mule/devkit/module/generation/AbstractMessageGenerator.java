/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mule.devkit.module.generation;

import org.apache.commons.lang.StringUtils;
import org.mule.api.annotations.callback.HttpCallback;
import org.mule.api.annotations.callback.ProcessorCallback;
import org.mule.api.annotations.callback.SourceCallback;
import org.mule.api.construct.FlowConstructAware;
import org.mule.api.context.MuleContextAware;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.processor.InterceptingMessageProcessor;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.RegistrationException;
import org.mule.api.source.MessageSource;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.Transformer;
import org.mule.config.i18n.CoreMessages;
import org.mule.construct.SimpleFlowConstruct;
import org.mule.devkit.model.code.Block;
import org.mule.devkit.model.code.CatchBlock;
import org.mule.devkit.model.code.Conditional;
import org.mule.devkit.model.code.DefinedClass;
import org.mule.devkit.model.code.Expression;
import org.mule.devkit.model.code.ExpressionFactory;
import org.mule.devkit.model.code.FieldVariable;
import org.mule.devkit.model.code.Invocation;
import org.mule.devkit.model.code.Method;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.Op;
import org.mule.devkit.model.code.Package;
import org.mule.devkit.model.code.TryStatement;
import org.mule.devkit.model.code.TypeReference;
import org.mule.devkit.model.code.Variable;
import org.mule.devkit.model.code.builders.FieldBuilder;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.util.TemplateParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMessageGenerator extends AbstractModuleGenerator {

    protected FieldVariable generateFieldForPatternInfo(DefinedClass messageProcessorClass) {
        FieldVariable patternInfo = messageProcessorClass.field(Modifier.PRIVATE, ref(TemplateParser.PatternInfo.class), "patternInfo");
        patternInfo.javadoc().add("Mule Pattern Info");
        return patternInfo;
    }

    protected FieldVariable generateFieldForExpressionManager(DefinedClass messageProcessorClass) {
        FieldVariable expressionManager = messageProcessorClass.field(Modifier.PRIVATE, ref(ExpressionManager.class), "expressionManager");
        expressionManager.javadoc().add("Mule Expression Manager");
        return expressionManager;
    }

    protected FieldVariable generateFieldForModuleObject(DefinedClass messageProcessorClass, Element typeElement) {
        DefinedClass moduleObject = context.getClassForRole(context.getNameUtils().generateModuleObjectRoleKey((TypeElement) typeElement));
        FieldVariable field = messageProcessorClass.field(Modifier.PRIVATE, moduleObject, "moduleObject");
        field.javadoc().add("Module object");

        return field;
    }

    protected FieldVariable generateFieldForMessageProcessorListener(DefinedClass messageSourceClass) {
        FieldVariable messageProcessor = messageSourceClass.field(Modifier.PRIVATE, ref(MessageProcessor.class), "messageProcessor");
        messageProcessor.javadoc().add("Message processor that will get called for processing incoming events");
        return messageProcessor;
    }

    protected DefinedClass getBeanDefinitionParserClass(ExecutableElement executableElement) {
        String beanDefinitionParserName = context.getNameUtils().generateClassName(executableElement, "DefinitionParser");
        Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(beanDefinitionParserName) + ".config.spring");
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(beanDefinitionParserName), new Class[]{BeanDefinitionParser.class});

        return clazz;
    }

    protected DefinedClass getConfigBeanDefinitionParserClass(Element typeElement) {
        String poolAdapterName = context.getNameUtils().generateClassName((TypeElement) typeElement, ".config.spring", "ConfigDefinitionParser");
        org.mule.devkit.model.code.Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(poolAdapterName));
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(poolAdapterName), new Class[]{BeanDefinitionParser.class});

        context.setClassRole(context.getNameUtils().generateConfigDefParserRoleKey((TypeElement) typeElement), clazz);

        return clazz;
    }

    protected DefinedClass getMessageProcessorClass(ExecutableElement executableElement) {
        String beanDefinitionParserName = context.getNameUtils().generateClassName(executableElement, "MessageProcessor");
        org.mule.devkit.model.code.Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(beanDefinitionParserName) + ".config");
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(beanDefinitionParserName), new Class[]{
                Initialisable.class,
                Startable.class,
                Disposable.class,
                Stoppable.class,
                MessageProcessor.class,
                MuleContextAware.class});

        return clazz;
    }

    protected DefinedClass getInterceptingMessageProcessorClass(ExecutableElement executableElement) {
        String beanDefinitionParserName = context.getNameUtils().generateClassName(executableElement, "MessageProcessor");
        org.mule.devkit.model.code.Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(beanDefinitionParserName) + ".config");
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(beanDefinitionParserName), new Class[]{
                Initialisable.class,
                Startable.class,
                Disposable.class,
                Stoppable.class,
                InterceptingMessageProcessor.class,
                MuleContextAware.class});

        return clazz;
    }


    protected DefinedClass getMessageSourceClass(ExecutableElement executableElement) {
        String beanDefinitionParserName = context.getNameUtils().generateClassName(executableElement, "MessageSource");
        Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(beanDefinitionParserName) + ".config");
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(beanDefinitionParserName), new Class[]{
                MuleContextAware.class,
                Startable.class,
                Stoppable.class,
                Runnable.class,
                Initialisable.class,
                MessageSource.class,
                SourceCallback.class,
                FlowConstructAware.class});

        return clazz;
    }

    protected Map<String, FieldVariableElement> generateFieldForEachParameter(DefinedClass messageProcessorClass, ExecutableElement processorMethod) {
        Map<String, AbstractMessageGenerator.FieldVariableElement> fields = new HashMap<String, FieldVariableElement>();
        for (VariableElement variable : processorMethod.getParameters()) {
            if (variable.asType().toString().contains(SourceCallback.class.getName()))
                continue;
            String fieldName = variable.getSimpleName().toString();

            FieldVariable field = null;
            FieldVariable fieldType = null;
            if (variable.asType().toString().contains(ProcessorCallback.class.getName())) {
                field = new FieldBuilder(messageProcessorClass).
                        privateVisibility().
                        type(MessageProcessor.class).
                        name(fieldName).
                        build();
                fieldType = new FieldBuilder(messageProcessorClass).
                        privateVisibility().
                        type(ref(variable.asType())).
                        name(fieldName + "Type").
                        build();
            } else if (variable.asType().toString().contains(HttpCallback.class.getName())) {
                // for each parameter of type HttpCallback we need two fields: one that will hold a reference to the flow
                // that is going to be executed upon the callback and the other one to hold the HttpCallback object itself
                field = new FieldBuilder(messageProcessorClass).
                        type(SimpleFlowConstruct.class).
                        name(fieldName + "CallbackFlow").
                        javadoc("The flow to be invoked when the http callback is received").build();
                fieldType = new FieldBuilder(messageProcessorClass).
                        type(HttpCallback.class).
                        name(fieldName).
                        javadoc("An HttpCallback instance responsible for linking the APIs http callback with the flow {@link " + messageProcessorClass.fullName() + "#" + fieldName + "CallbackFlow").build();
            } else {
                field = new FieldBuilder(messageProcessorClass).
                        privateVisibility().
                        type(Object.class).
                        name(fieldName).
                        build();
                fieldType = new FieldBuilder(messageProcessorClass).
                        privateVisibility().
                        type(ref(variable.asType())).
                        name(fieldName + "Type").
                        build();
            }
            fields.put(variable.getSimpleName().toString(), new AbstractMessageGenerator.FieldVariableElement(field, fieldType, variable));
        }
        return fields;
    }

    protected Map<String, FieldVariableElement> generateFieldForEachSetter(DefinedClass transferObjectClass, TypeElement transferObject) {
        Map<String, AbstractMessageGenerator.FieldVariableElement> fields = new HashMap<String, FieldVariableElement>();
        java.util.List<ExecutableElement> methods = ElementFilter.methodsIn(transferObject.getEnclosedElements());
        for (ExecutableElement method : methods) {
            String methodName = method.getSimpleName().toString();
            if (!methodName.startsWith("set") ||
                    method.getReturnType().getKind() != TypeKind.VOID ||
                    method.getParameters().size() != 1) {
                continue;
            }

            String fieldName = StringUtils.uncapitalize(methodName.substring(methodName.indexOf("set") + 3));
            TypeMirror fieldTypeMirror = method.getParameters().get(0).asType();
            FieldVariable field = null;
            if (fieldTypeMirror.toString().contains(ProcessorCallback.class.getName())) {
                field = transferObjectClass.field(Modifier.PRIVATE, ref(MessageProcessor.class), fieldName);
            } else {
                field = transferObjectClass.field(Modifier.PRIVATE, ref(Object.class), fieldName);
            }
            FieldVariable fieldType = transferObjectClass.field(Modifier.PRIVATE, ref(fieldTypeMirror), fieldName + "Type");
            fields.put(fieldName, new AbstractMessageGenerator.FieldVariableElement(field, fieldType, null));
        }
        return fields;
    }


    protected Method generateInitialiseMethod(DefinedClass messageProcessorClass, Map<String, FieldVariableElement> fields, Element typeElement, FieldVariable muleContext, FieldVariable expressionManager, FieldVariable patternInfo, FieldVariable object) {
        DefinedClass pojoClass = context.getClassForRole(context.getNameUtils().generateModuleObjectRoleKey((TypeElement) typeElement));

        Method initialise = messageProcessorClass.method(Modifier.PUBLIC, context.getCodeModel().VOID, "initialise");
        initialise.javadoc().add("Obtains the expression manager from the Mule context and initialises the connector. If a target object ");
        initialise.javadoc().add(" has not been set already it will search the Mule registry for a default one.");
        initialise.javadoc().addThrows(ref(InitialisationException.class));
        initialise._throws(InitialisationException.class);

        if (expressionManager != null) {
            initialise.body().assign(expressionManager, muleContext.invoke("getExpressionManager"));
        }
        if (patternInfo != null) {
            initialise.body().assign(patternInfo, ref(TemplateParser.class).staticInvoke("createMuleStyleParser").invoke("getStyle"));
        }

        Conditional ifNoObject = initialise.body()._if(Op.eq(object, ExpressionFactory._null()));
        TryStatement tryLookUp = ifNoObject._then()._try();
        tryLookUp.body().assign(object, muleContext.invoke("getRegistry").invoke("lookupObject").arg(ExpressionFactory.dotclass(pojoClass)));
        CatchBlock catchBlock = tryLookUp._catch(ref(RegistrationException.class));
        Variable exception = catchBlock.param("e");
        TypeReference coreMessages = ref(CoreMessages.class);
        Invocation failedToInvoke = coreMessages.staticInvoke("initialisationFailure");
        failedToInvoke.arg(pojoClass.fullName());
        Invocation messageException = ExpressionFactory._new(ref(InitialisationException.class));
        messageException.arg(failedToInvoke);
        messageException.arg(exception);
        messageException.arg(ExpressionFactory._this());
        catchBlock.body()._throw(messageException);

        if (fields != null) {
            for (String fieldName : fields.keySet()) {
                FieldVariableElement variableElement = fields.get(fieldName);

                if (variableElement.getVariableElement().asType().toString().contains(ProcessorCallback.class.getName())) {
                    Conditional ifInitialisable = initialise.body()._if(Op._instanceof(variableElement.getField(), ref(Initialisable.class)));
                    ifInitialisable._then().add(
                            ExpressionFactory.cast(ref(Initialisable.class), variableElement.getField()).invoke("initialise")
                    );
                }
            }
        }


        return initialise;
    }


    protected Method generateSetModuleObjectMethod(DefinedClass messageProcessorClass, FieldVariable object) {
        Method setObject = messageProcessorClass.method(Modifier.PUBLIC, context.getCodeModel().VOID, "setModuleObject");
        setObject.javadoc().add("Sets the instance of the object under which the processor will execute");
        setObject.javadoc().addParam("moduleObject Instace of the module");
        Variable objectParam = setObject.param(object.type(), "moduleObject");
        setObject.body().assign(ExpressionFactory._this().ref(object), objectParam);

        return setObject;
    }

    protected void generateTransform(Block block, Variable transformedField, Variable evaluatedField, TypeMirror expectedType, FieldVariable muleContext) {
        Invocation isAssignableFrom = ExpressionFactory.dotclass(ref(expectedType).boxify()).invoke("isAssignableFrom").arg(evaluatedField.invoke("getClass"));
        Conditional ifIsAssignableFrom = block._if(Op.not(isAssignableFrom));
        Block isAssignable = ifIsAssignableFrom._then();
        Variable dataTypeSource = isAssignable.decl(ref(DataType.class), "source");
        Variable dataTypeTarget = isAssignable.decl(ref(DataType.class), "target");

        isAssignable.assign(dataTypeSource, ref(DataTypeFactory.class).staticInvoke("create").arg(evaluatedField.invoke("getClass")));
        isAssignable.assign(dataTypeTarget, ref(DataTypeFactory.class).staticInvoke("create").arg(ExpressionFactory.dotclass(ref(expectedType).boxify())));

        Variable transformer = isAssignable.decl(ref(Transformer.class), "t");
        Invocation lookupTransformer = muleContext.invoke("getRegistry").invoke("lookupTransformer");
        lookupTransformer.arg(dataTypeSource);
        lookupTransformer.arg(dataTypeTarget);
        isAssignable.assign(transformer, lookupTransformer);
        isAssignable.assign(transformedField, ExpressionFactory.cast(ref(expectedType).boxify(), transformer.invoke("transform").arg(evaluatedField)));

        Block notAssignable = ifIsAssignableFrom._else();
        notAssignable.assign(transformedField, ExpressionFactory.cast(ref(expectedType).boxify(), evaluatedField));
    }


    protected Method generateSetListenerMethod(DefinedClass messageSourceClass, FieldVariable messageProcessor) {
        Method setListener = messageSourceClass.method(Modifier.PUBLIC, context.getCodeModel().VOID, "setListener");
        setListener.javadoc().add("Sets the message processor that will \"listen\" the events generated by this message source");
        setListener.javadoc().addParam("listener Message processor");
        Variable listener = setListener.param(ref(MessageProcessor.class), "listener");
        setListener.body().assign(ExpressionFactory._this().ref(messageProcessor), listener);

        return setListener;
    }

    protected void generateThrow(String bundle, Class<?> clazz, CatchBlock callProcessorCatch, Expression event, String methodName) {
        Variable exception = callProcessorCatch.param("e");
        TypeReference coreMessages = ref(CoreMessages.class);
        Invocation failedToInvoke = coreMessages.staticInvoke(bundle);
        if (methodName != null) {
            failedToInvoke.arg(ExpressionFactory.lit(methodName));
        }
        Invocation messageException = ExpressionFactory._new(ref(clazz));
        messageException.arg(failedToInvoke);
        if (event != null) {
            messageException.arg(event);
        }
        messageException.arg(exception);
        callProcessorCatch.body()._throw(messageException);
    }

    protected class FieldVariableElement {
        private final FieldVariable field;
        private final FieldVariable fieldType;
        private final VariableElement variableElement;

        public FieldVariableElement(FieldVariable field, FieldVariable fieldType, VariableElement variableElement) {
            this.field = field;
            this.fieldType = fieldType;
            this.variableElement = variableElement;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((field == null) ? 0 : field.hashCode());
            result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
            result = prime * result + ((variableElement == null) ? 0 : variableElement.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FieldVariableElement other = (FieldVariableElement) obj;
            if (field == null) {
                if (other.field != null)
                    return false;
            } else if (!field.equals(other.field))
                return false;
            if (fieldType == null) {
                if (other.fieldType != null)
                    return false;
            } else if (!fieldType.equals(other.fieldType))
                return false;
            if (variableElement == null) {
                if (other.variableElement != null)
                    return false;
            } else if (!variableElement.equals(other.variableElement))
                return false;
            return true;
        }

        public FieldVariable getField() {
            return field;
        }

        public FieldVariable getFieldType() {
            return fieldType;
        }

        public VariableElement getVariableElement() {
            return variableElement;
        }
    }


}
