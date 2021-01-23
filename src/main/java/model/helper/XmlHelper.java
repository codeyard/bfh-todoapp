package model.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.UserManager;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.logging.Logger;

/**
 * Helper methods for reading and writing XML data.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class XmlHelper {
    private static final String fileName = "WEB-INF/xml/Data.xml";
    private static final Logger LOGGER = Logger.getLogger(XmlHelper.class.getName());

    /**
     * Reads XML data.
     *
     * @param servletContext the context of the current servlet
     * @return a User Manager
     */
    public static UserManager readXmlData(ServletContext servletContext) {
        ObjectMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        LOGGER.info(" - - - - Read XML data from file " + fileName + " - - - - ");
        try (InputStream in = new FileInputStream(servletContext.getRealPath(fileName))) {
            return mapper.readValue(in, UserManager.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Writes XML data.
     *
     * @param userManager    the User Manager
     * @param servletContext the context of the current servlet
     */
    public static void writeXmlData(UserManager userManager, ServletContext servletContext) {
        ObjectMapper mapper = new XmlMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LOGGER.info(" - - - - Write data to XML file " + fileName + " - - - - ");
        try (OutputStream out = new FileOutputStream(servletContext.getRealPath(fileName))) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, userManager);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
