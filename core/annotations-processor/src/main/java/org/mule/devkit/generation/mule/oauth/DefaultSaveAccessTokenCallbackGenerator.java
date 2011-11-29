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
package org.mule.devkit.generation.mule.oauth;

import org.mule.api.annotations.oauth.OAuth;
import org.mule.api.annotations.oauth.OAuth2;
import org.mule.api.oauth.SaveAccessTokenCallback;
import org.mule.devkit.generation.AbstractMessageGenerator;
import org.mule.devkit.generation.DevKitTypeElement;
import org.mule.devkit.generation.GenerationException;
import org.mule.devkit.model.code.DefinedClass;
import org.mule.devkit.model.code.FieldVariable;
import org.mule.devkit.model.code.Method;
import org.mule.devkit.model.code.Modifier;
import org.mule.devkit.model.code.Modifiers;

import javax.lang.model.element.TypeElement;

public class DefaultSaveAccessTokenCallbackGenerator extends AbstractMessageGenerator {

    public static final String ROLE = "DefaultSaveAccessTokenCallback";

    @Override
    protected boolean shouldGenerate(DevKitTypeElement typeElement) {
        if (typeElement.hasAnnotation(OAuth.class) || typeElement.hasAnnotation(OAuth2.class)) {
            return true;
        }

        return false;
    }

    @Override
    protected void doGenerate(DevKitTypeElement typeElement) throws GenerationException {
        DefinedClass callbackClass = getDefaultSaveAccessTokenCallbackClass(typeElement);

        FieldVariable messageProcessor = generateFieldForMessageProcessor(callbackClass, "messageProcessor");

        generateGetter(callbackClass, messageProcessor);
        generateSetter(callbackClass, messageProcessor);

        Method saveAccessTokenMethod = callbackClass.method(Modifier.PUBLIC, context.getCodeModel().VOID, "saveAccessToken");
        saveAccessTokenMethod.param(ref(String.class), "accessToken");
        saveAccessTokenMethod.param(ref(String.class), "accessTokenSecret");
    }

    private DefinedClass getDefaultSaveAccessTokenCallbackClass(TypeElement type) {
        String callbackClassName = context.getNameUtils().generateClassNameInPackage(type, ".config", "DefaultSaveAccessTokenCallback");
        org.mule.devkit.model.code.Package pkg = context.getCodeModel()._package(context.getNameUtils().getPackageName(callbackClassName));
        DefinedClass clazz = pkg._class(context.getNameUtils().getClassName(callbackClassName), new Class[]{
                SaveAccessTokenCallback.class});
        context.setClassRole(ROLE, clazz);

        return clazz;
    }
}
