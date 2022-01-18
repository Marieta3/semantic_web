package semanticweb.util.dom_parser;

import static org.apache.xerces.jaxp.JAXPConstants.JAXP_SCHEMA_LANGUAGE;
import static org.apache.xerces.jaxp.JAXPConstants.W3C_XML_SCHEMA;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
public class DOMParser {

	public Document buildDocument(String xml, String xsdPath)
			throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		factory.setValidating(false);

		factory.setNamespaceAware(true);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		
		File file = new File(xsdPath);
		String constant = XMLConstants.W3C_XML_SCHEMA_NS_URI;
        SchemaFactory xsdFactory = SchemaFactory.newInstance(constant);
        Schema schema = null;
		schema = xsdFactory.newSchema(file);
		
		
		factory.setSchema(schema);
			
		DocumentBuilder builder = factory.newDocumentBuilder();
		

		/* Postavlja error handler. */
		builder.setErrorHandler(new DOMParserErrorHandler());
		
		document = builder.parse(new InputSource(xml));

		/* Detektuju eventualne greske */
		if (document != null)
			System.out.println("[INFO] File parsed with no errors.");
		else
			System.out.println("[WARN] Document is null.");
		return document;
	}
}
