package converting;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ConvertPlanXmlToCsv {

    public static void main(String[] args) {
        try {
            File inputFile = new File(args[0]);  // input path eg: "./output_plan.xml"
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            PrintWriter writer = new PrintWriter(new FileWriter(args[1]));  // output path eg: "./output_plan.csv"
            writer.println("Person ID,Element Type, Activity Type,Link,X Coordinate,Y Coordinate,End Time,Mode,Dep Time,Trav Time,Route Type,Start Link,End Link,Route Trav Time,Distance,Vehicle Ref ID, Route");

            NodeList nList = doc.getElementsByTagName("person");
            System.out.println("Total persons: " + nList.getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String personId = eElement.getAttribute("id");

                    // Check for selected plans
                    NodeList plans = eElement.getElementsByTagName("plan");
                    for (int planIndex = 0; planIndex < plans.getLength(); planIndex++) {
                        Element plan = (Element) plans.item(planIndex);
                        if (plan.getAttribute("selected").equals("yes")) {
                            processActivities(writer, plan, personId);
                            processLegs(writer, plan, personId);
                        }
                    }
                }
            }
            writer.close();
            System.out.println("Finished writing to CSV");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processActivities(PrintWriter writer, Element planElement, String personId) {
        NodeList activities = planElement.getElementsByTagName("activity");
        for (int count = 0; count < activities.getLength(); count++) {
            Node node = activities.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element activity = (Element) node;
                writer.printf("%s,Activity,%s,%s,%s,%s,%s,,,,,,,,,,\n",
                        personId,
                        activity.getAttribute("type"),
                        activity.getAttribute("link"),
                        activity.getAttribute("x"),
                        activity.getAttribute("y"),
                        activity.getAttribute("end_time"));
            }
        }
    }

    private static void processLegs(PrintWriter writer, Element planElement, String personId) {
        NodeList legs = planElement.getElementsByTagName("leg");
        for (int count = 0; count < legs.getLength(); count++) {
            Node node = legs.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element leg = (Element) node;
                NodeList routes = leg.getElementsByTagName("route");
                if (routes.getLength() > 0) {
                    Element route = (Element) routes.item(0);
                    writer.printf("%s,Leg,,,,,,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            personId,
                            leg.getAttribute("mode"),
                            leg.getAttribute("dep_time"),
                            leg.getAttribute("trav_time"),
                            route.getAttribute("type"),
                            route.getAttribute("start_link"),
                            route.getAttribute("end_link"),
                            route.getAttribute("trav_time"),
                            route.getAttribute("distance"),
                            route.getAttribute("vehicleRefId"),
                            route.getTextContent().trim());
                } else {
                    // Handle legs without routes
                    writer.printf("%s,Leg,,,,%s,%s,%s,,,,,,,\n",
                            personId,
                            leg.getAttribute("mode"),
                            leg.getAttribute("dep_time"),
                            leg.getAttribute("trav_time"));
                }
            }
        }
    }

}
