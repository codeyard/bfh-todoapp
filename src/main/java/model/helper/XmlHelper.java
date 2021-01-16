package model.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.UserManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class XmlHelper {
    private static final String xmlOutputFileName = "Data.xml";
    private static final Logger LOGGER = Logger.getLogger(XmlHelper.class.getName());

    public static void writeXmlData(UserManager userManager){
        ObjectMapper mapper = new XmlMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LOGGER.info(" - - - - Write to file " + xmlOutputFileName + " - - - - ");
        try (OutputStream out = new FileOutputStream(xmlOutputFileName)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, userManager);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
