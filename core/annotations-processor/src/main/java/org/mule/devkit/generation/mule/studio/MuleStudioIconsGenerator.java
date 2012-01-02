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

package org.mule.devkit.generation.mule.studio;

import org.apache.commons.lang.StringUtils;
import org.mule.api.annotations.display.Icons;
import org.mule.devkit.generation.AbstractMessageGenerator;
import org.mule.devkit.generation.DevKitTypeElement;
import org.mule.devkit.generation.GenerationException;
import org.mule.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MuleStudioIconsGenerator extends AbstractMessageGenerator {

    public static final String ICONS_FOLDER = "icons/";

    @Override
    protected boolean shouldGenerate(DevKitTypeElement typeElement) {
        return !context.hasOption("skipStudioPluginPackage");
    }

    @Override
    protected void doGenerate(DevKitTypeElement typeElement) throws GenerationException {
        Icons icons = typeElement.getAnnotation(Icons.class);
        if (icons != null) {
            copyFile(icons.processorSmall(), "icons/small", typeElement);
            copyFile(icons.sourceSmall(), "icons/small", typeElement);
            copyFile(icons.transformerSmall(), "icons/small", typeElement);
            copyFile(icons.processorLarge(), "icons/large", typeElement);
            copyFile(icons.sourceLarge(), "icons/large", typeElement);
            copyFile(icons.transformerLarge(), "icons/large", typeElement);
        } else {
            copyFile(Icons.GENERIC_CLOUD_CONNECTOR_SMALL, "icons/small", typeElement);
            copyFile(Icons.GENERIC_ENDPOINT_SMALL, "icons/small", typeElement);
            copyFile(Icons.GENERIC_TRANSFORMER_SMALL, "icons/small", typeElement);
            copyFile(Icons.GENERIC_CLOUD_CONNECTOR_LARGE, "icons/large", typeElement);
            copyFile(Icons.GENERIC_ENDPOINT_LARGE, "icons/large", typeElement);
            copyFile(Icons.GENERIC_TRANSFORMER_LARGE, "icons/large", typeElement);
        }
    }

    private void copyFile(String fileName, String folder, DevKitTypeElement typeElement) throws GenerationException {
        String sourcePath = context.getSourceUtils().getPath(typeElement.getInnerTypeElement());
        int packageCount = StringUtils.countMatches(typeElement.getQualifiedName().toString(), ".") + 1;
        while (packageCount > 0) {
            sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
            packageCount--;
        }
        OutputStream outputStream = null;
        try {
            outputStream = createFile(folder, fileName);
            IOUtils.copy(new FileInputStream(new File(sourcePath, fileName)), outputStream);
        } catch (IOException e) {
            throw new GenerationException("Error copy icons to output folder", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private OutputStream createFile(String folder, String fileName) throws GenerationException {
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        try {
            return context.getCodeModel().getCodeWriter().openBinary(null, folder + '/' + fileName);
        } catch (IOException e) {
            throw new GenerationException("Could not create file or folder: " + fileName, e);
        }
    }
}