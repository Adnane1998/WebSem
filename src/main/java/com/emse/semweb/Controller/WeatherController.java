package com.emse.semweb.Controller;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController // (1)
@RequestMapping("/api/weather")
public class WeatherController {



    @GetMapping // (5)
    public List<WeatherDto> findAll() {
        List<WeatherDto> Sensors2 =new ArrayList<WeatherDto>();
        String datasetURL = "http://localhost:3030/Weather/";
        String sparqlEndpoint = datasetURL + "sparql";
        String sparqlUpdate = datasetURL + "update";
        String graphStore = datasetURL + "data";

        RDFConnection conneg2 = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);

        QueryExecution qExec = conneg2.query("SELECT ?subject (str(?time) as ?ti) (str(?result) as ?res)  WHERE {?subject a ?object.?observation <http://www.w3.org/ns/sosa/madeBySensor> ?subject.?observation <http://www.w3.org/ns/sosa/hasSimpleResult> ?result.?observation <http://www.w3.org/ns/sosa/resultTime> ?time }")  ;
        ResultSet rs = qExec.execSelect() ;


        while(rs.hasNext()) {
            WeatherDto sensor1 =new WeatherDto();
            QuerySolution qs = rs.next() ;
            Resource subject = qs.getResource("subject");

            Literal time = qs.getLiteral("ti");
            Literal res = qs.getLiteral("res");

            sensor1.setName(subject.toString());
            sensor1.setResultTime(time.toString());
            sensor1.setResult(res.toString());


            Sensors2.add(sensor1);

        }


        return  Sensors2;  // (6)

    }

}
class WeatherDto {

    private Long id;


    private String name;


    private String ObservationId;


    private String ResultTime;
    private String Result;





    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObservationId(String observationId) {
        ObservationId = observationId;
    }

    public void setResultTime(String resultTime) {
        ResultTime = resultTime;
    }

    public void setResult(String result) {
        Result = result;
    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getObservationId() {
        return ObservationId;
    }

    public String getResultTime() {
        return ResultTime;
    }



    public String getResult() {
        return Result;
    }
}
