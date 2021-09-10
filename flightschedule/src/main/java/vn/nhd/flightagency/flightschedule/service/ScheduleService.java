package vn.nhd.flightagency.flightschedule.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import vn.nhd.flightagency.flightschedule.domain.Airport;
import vn.nhd.flightagency.flightschedule.domain.Schedule;
import vn.nhd.flightagency.flightschedule.model.SearchFlightRequestModel;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    Page<Schedule> findByDepartureDate(LocalDate departureDate, int page);

    List<Schedule> findByDepartureDate(LocalDate departureDate);

    List<Schedule> searchFlights(SearchFlightRequestModel flights);

    List<Airport> findAllAirport();
}
