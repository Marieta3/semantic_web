package semanticweb.util.fuseki_jena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import semanticweb.util.fuseki_jena.AuthenticationUtilitiesFuseki.ConnectionPropertiesFuseki;
import org.apache.commons.text.StringSubstitutor;
import org.apache.jena.query.*;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//import static publications.util.constants.ApplicationConstants.RDF_FILE_PATH;

public class FusekiManagement {

	//private static final String RDF_FILEPATH = "src/main/resources/data/rdf/paper_metadata.rdf";
	private static final String RDF_FILEPATH = "src/main/resources/data/rdf/onto17_new3_najnovije.rdf";
	private static final String PAPERS_METADATA_GRAPH_URI = "/papersMetadata";
	private static final String QUERY_FILEPATH = "src/main/resources/data/sparql/%s.rq";
	

	public static void saveRDF() throws IOException {

		System.out.println("[INFO] Loading triples from an RDF/XML to a model...");

		ConnectionPropertiesFuseki conn = AuthenticationUtilitiesFuseki.loadProperties();
		// RDF triples which are to be loaded into the model

		// Creates a default model
		Model model = ModelFactory.createDefaultModel();
		model.read(RDF_FILEPATH);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		model.write(out, SparqlUtil.NTRIPLES);

		System.out.println("[INFO] Rendering model as RDF/XML...");
		model.write(System.out, SparqlUtil.RDF_XML);

		// Delete all of the triples in all of the named graphs
		UpdateRequest request = UpdateFactory.create();
		request.add(SparqlUtil.dropAll());

		/*
		 * Create UpdateProcessor, an instance of execution of an UpdateRequest.
		 * UpdateProcessor sends update request to a remote SPARQL update service.
		 */
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, conn.updateEndpoint);
		processor.execute();

		// Creating the first named graph and updating it with RDF data
		System.out.println("[INFO] Writing the triples to a named graph \"" + PAPERS_METADATA_GRAPH_URI + "\".");
		String sparqlUpdate = SparqlUtil.insertData(conn.dataEndpoint + PAPERS_METADATA_GRAPH_URI,
				new String(out.toByteArray()));
		System.out.println(sparqlUpdate);

		// UpdateRequest represents a unit of execution
		UpdateRequest update = UpdateFactory.create(sparqlUpdate);

		processor = UpdateExecutionFactory.createRemote(update, conn.updateEndpoint);
		processor.execute();

	}
	

	public static Map<?,?> executeQuery(String query_id) throws IOException {

		ConnectionPropertiesFuseki conn = AuthenticationUtilitiesFuseki.loadProperties();

		String sparqlQueryTemplate = readFile(String.format(QUERY_FILEPATH, query_id), StandardCharsets.UTF_8);
		System.out.println("Query: " + sparqlQueryTemplate);
		//String sparqlQuery = StringSubstitutor.replace(sparqlQueryTemplate, params, "{{", "}}");
		String sparqlQuery = String.format(sparqlQueryTemplate, conn.dataEndpoint + PAPERS_METADATA_GRAPH_URI);

		System.out.println("Query: " + sparqlQuery);

		// Create a QueryExecution that will access a SPARQL service over HTTP
		QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
		// Query the SPARQL endpoint, iterate over the result set...
		ResultSet results = query.execSelect();

		String varName;
		RDFNode varValue;
		Map<Long, Map<String, String>> found1 = new HashMap<>();
		Long cnt_result = 0L;
		while(results.hasNext()) {

			// A single answer from a SELECT query
			QuerySolution querySolution = results.next() ;
			Iterator<String> variableBindings = querySolution.varNames();

			Map<String, String> iteration = new HashMap<>();
			// Retrieve variable bindings
		    while (variableBindings.hasNext()) {
		    	varName = variableBindings.next();
		    	varValue = querySolution.get(varName);

				String value = "";
				if(varValue.toString().contains("^^")){
					String[] lista1 = varValue.toString().split("\\^");

					value = lista1[0];

				}
				else if(varValue.toString().contains("#")){
					String[] lista1 = varValue.toString().split("#");

					value = lista1[1];
				}
				else{
					value = varValue.toString();
				}
				iteration.put(varName, value);
				System.out.println(varName + ": " + value);
		    }
			found1.put(cnt_result, iteration);
			cnt_result+=1;
		    System.out.println();
		}

	    ResultSetFormatter.outputAsJSON(System.out, results);
		query.close() ;
		System.out.println("[INFO] SPARQL Query End.");
		return found1;
	}
	
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static void deleteRDF() throws Exception
    {
        ConnectionPropertiesFuseki conn = AuthenticationUtilitiesFuseki.loadProperties();
        String spargDelete = SparqlUtil.dropGraph(conn.dataEndpoint +  PAPERS_METADATA_GRAPH_URI);
        
		// UpdateRequest represents a unit of execution
		UpdateRequest update = UpdateFactory.create(spargDelete);

		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, conn.updateEndpoint);
		processor.execute();
    }
}
