package metro;

public class Connections {
    private Line lineDonor;
    private Station stationDonor;

    private Line lineRecepient;
    private Station stationRecepient;

    public Connections(Line lineDonor, Station stationDonor, Line lineRecepient, Station stationRecepient) {
        this.lineDonor = lineDonor;
        this.stationDonor = stationDonor;
        this.lineRecepient = lineRecepient;
        this.stationRecepient = stationRecepient;
    }

    public Line getLineDonor() {
        return lineDonor;
    }

    public Station getStationDonor() {
        return stationDonor;
    }

    public Line getLineRecepient() {
        return lineRecepient;
    }

    public Station getStationRecepient() {
        return stationRecepient;
    }
}
