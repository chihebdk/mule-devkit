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
package org.mule.devkit.it;

import org.mule.api.store.ObjectStore;
import org.mule.api.store.ObjectStoreException;

import java.io.Serializable;

public class CustomObjectStore implements ObjectStore {

    public boolean contains(Serializable key) throws ObjectStoreException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void store(Serializable key, Serializable value) throws ObjectStoreException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Serializable retrieve(Serializable key) throws ObjectStoreException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Serializable remove(Serializable key) throws ObjectStoreException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isPersistent() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
