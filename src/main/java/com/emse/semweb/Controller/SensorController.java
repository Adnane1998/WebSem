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
@RequestMapping("/api/sensors")
public class SensorController {



    @GetMapping // (5)
    public List<SensorDto> findAll() {
        List<SensorDto> Sensors =new ArrayList<SensorDto>();
        String datasetURL = "http://localhost:3030/Sensors/";
        String sparqlEndpoint = datasetURL + "sparql";
        String sparqlUpdate = datasetURL + "update";
        String graphStore = datasetURL + "data";

        RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);

        QueryExecution qExec = conneg.query("SELECT ?subject (str(?time) as ?ti) (str(?result) as ?res) ?room WHERE {?subject a ?object.?observation <http://www.w3.org/ns/sosa/madeBySensor> ?subject.?observation <http://www.w3.org/ns/sosa/hasSimpleResult> ?result.?observation <http://www.w3.org/ns/sosa/resultTime> ?time.?observation <http://www.w3.org/ns/sosa/hasFeatureOfInterest> ?room. }")  ;
        ResultSet rs = qExec.execSelect() ;


        while(rs.hasNext()) {
            SensorDto sensor1 =new SensorDto();
            QuerySolution qs = rs.next() ;
            Resource subject = qs.getResource("subject");
            Resource room = qs.getResource("room");
            Literal time = qs.getLiteral("ti");
            Literal res = qs.getLiteral("res");

            sensor1.setName(subject.toString());
            sensor1.setResultTime(time.toString());
            sensor1.setResult(res.toString());
            sensor1.setHostRoom(room.toString());

           Sensors.add(sensor1);

        }


        return  Sensors;  // (6)

    }

}
class SensorDto {

    private Long id;


    private String name;


    private String ObservationId;


    private String ResultTime;
    private String Result;


    private String HostRoom;



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

    public void setHostRoom(String hostRoom) {
        HostRoom = hostRoom;
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

    public String getHostRoom() {
        return HostRoom;
    }

    public String getResult() {
        return Result;
    }
}
