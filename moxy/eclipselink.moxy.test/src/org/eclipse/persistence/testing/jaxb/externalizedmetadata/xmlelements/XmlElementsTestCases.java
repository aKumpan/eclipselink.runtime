/*******************************************************************************
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * dmccann - November 27/2009 - 2.0 - Initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.externalizedmetadata.xmlelements;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.testing.jaxb.externalizedmetadata.ExternalizedMetadataTestCases;
import org.eclipse.persistence.testing.jaxb.externalizedmetadata.xmlelementwrapper.Employee;
import org.w3c.dom.Document;

/**
 * Tests XmlElements via eclipselink-oxm.xml
 *
 */
public class XmlElementsTestCases extends ExternalizedMetadataTestCases {
    private static final String CONTEXT_PATH = "org.eclipse.persistence.testing.jaxb.externalizedmetadata.xmlelements";
    private static final String PATH = "org/eclipse/persistence/testing/jaxb/externalizedmetadata/xmlelements/";
    
    /**
     * This is the preferred (and only) constructor.
     * 
     * @param name
     */
    public XmlElementsTestCases(String name) {
        super(name);
    }
    
    /**
     * Tests @XmlElements schema generation via eclipselink-oxm.xml.  
     * 
     * Positive test.
     */
    public void testXmlElementsSchemaGen() {
        MySchemaOutputResolver outputResolver = generateSchema(CONTEXT_PATH, PATH, 1);
        // validate schema
        String controlSchema = PATH + "schema.xsd";
        compareSchemas(outputResolver.schemaFiles.get(EMPTY_NAMESPACE), new File(controlSchema));
    }

    /**
     * Tests @XmlElements schema generation via eclipselink-oxm.xml.  Here, an
     * xml-element-wrapper is also used.
     * 
     * Positive test.
     */
    public void testXmlElementsWithWrapperSchemaGen() {
        String metadataFile = PATH + "eclipselink-oxm-wrapper.xml";
        InputStream iStream = loader.getResourceAsStream(metadataFile);
        if (iStream == null) {
            fail("Couldn't load metadata file [" + metadataFile + "]");
        }

        MySchemaOutputResolver outputResolver = generateSchema(new Class[] { Foo.class }, CONTEXT_PATH, iStream, 1);
        
        // validate schema
        String controlSchema = PATH + "schema_wrapper.xsd";
        compareSchemas(outputResolver.schemaFiles.get(EMPTY_NAMESPACE), new File(controlSchema));
    }

    /**
     * Tests @XmlElements schema generation via eclipselink-oxm.xml.  Here, an
     * xml-element-wrapper and xml-idref are used.
     * 
     * Positive test.
     */
    public void testXmlElementsWithIdRefSchemaGen() {
        String metadataFile = PATH + "eclipselink-oxm-idref.xml";
        InputStream iStream = loader.getResourceAsStream(metadataFile);
        if (iStream == null) {
            fail("Couldn't load metadata file [" + metadataFile + "]");
        }

        MySchemaOutputResolver outputResolver = generateSchema(new Class[] { Bar.class, Address.class, Phone.class }, CONTEXT_PATH, iStream, 1);
        
        // validate schema
        String controlSchema = PATH + "schema_idref.xsd";
        compareSchemas(outputResolver.schemaFiles.get(EMPTY_NAMESPACE), new File(controlSchema));
    }

    /**
     * Tests @XmlElements schema generation via eclipselink-oxm.xml.  Here, an
     * xml-idref is used, but one of the elements in not an XmlID.
     * 
     * Negative test.
     */
    public void testXmlElementsWithInvalidIdRef() {
        String metadataFile = PATH + "eclipselink-oxm-idref-invalid.xml";
        InputStream iStream = loader.getResourceAsStream(metadataFile);
        if (iStream == null) {
            fail("Couldn't load metadata file [" + metadataFile + "]");
        }
        HashMap<String, Source> metadataSourceMap = new HashMap<String, Source>();
        metadataSourceMap.put(CONTEXT_PATH, new StreamSource(iStream));
        Map<String, Map<String, Source>> properties = new HashMap<String, Map<String, Source>>();
        properties.put(JAXBContextFactory.ECLIPSELINK_OXM_XML_KEY, metadataSourceMap);

        // create context
        boolean exception = false;
        JAXBContext jCtx = null;
        try {
            jCtx = (JAXBContext) JAXBContextFactory.createContext(new Class[] { Bar.class, Address.class, Foo.class }, properties);
        } catch (JAXBException e1) {
            exception = true;
        }
        assertTrue("The expected exception did not occur.", exception);
    }

    /**
     * Tests @XmlElements via eclipselink-oxm.xml.
     * 
     * Positive test.
     */
    public void testXmlElements() {
        // load XML metadata
        String metadataFile = PATH + "eclipselink-oxm.xml";
        InputStream iStream = loader.getResourceAsStream(metadataFile);
        if (iStream == null) {
            fail("Couldn't load metadata file [" + metadataFile + "]");
        }

        HashMap<String, Source> metadataSourceMap = new HashMap<String, Source>();
        metadataSourceMap.put(CONTEXT_PATH, new StreamSource(iStream));
        Map<String, Map<String, Source>> properties = new HashMap<String, Map<String, Source>>();
        properties.put(JAXBContextFactory.ECLIPSELINK_OXM_XML_KEY, metadataSourceMap);

        // create context
        JAXBContext jCtx = null;
        try {
            jCtx = (JAXBContext) JAXBContextFactory.createContext(new Class[] { Foo.class }, properties);
        } catch (JAXBException e1) {
            e1.printStackTrace();
            fail("JAXBContext creation failed.");
        }
        
        // load instance doc
        String src = PATH + "foo.xml";
        InputStream iDocStream = loader.getResourceAsStream(src);
        if (iDocStream == null) {
            fail("Couldn't load instance doc [" + src + "]");
        }

        // unmarshal
        Object obj = null;
        Unmarshaller unmarshaller = jCtx.createUnmarshaller();
        try {
            obj = unmarshaller.unmarshal(iDocStream);
            assertFalse("Unmarshalled object is null.", obj == null);
        } catch (JAXBException e) {
            e.printStackTrace();
            fail("Unmarshal operation failed.");
        }

        Document testDoc = parser.newDocument();
        Document ctrlDoc = parser.newDocument();
        try {
            ctrlDoc = getControlDocument(src);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception occurred loading control document [" + src + "].");
        }

        // marshal
        Marshaller marshaller = jCtx.createMarshaller();
        try {
            marshaller.marshal(obj, testDoc);
            assertTrue("Document comparison failed unxepectedly: ", compareDocuments(ctrlDoc, testDoc));
        } catch (JAXBException e) {
            e.printStackTrace();
            fail("Unmarshal operation failed.");
        }
    }

    /**
     * Tests @XmlElements via eclipselink-oxm.xml.  Here an xml-element-wrapper
     * is also used.
     * 
     * Positive test.
     */
    public void testXmlElementsWithWrapper() {
        String metadataFile = PATH + "eclipselink-oxm-wrapper.xml";
        InputStream iStream = loader.getResourceAsStream(metadataFile);
        if (iStream == null) {
            fail("Couldn't load metadata file [" + metadataFile + "]");
        }

        HashMap<String, Source> metadataSourceMap = new HashMap<String, Source>();
        metadataSourceMap.put(CONTEXT_PATH, new StreamSource(iStream));
        Map<String, Map<String, Source>> properties = new HashMap<String, Map<String, Source>>();
        properties.put(JAXBContextFactory.ECLIPSELINK_OXM_XML_KEY, metadataSourceMap);

        // create context
        JAXBContext jCtx = null;
        try {
            jCtx = (JAXBContext) JAXBContextFactory.createContext(new Class[] { Foo.class }, properties);
        } catch (JAXBException e1) {
            e1.printStackTrace();
            fail("JAXBContext creation failed.");
        }
        
        // load instance doc
        String src = PATH + "foo-wrapper.xml";
        InputStream iDocStream = loader.getResourceAsStream(src);
        if (iDocStream == null) {
            fail("Couldn't load instance doc [" + src + "]");
        }

        // unmarshal
        Object obj = null;
        Unmarshaller unmarshaller = jCtx.createUnmarshaller();
        try {
            obj = unmarshaller.unmarshal(iDocStream);
            assertFalse("Unmarshalled object is null.", obj == null);
        } catch (JAXBException e) {
            e.printStackTrace();
            fail("Unmarshal operation failed.");
        }

        Document testDoc = parser.newDocument();
        Document ctrlDoc = parser.newDocument();
        try {
            ctrlDoc = getControlDocument(src);
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception occurred loading control document [" + src + "].");
        }

        // marshal
        Marshaller marshaller = jCtx.createMarshaller();
        try {
            marshaller.marshal(obj, testDoc);
            assertTrue("Document comparison failed unxepectedly: ", compareDocuments(ctrlDoc, testDoc));
        } catch (JAXBException e) {
            e.printStackTrace();
            fail("Unmarshal operation failed.");
        }
    }
}
