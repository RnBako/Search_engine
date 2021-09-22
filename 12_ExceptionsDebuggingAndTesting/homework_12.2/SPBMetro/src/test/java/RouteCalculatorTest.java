import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorTest extends TestCase {

    private static RouteCalculator routeCalculator ;
    private static StationIndex stationIndex;

    @Override
    protected void setUp() throws Exception {
        stationIndex = new StationIndex();
        try {
            JSONParser parser = new JSONParser();
            StringBuilder builder = new StringBuilder();
            List<String> lines = Files.readAllLines(Paths.get("C:/Users/AVK/Documents/Java/java_basics/12_ExceptionsDebuggingAndTesting/homework_12.2/SPBMetro/src/main/resources/map.json"));
            lines.forEach(line -> builder.append(line));
            JSONObject jsonData = (JSONObject) parser.parse(builder.toString());


            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            linesArray.forEach(lineObject -> {
                JSONObject lineJsonObject = (JSONObject) lineObject;
                Line line = new Line(
                        ((Long) lineJsonObject.get("number")).intValue(),
                        (String) lineJsonObject.get("name")
                );
                stationIndex.addLine(line);
            });

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            stationsObject.keySet().forEach(lineNumberObject ->
            {
                int lineNumber = Integer.parseInt((String) lineNumberObject);
                Line line = stationIndex.getLine(lineNumber);
                JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
                stationsArray.forEach(stationObject ->
                {
                    Station station = new Station((String) stationObject, line);
                    stationIndex.addStation(station);
                    line.addStation(station);
                });
            });

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            connectionsArray.forEach(connectionObject ->
            {
                JSONArray connection = (JSONArray) connectionObject;
                List<Station> connectionStations = new ArrayList<>();
                connection.forEach(item ->
                {
                    JSONObject itemObject = (JSONObject) item;
                    int lineNumber = ((Long) itemObject.get("line")).intValue();
                    String stationName = (String) itemObject.get("station");

                    Station station = stationIndex.getStation(stationName, lineNumber);
                    if (station == null) {
                        throw new IllegalArgumentException("core.Station " +
                                stationName + " on line " + lineNumber + " not found");
                    }
                    connectionStations.add(station);
                });
                stationIndex.addConnection(connectionStations);
            });
            routeCalculator = new RouteCalculator(stationIndex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void testGetShortestRoute() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Девяткино"), stationIndex.getStation("Академическая"));
        List<Station> expected = new ArrayList<>();
        expected.add(stationIndex.getStation("Девяткино"));
        expected.add(stationIndex.getStation("Гражданский проспект"));
        expected.add(stationIndex.getStation("Академическая"));
        assertEquals(expected, actual);
    }

    public void testCalculateDuration() {
        List<Station> route = routeCalculator.getShortestRoute(stationIndex.getStation("Беговая"), stationIndex.getStation("Ладожская"));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 23.5;
        assertEquals(expected, actual);
    }

    public void testGetRouteOnTheLine() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Девяткино"), stationIndex.getStation("Академическая"));
        List<Station> expected = new ArrayList<>();
        expected.add(stationIndex.getStation("Девяткино"));
        expected.add(stationIndex.getStation("Гражданский проспект"));
        expected.add(stationIndex.getStation("Академическая"));
        assertEquals(expected, actual);
    }

    public void testGetRouteWithOneConnection() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Василеостровская"), stationIndex.getStation("Горьковская"));
        List<Station> expected = new ArrayList<>();
        expected.add(stationIndex.getStation("Василеостровская"));
        expected.add(stationIndex.getStation("Гостиный двор"));
        expected.add(stationIndex.getStation("Невский проспект"));
        expected.add(stationIndex.getStation("Горьковская"));
        assertEquals(expected, actual);
    }

    public void testGetRouteWithTwoConnections() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Адмиралтейская"), stationIndex.getStation("Елизаровская"));
        List<Station> expected = new ArrayList<>();
        expected.add(stationIndex.getStation("Адмиралтейская"));
        expected.add(stationIndex.getStation("Садовая"));
        expected.add(stationIndex.getStation("Сенная площадь"));
        expected.add(stationIndex.getStation("Невский проспект"));
        expected.add(stationIndex.getStation("Гостиный двор"));
        expected.add(stationIndex.getStation("Маяковская"));
        expected.add(stationIndex.getStation("Площадь Александра Невского"));
        expected.add(stationIndex.getStation("Елизаровская"));
        assertEquals(expected, actual);
    }
}
