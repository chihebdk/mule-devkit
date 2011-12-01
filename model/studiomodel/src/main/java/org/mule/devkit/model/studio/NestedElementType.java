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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.14 at 10:40:36 AM CDT 
//


package org.mule.devkit.model.studio;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for NestedElementType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="NestedElementType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tooling.attributes}AbstractElementType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="regexp" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="encoding" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="string" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="globalRef" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="long" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}LongType"/>
 *         &lt;element name="transientBoolean" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}Booleantype"/>
 *         &lt;element name="list" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="integer" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}IntegerType"/>
 *         &lt;element name="boolean" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}Booleantype"/>
 *         &lt;element name="password" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}StringAttributeType"/>
 *         &lt;element name="classname" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}ClassType"/>
 *         &lt;element name="text" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}TextType"/>
 *         &lt;element name="name" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="url" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="childElement" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}NestedElementReference"/>
 *         &lt;element name="path" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *         &lt;element name="enum" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}EnumType"/>
 *         &lt;element name="expression" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}ExpressionAttributeType"/>
 *         &lt;element name="file" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}AttributeType"/>
 *       &lt;/choice>
 *       <attribute name="xmlname" type="string"></attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NestedElementType", propOrder = {
        "regexpOrEncodingOrString"
})
public class NestedElementType
        extends AbstractElementType {

    @XmlElementRefs({
            @XmlElementRef(name = "name", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "expression", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "url", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "encoding", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "childElement", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "password", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "text", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "long", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "boolean", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "file", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "list", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "integer", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "transientBoolean", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "regexp", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "globalRef", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "string", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "path", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "classname", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class),
            @XmlElementRef(name = "enum", namespace = "http://www.mulesoft.org/schema/mule/tooling.attributes", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends AttributeType>> regexpOrEncodingOrString;

    @XmlAttribute(name = "xmlname")
    protected String xmlname;

    /**
     * Gets the value of the regexpOrEncodingOrString property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regexpOrEncodingOrString property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegexpOrEncodingOrString().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ExpressionAttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link NestedElementReference }{@code >}
     * {@link JAXBElement }{@code <}{@link StringAttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link TextType }{@code >}
     * {@link JAXBElement }{@code <}{@link LongType }{@code >}
     * {@link JAXBElement }{@code <}{@link Booleantype }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link IntegerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link Booleantype }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link ClassType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttributeType }{@code >}
     * {@link JAXBElement }{@code <}{@link EnumType }{@code >}
     */
    public List<JAXBElement<? extends AttributeType>> getRegexpOrEncodingOrString() {
        if (regexpOrEncodingOrString == null) {
            regexpOrEncodingOrString = new ArrayList<JAXBElement<? extends AttributeType>>();
        }
        return this.regexpOrEncodingOrString;
    }

    public String getXmlname() {
        return xmlname;
    }

    public void setXmlname(String xmlname) {
        this.xmlname = xmlname;
    }
}
