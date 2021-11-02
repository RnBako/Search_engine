import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class XMLHandler extends DefaultHandler {
    private Voter voter;
    //private static SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    //private HashMap<Voter, Integer> voterCounts;
    private HashMap<Integer, WorkTime> voteStationWorkTimes;
    private int maxBulkSize = 10000;
    private int bulkSize = 0;

    public XMLHandler () {
        //voterCounts = new HashMap<>();
        voteStationWorkTimes = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter") && voter == null) {
                DBConnection.countVoter(attributes.getValue("name"), attributes.getValue("birthDay"));
                bulkSize++;
            } else  if (qName.equals("visit") && voter != null) {
                int station = Integer.parseInt(attributes.getValue("station"));
                Date time = visitDateFormat.parse(attributes.getValue("time"));
                WorkTime workTime = voteStationWorkTimes.get(station);
                if (workTime == null) {
                    workTime = new WorkTime();
                    voteStationWorkTimes.put(station, workTime);
                }
                workTime.addVisitTime(time.getTime());
            } else if (qName.equals("visit")) {
                int station = Integer.parseInt(attributes.getValue("station"));
                Date time = visitDateFormat.parse(attributes.getValue("time"));
                WorkTime workTime = voteStationWorkTimes.get(station);
                if (workTime == null) {
                    workTime = new WorkTime();
                    voteStationWorkTimes.put(station, workTime);
                }
                workTime.addVisitTime(time.getTime());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (bulkSize == maxBulkSize) {
                DBConnection.executeMultiInsert();
                bulkSize = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            DBConnection.executeMultiInsert();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDuplicatedVoters () throws SQLException {
        System.out.println("Duplicated voters: ");
        DBConnection.printVoterCounts();
    }

    public void printStationWorkTimes() {
        System.out.println("Voting station work times: ");
        for (Integer votingStation : voteStationWorkTimes.keySet()) {
            WorkTime workTime = voteStationWorkTimes.get(votingStation);
            System.out.println("\t" + votingStation + " - " + workTime);
        }
    }
}
