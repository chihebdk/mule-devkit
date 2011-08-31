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

package org.mule.devkit.module.validation;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.callback.InterceptCallback;
import org.mule.api.annotations.callback.SourceCallback;
import org.mule.api.annotations.oauth.OAuth;
import org.mule.api.annotations.oauth.OAuthAccessToken;
import org.mule.api.annotations.oauth.OAuthAccessTokenSecret;
import org.mule.api.annotations.oauth.OAuthConsumerKey;
import org.mule.api.annotations.oauth.OAuthConsumerSecret;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.InboundHeaders;
import org.mule.api.annotations.param.InvocationHeaders;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.Payload;
import org.mule.devkit.validation.ValidationException;
import org.mule.devkit.validation.Validator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.lang.annotation.Annotation;
import java.util.List;

public class ModuleValidator implements Validator {

    public void validate(Element element) throws ValidationException {
        // if it not annotated just skip
        if (element.getAnnotation(Module.class) == null)
            return;

        // must not be an interface
        if (element.getKind() == ElementKind.INTERFACE) {
            throw new ValidationException(element, "@Module cannot be applied to an interface");
        }

        // must not have type parameters
        if (!((TypeElement) element).getTypeParameters().isEmpty()) {
            throw new ValidationException(element, "@Module type cannot have type parameters");
        }

        // must be public
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ValidationException(element, "@Module must be public");
        }

        List<VariableElement> variables = ElementFilter.fieldsIn(element.getEnclosedElements());
        for (VariableElement variable : variables) {
            Configurable configurable = variable.getAnnotation(Configurable.class);
            if (configurable == null)
                continue;

            Optional optional = variable.getAnnotation(Optional.class);
            Default def = variable.getAnnotation(Default.class);

            if (variable.getModifiers().contains(Modifier.FINAL)) {
                throw new ValidationException(variable, "@Configurable cannot be applied to field with final modifier");
            }

            if (variable.getModifiers().contains(Modifier.STATIC)) {
                throw new ValidationException(variable, "@Configurable cannot be applied to field with static modifier");
            }

            if (variable.asType().getKind().isPrimitive() && optional != null && (def == null || def.value().length() == 0)) {
                throw new ValidationException(variable, "@Optional @Configurable fields can only be applied to non-primitive types with a @Default value");
            }
        }

        // verify that every @Processor is public and non-static and non-generic
        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());
        for (ExecutableElement executableElement : executableElements) {
            Processor processor = executableElement.getAnnotation(Processor.class);

            if (processor == null)
                continue;

            if (executableElement.getModifiers().contains(Modifier.STATIC)) {
                throw new ValidationException(executableElement, "@Processor cannot be applied to a static method");
            }

            if (executableElement.getTypeParameters().size() > 0) {
                throw new ValidationException(executableElement, "@Processor cannot be applied to a generic method");
            }

            if (!executableElement.getModifiers().contains(Modifier.PUBLIC)) {
                throw new ValidationException(executableElement, "@Processor cannot be applied to a non-public method");
            }

            if (processor.intercepting()) {
                // verify that every @Processor(intercepting=true) receives a SourceCallback
                boolean containsInterceptCallback = false;
                List<? extends VariableElement> parameters = executableElement.getParameters();
                for (VariableElement parameter : parameters) {
                    if (parameter.asType().toString().contains(InterceptCallback.class.getName())) {
                        containsInterceptCallback = true;
                    }
                }

                if (!containsInterceptCallback) {
                    throw new ValidationException(executableElement, "An intercepting processor method must contain a InterceptCallback as one of its parameters");
                }
            }

            List<? extends VariableElement> parameters = executableElement.getParameters();
            for (VariableElement parameter : parameters) {
                int count = 0;
                if (parameter.getAnnotation(InboundHeaders.class) != null)
                    count++;
                if (parameter.getAnnotation(InvocationHeaders.class) != null)
                    count++;
                if (parameter.getAnnotation(Payload.class) != null)
                    count++;

                if( count > 1 ) {
                    throw new ValidationException(parameter, "You cannot have more than one of InboundHeader, InvocationHeaders or Payload annotation");
                }
            }

        }

        // verify that every @Source is public and non-static and non-generic
        for (ExecutableElement executableElement : executableElements) {
            Source source = executableElement.getAnnotation(Source.class);

            if (source == null)
                continue;

            if (executableElement.getModifiers().contains(Modifier.STATIC)) {
                throw new ValidationException(executableElement, "@Source cannot be applied to a static method");
            }

            if (executableElement.getTypeParameters().size() > 0) {
                throw new ValidationException(executableElement, "@Source cannot be applied to a generic method");
            }

            if (!executableElement.getModifiers().contains(Modifier.PUBLIC)) {
                throw new ValidationException(executableElement, "@Source cannot be applied to a non-public method");
            }

            // verify that every @Source receives a SourceCallback
            boolean containsSourceCallback = false;
            List<? extends VariableElement> parameters = executableElement.getParameters();
            for (VariableElement parameter : parameters) {
                if (parameter.asType().toString().contains(SourceCallback.class.getName())) {
                    containsSourceCallback = true;
                }
            }

            if (!containsSourceCallback) {
                throw new ValidationException(executableElement, "@Source method must contain a SourceCallback as one of its parameters");
            }
        }

        if (element.getAnnotation(OAuth.class) != null) {
            checkClassHasFieldWithAnnotation(element, OAuthConsumerKey.class, "@OAuth class must contain a field annotated with @OAuthConsumerKey");
            checkClassHasFieldWithAnnotation(element, OAuthConsumerSecret.class, "@OAuth class must contain a field annotated with @OAuthConsumerSecret");
        } else if (classHasMethodWithParameterAnnotated(element, OAuthAccessToken.class)) {
            throw new ValidationException(element, "Cannot annotate parameter with @OAuthAccessToken without annotating the class with @OAuth");
        } else if (classHasMethodWithParameterAnnotated(element, OAuthAccessTokenSecret.class)) {
            throw new ValidationException(element, "Cannot annotate parameter with @OAuthAccessTokenSecret without annotating the class with @OAuth");
        }

        // verify that every @Transformer is public and non-static and non-generic
        // verify that every @Transformer signature is Object x(Object);

        // verify that every @Filter is public and non-static and non-generic
    }

    private void checkClassHasFieldWithAnnotation(Element element, Class<? extends Annotation> annotation, String errorMessage) throws ValidationException {
        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
        for (VariableElement field : fields) {
            if (field.getAnnotation(annotation) != null) {
                return;
            }
        }
        throw new ValidationException(element, errorMessage);
    }

    private boolean classHasMethodWithParameterAnnotated(Element element, Class<? extends Annotation> annotation) throws ValidationException {
        List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
        for (ExecutableElement method : methods) {
            for(VariableElement parameter : method.getParameters()) {
                if(parameter.getAnnotation(annotation) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}