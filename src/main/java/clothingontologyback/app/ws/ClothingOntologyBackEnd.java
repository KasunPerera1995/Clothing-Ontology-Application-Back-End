package clothingontologyback.app.ws;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ClothingOntologyBackEnd {

    @RequestMapping(value = "/ontologies",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getontologies() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator ontologiesIter = model.listOntologies();
            while (ontologiesIter.hasNext()) {
                Ontology ontology = (Ontology) ontologiesIter.next();

                JSONObject obj = new JSONObject();
                obj.put("name",ontology.getLocalName());
                obj.put("uri",ontology.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(path = "/searchcloth/{type}/{brand}/{material}/{sleeves}/{mencollar}/{womencollar}/{collar_size}/{color}/{fit}/{clothing_size}/{pant_size}/{pricefrom}/{priceto}", method = RequestMethod.GET)
    public List<JSONObject> searchcloth(@PathVariable String type, @PathVariable String brand,
                                        @PathVariable String material, @PathVariable String sleeves,
                                        @PathVariable String mencollar, @PathVariable String womencollar,
                                        @PathVariable String collar_size,
                                        @PathVariable String color, @PathVariable String fit,
                                        @PathVariable String clothing_size, @PathVariable String pant_size,
                                        @PathVariable String pricefrom, @PathVariable String priceto) {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String def = "All";
            String hasType = "?cl rdf:type cloth:" + type + ".";
            String hasBrand = "?cl cloth:hasBrand cloth:" + brand + ".";
            String isMadeUpOf = "?cl cloth:hasMaterial cloth:" + material + ".";
            String hasSleeves = "?cl cloth:hasSleeves cloth:" + sleeves + ".";
            String hasMenCollar = "?cl cloth:hasMenCollar cloth:"+ mencollar + ".";
            String hasWomenCollar = "?cl cloth:hasWomenCollar cloth:"+ womencollar + ".";
            String hasColler_size = "?cl cloth:hasCollarSize cloth:" + collar_size + ".";
            String hasColor = "?cl cloth:hasColor cloth:" + color + ".";
            String hasFit = "?cl cloth:hasFitPattern cloth:" + fit + ".";
            String hasClothing_size = "?cl cloth:hasClothingSize cloth:" + clothing_size + ".";
            String hasPantSize = "?cl cloth:hasPantSize cloth:" + pant_size + ".";
            String prize = "?cl cloth:hasPrice ?price.";
            String priceRange = "FILTER (?price > " + pricefrom + " && ?price < " + priceto + ")";

            //String id = "?cloth hasID ?id.";
            String whereend = "}";

            String sprqlStart = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?cl ?price\n" +
                    "\tWHERE {" ;

            String sprql = sprqlStart;


            if(!type.equals(def)) {
                sprql = sprql + hasType;
            }
            if(!brand.equals(def)) {
                sprql = sprql + hasBrand;
            }
            if(!material.equals(def)) {
                sprql = sprql + isMadeUpOf;
            }
            if(!sleeves.equals(def)) {
                sprql = sprql + hasSleeves;
            }
            if(!mencollar.equals(def)) {
                sprql = sprql + hasMenCollar;
            }

            if(!womencollar.equals(def)) {
                sprql = sprql + hasWomenCollar;
            }

            if(!collar_size.equals(def)) {
                sprql = sprql + hasColler_size;
            }


            if(!color.equals(def)) {
                sprql = sprql + hasColor;
            }
            if(!clothing_size.equals(def)) {
                sprql = sprql + hasClothing_size;
            }
            if(!collar_size.equals(def)) {
                sprql = sprql + hasColler_size;
            }
            if(!fit.equals(def)) {
                sprql = sprql + hasFit;
            }

            if(!pant_size.equals(def)) {
                sprql = sprql + hasPantSize;
            }

            sprql = sprql + prize;

            if(!pricefrom.equals(def) && !priceto.equals(def)) {
                sprql = sprql + priceRange;
            }
            sprql = sprql + whereend;
            System.out.println(sprql);

            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                //  System.out.println(solution.get("prize").toString());
                obj.put("id",x);
                obj.put("price",solution.get("price").toString());
                obj.put("Cloth_Model",solution.get("cl").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/brand",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getBrands() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Brand." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/material",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMaterials() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Material." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/sleeves",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getSleeves() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Sleeves." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/collarsize",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getCollarSize() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:CollarSize." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/mencollar",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMenCollar() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Men_dress_collar" +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/womencollar",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getWomenCollar() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Women_dress_collar" +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/color",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getColor() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT DISTINCT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Color." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/clothingsize",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getClothingSize() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT DISTINCT ?subject\n" +
                    "\tWHERE {" +
                    "?subclass rdfs:subClassOf cloth:ClothingSize." +
                    "?subject rdf:type ?subclass." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/fit",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getFit() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:FitPattern." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/men",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getMenClothTypes() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdfs:subClassOf cloth:MenClothing." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/women",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getWomenClothTypes() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdfs:subClassOf cloth:WomenClothing." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/pantsize",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getPantSize() {
        List<JSONObject> list=new ArrayList();
        String fileName = "ClothingOntology.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);

            String sprql =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX cloth:<http://www.semanticweb.org/kasun/ontologies/2019/6/untitled-ontology-5#>\n" +
                    "SELECT ?subject\n" +
                    "\tWHERE {" +
                    "?subject rdf:type cloth:Pants_sizes." +
                    "}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
            int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("id",x);
                obj.put("subject",solution.get("subject").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
