import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;
import com.skillbox.airport.Terminal;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List <Flight> flightList = findPlanesLeavingInTheNextTwoHours(Airport.getInstance());
        flightList.forEach(System.out::println);
    }

    public static List<Flight> findPlanesLeavingInTheNextTwoHours(Airport airport) {
        //TODO Метод должден вернуть список рейсов вылетающих в ближайшие два часа.
        List <Flight> flightList = new ArrayList<>();
        for (Terminal terminal : airport.getTerminals()) {
            flightList.addAll(terminal.getFlights());
        }

        long nextTwoHours = 2 * 60 * 60 * 1000;
        return flightList.stream().filter(f -> f.getType() == Flight.Type.DEPARTURE)
                                  .filter(f -> f.getDate().after(new Date(System.currentTimeMillis())) &&
                                               f.getDate().before(new Date(System.currentTimeMillis() + nextTwoHours))).
                                  collect(Collectors.toList());
    }

}