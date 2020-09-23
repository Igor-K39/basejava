package com.urise.webapp.util;

import com.urise.webapp.exception.StorageException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.Writer;

public class XMLParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XMLParser(Class... classesToBeFound) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeFound);
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public void marshall(Object instance, Writer os) {
        try {
            marshaller.marshal(instance, os);
        } catch (JAXBException e) {
            throw new StorageException("Marshalling error", e);
        }
    }

    public <T> T unmarshall(Reader reader) {
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new StorageException("Unmarshalling error", e);
        }
    }
}
