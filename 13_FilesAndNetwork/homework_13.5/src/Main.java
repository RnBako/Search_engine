import metro.Connections;
import metro.Line;
import metro.Station;
import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
            //Document document = Jsoup.parse(Files.readString(Path.of("C:\\Users\\AVK\\Documents\\Java\\java_basics\\13_FilesAndNetwork\\homework_13.5\\src\\MoscowMetro.html")));
            Element table = document.select("div#metrodata").select("div.t-text-simple").first();
            Elements rows = table.select("div.js-toggle-depend");
            //Получаем линии метро
            List<Line> lineList = parseLines(rows);

            //Получаем станции метро
            Elements rows1 = table.select("div.js-depend");
            List<Station> stationList = parseStations(rows1, lineList);

            //Получаем переходы метро
            List<Connections> connectionsList = parseConnections(rows1, lineList, stationList);

            //Складываем в JSON
            createJSON(lineList, stationList, connectionsList);

            for (Map.Entry<String, List<Station>> st : stationList.stream().collect(Collectors.groupingBy(sl -> sl.getStationLine().getName())).entrySet()) {
                System.out.println("На " + st.getKey() + " - " + st.getValue().size() + " станций");
            }
            System.out.println();
            System.out.println(connectionsList.size() + " переходов.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<Line> parseLines(Elements rows) {
        List<Line> lineList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            lineList.add(new Line(rows.get(i).select("span.js-metro-line").attr("data-line"), rows.get(i).text()));
        }
        return lineList;
    }

    public static List<Station> parseStations(Elements rows, List<Line> lineList) {
        List<Station> stationList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            String lineNumber = rows.get(i).attr("data-depend-set").substring(6);
            Elements stations = rows.get(i).select("p");

            stations.forEach(station -> {
                if (!station.select("span.name").text().isEmpty()) stationList.add(new Station(
                        lineList.stream().filter(l -> l.getNumber().equals(lineNumber)).collect(Collectors.toList()).get(0),
                        station.select("span.name").text()));
            });
        }
        return stationList;
    }

    public static List<Connections> parseConnections(Elements rows, List<Line> lineList, List<Station> stationList) {
        List<Connections> connectionsList = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            String lineNumber = rows.get(i).attr("data-depend-set").substring(6);
            Elements stations = rows.get(i).select("p");

            stations.forEach(station -> {
                Line currentLine = lineList.stream().filter(l -> l.getNumber().equals(lineNumber)).collect(Collectors.toList()).get(0);
                if (!station.select("span.t-icon-metroln").attr("class").isEmpty()) {

                    connectionsList.add(new Connections(currentLine,
                            stationList.stream().filter(s -> s.getStationName().equals(station.select("span.name").text())&&
                                    s.getStationLine().getNumber().equals(currentLine.getNumber())).collect(Collectors.toList()).get(0),
                            lineList.stream().filter(l ->
                                    l.getNumber().equals(station.select("span.t-icon-metroln").
                                            attr("class").substring(18))).collect(Collectors.toList()).get(0),
                            stationList.stream().filter(s ->
                                    s.getStationName().equals(
                                            station.select("span.t-icon-metroln").attr("title").substring(20,
                                                    station.select("span.t-icon-metroln").attr("title").lastIndexOf('»')))&&
                                            s.getStationLine().getNumber().equals(station.select("span.t-icon-metroln").
                                                    attr("class").substring(18))
                            ).collect(Collectors.toList()).get(0)
                    ));
                }
            });
        }
        return connectionsList;
    }

    public static void createJSON(List<Line> lineList, List<Station> stationList, List<Connections> connectionsList) {
        try {
            //Переводим в JSON формат станции
            JSONObject stationObject = new JSONObject();
            for (Map.Entry<String, List<Station>> st : stationList.stream().collect(Collectors.groupingBy(sl -> sl.getStationLine().getNumber())).entrySet()) {
                List<String> stationName = new ArrayList<>();
                for (Station stName : st.getValue()) stationName.add(stName.getStationName());
                stationObject.put(st.getKey(), stationName);
            }

            //Переводим в JSON формат переходы
            JSONArray connectionsArray = new JSONArray();
            for (Connections connection : connectionsList) {
                JSONArray connectionArray = new JSONArray();
                JSONObject donorObject = new JSONObject();
                JSONObject recepientObject = new JSONObject();
                donorObject.put("Line", connection.getLineDonor().getNumber());
                donorObject.put("station", connection.getStationDonor().getStationName());
                recepientObject.put("Line", connection.getLineRecepient().getNumber());
                recepientObject.put("station", connection.getStationRecepient().getStationName());
                connectionArray.add(donorObject);
                connectionArray.add(recepientObject);
                connectionsArray.add(connectionArray);
            }

            //Переводим в JSON формат линии
            JSONArray lineArray = new JSONArray();
            for (Line line : lineList) {
                JSONObject lineObject = new JSONObject();
                lineObject.put("number", line.getNumber());
                lineObject.put("name", line.getName());
                lineArray.add(lineObject);
            }

            //Создаем объект метро для записи в файл
            JSONObject metro = new JSONObject();
            metro.put("stations", stationObject);
            metro.put("connections", connectionsArray);
            metro.put("lines", lineArray);

            Files.write(Path.of("C:\\Users\\AVK\\Documents\\Java\\java_basics\\13_FilesAndNetwork\\homework_13.5\\files\\MoscowMetro.json"), metro.toJSONString().getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
