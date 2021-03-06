/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mule.devkit.doclet;

import org.mule.devkit.doclet.apicheck.AbstractMethodInfo;

import java.util.ArrayList;
import java.util.List;

public class ConstructorInfo implements AbstractMethodInfo {

    private boolean mIsVarargs;
    private String mName;
    private String mType;
    private boolean mIsStatic;
    private boolean mIsFinal;
    private boolean mIsDeprecated;
    private String mScope;
    private List<String> mExceptions;
    private List<ParameterInfo> mParameters;
    private SourcePositionInfo mSourcePosition;
    private ClassInfo mClass;

    public ConstructorInfo(String name, String type, boolean isStatic, boolean isFinal,
                           String deprecated, String scope, SourcePositionInfo pos, ClassInfo clazz) {
        mName = name;
        mType = type;
        mIsStatic = isStatic;
        mIsFinal = isFinal;
        mIsDeprecated = "deprecated".equals(deprecated);
        mScope = scope;
        mExceptions = new ArrayList<String>();
        mParameters = new ArrayList<ParameterInfo>();
        mSourcePosition = pos;
        mClass = clazz;
    }

    public void setDeprecated(boolean deprecated) {
        mIsDeprecated = deprecated;
    }

    public void addParameter(ParameterInfo pInfo) {
        mParameters.add(pInfo);
    }

    public void addException(String exec) {
        mExceptions.add(exec);
    }

    public String getHashableName() {
        StringBuilder result = new StringBuilder();
        result.append(name());
        for (ParameterInfo pInfo : mParameters) {
            result.append(":").append(pInfo.typeName());
        }
        return result.toString();
    }

    public SourcePositionInfo position() {
        return mSourcePosition;
    }

    public String name() {
        return mName;
    }

    public String qualifiedName() {
        String baseName = (mClass != null) ? (mClass.qualifiedName() + ".") : "";
        return baseName + name();
    }

    public String prettySignature() {
        String params = "";
        for (ParameterInfo pInfo : mParameters) {
            if (params.length() > 0) {
                params += ", ";
            }
            params += pInfo.typeName();
        }
        return qualifiedName() + '(' + params + ')';
    }

    public boolean isConsistent(ConstructorInfo mInfo) {
        boolean consistent = true;

        if (mIsFinal != mInfo.mIsFinal) {
            consistent = false;
            Errors.error(Errors.CHANGED_FINAL, mInfo.position(), "Constructor " + mInfo.qualifiedName()
                    + " has changed 'final' qualifier");
        }

        if (mIsStatic != mInfo.mIsStatic) {
            consistent = false;
            Errors.error(Errors.CHANGED_FINAL, mInfo.position(), "Constructor " + mInfo.qualifiedName()
                    + " has changed 'static' qualifier");
        }

        if (!mScope.equals(mInfo.mScope)) {
            consistent = false;
            Errors.error(Errors.CHANGED_SCOPE, mInfo.position(), "Constructor " + mInfo.qualifiedName()
                    + " changed scope from " + mScope + " to " + mInfo.mScope);
        }

        if (!mIsDeprecated == mInfo.mIsDeprecated) {
            consistent = false;
            Errors.error(Errors.CHANGED_DEPRECATED, mInfo.position(), "Constructor "
                    + mInfo.qualifiedName() + " has changed deprecation state");
        }

        for (String exec : mExceptions) {
            if (!mInfo.mExceptions.contains(exec)) {
                Errors.error(Errors.CHANGED_THROWS, mInfo.position(), "Constructor "
                        + mInfo.qualifiedName() + " no longer throws exception " + exec);
                consistent = false;
            }
        }

        for (String exec : mInfo.mExceptions) {
            if (!mExceptions.contains(exec)) {
                Errors.error(Errors.CHANGED_THROWS, mInfo.position(), "Constructor "
                        + mInfo.qualifiedName() + " added thrown exception " + exec);
                consistent = false;
            }
        }

        return consistent;
    }

    @Override
    public void setVarargs(boolean varargs) {
        mIsVarargs = varargs;
    }

    public boolean isVarArgs() {
        return mIsVarargs;
    }

}
