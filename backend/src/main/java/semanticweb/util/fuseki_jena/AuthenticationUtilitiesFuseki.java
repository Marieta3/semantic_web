package semanticweb.util.fuseki_jena;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AuthenticationUtilitiesFuseki {
	/**
	 * ExampleProperties represents the configuration for the examples.
	 */
	static public class ConnectionPropertiesFuseki {

		public String endpoint;
		public String dataset;
		
		public String queryEndpoint;
		public String updateEndpoint;
		public String dataEndpoint;
		

		public ConnectionPropertiesFuseki(Properties props) {
			super();
			dataset = props.getProperty("conn.dataset").trim();
			endpoint = props.getProperty("conn.endpoint").trim();
			
			queryEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.query").trim());
			updateEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.update").trim());
			dataEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.data").trim());
			
			System.out.println("[INFO] Parsing connection properties:");
			System.out.println("[INFO] Query endpoint: " + queryEndpoint);
			System.out.println("[INFO] Update endpoint: " + updateEndpoint);
			System.out.println("[INFO] Graph store endpoint: " + dataEndpoint);
		}
	}

	/**
	 * Read the configuration properties for the example.
	 * 
	 * @return the configuration object
	 */
	public static ConnectionPropertiesFuseki loadProperties() throws IOException {
		String propsName = "fuseki_jena.properties";

		InputStream propsStream = openStream(propsName);
		if (propsStream == null)
			throw new IOException("Could not read properties " + propsName);

		Properties props = new Properties();
		props.load(propsStream);

		return new ConnectionPropertiesFuseki(props);
	}

	/**
	 * Read a resource for an example.
	 * 
	 * @param fileName
	 *            the name of the resource
	 * @return an input stream for the resource
	 * @throws IOException
	 */
	public static InputStream openStream(String fileName) throws IOException {
		return AuthenticationUtilitiesFuseki.class.getClassLoader().getResourceAsStream(fileName);
	}
}
