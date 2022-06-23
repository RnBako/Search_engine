package metro;

public class Station {
    private Line stationLine;
    private String stationName;

    public Station(Line stationLine, String stationName) {
        this.stationLine = stationLine;
        this.stationName = stationName;
    }

    public Line getStationLine() {
        return stationLine;
    }

    public String getStationName() {
        return stationName;
    }
}
