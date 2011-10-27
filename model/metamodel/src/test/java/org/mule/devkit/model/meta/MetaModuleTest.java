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
package org.mule.devkit.model.meta;

import org.junit.Test;
import org.mule.tck.FunctionalTestCase;

public class MetaModuleTest extends FunctionalTestCase {
    @Test
    public void testParseSchema() throws Exception {
        MetaModule module = MetaModel.getModel(muleContext).getModule("http://www.mulesoft.org/schema/mule/sfdc");
        assertEquals("sfdc", module.getName());
        assertEquals("http://www.mulesoft.org/schema/mule/sfdc", module.getNamespace());
        assertNotNull(module.getDescription());
        assertEquals("org.mule.devkit.model.meta.MetaModule", module.getModuleClass().getName());
    }

    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }
}
