package semanticweb.util.dom_parser;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMParserErrorHandler implements ErrorHandler {

	@Override
	public void error(SAXParseException err) throws SAXException {
		// Propagate the exception
		throw err;
	}

	@Override
	public void fatalError(SAXParseException err) throws SAXException {
		// Propagate the exception
		throw err;
	}

	@Override
	public void warning(SAXParseException err) throws SAXException {
		System.out.println("[WARN] Warning, line: " + err.getLineNumber() + ", uri: " + err.getSystemId());
		System.out.println("[WARN] " + err.getMessage());
	}

}
