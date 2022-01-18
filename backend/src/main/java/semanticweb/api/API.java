package semanticweb.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
import semanticweb.util.dom_parser.DOMParser;
import semanticweb.util.fuseki_jena.FusekiManagement;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class API {

    @GetMapping("/{id}")
    public ResponseEntity<?> getFunc(@PathVariable("id") Long id){

        return new ResponseEntity<>("bla "+id, HttpStatus.OK);
    }

    @GetMapping(value = "/saveRDF")
    public void saveRdf() throws IOException {
        FusekiManagement.saveRDF();
    }

    @GetMapping(value = "/query/{query_id}")
    public ResponseEntity<?> executeQuery(@PathVariable("query_id") String query_id) throws IOException {
        return new ResponseEntity<>(FusekiManagement.executeQuery(query_id), HttpStatus.OK);
    }

//    @GetMapping(value = "/parseXML")
//    public void parseXML() throws IOException, ParserConfigurationException, SAXException {
//        DOMParser dp = new DOMParser();
//        dp.buildDocument("src/main/resources/data/xml/question1.xml", "src/main/resources/question_qti.xsd");
//    }
}
