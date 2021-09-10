package vn.nhd.flightagency.flightschedule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.nhd.flightagency.flightschedule.domain.Airport;
import vn.nhd.flightagency.flightschedule.domain.Schedule;
import vn.nhd.flightagency.flightschedule.model.SearchFlightRequestModel;
import vn.nhd.flightagency.flightschedule.repository.AirportRepository;
import vn.nhd.flightagency.flightschedule.repository.ScheduleRepository;
import vn.nhd.flightagency.flightschedule.service.ScheduleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    AirportRepository airportRepository;

    @Override
    public Page<Schedule> findByDepartureDate(LocalDate departureDate, int page) {
        return scheduleRepository.findByDepartureDate(departureDate, PageRequest.of(page, 10));
    }

    @Override
    public List<Schedule> findByDepartureDate(LocalDate departureDate) {
        return scheduleRepository.findByDepartureDate(departureDate);
    }

    @Override
    public List<Schedule> searchFlights(SearchFlightRequestModel flights) {
        LocalDateTime from;
        if (flights.getDepDate().compareTo(LocalDate.now()) == 0) {
            LocalDateTime now = LocalDateTime.now();
            from = now.plusHours(4);
        } else {
            from = LocalDateTime.of(flights.getDepDate(), LocalTime.of(0, 0));
        }
        LocalDateTime to = LocalDateTime.of(flights.getDepDate(), LocalTime.of(23, 59));
        Sort sort = getSort(flights.getSortBy()) ;
        return scheduleRepository.findAllFlightSchedules(
                flights.getDeparture(), flights.getArrival(),
                from.toLocalDate(), from.toLocalTime(),
                to.toLocalDate(), to.toLocalTime(),
                sort);
    }

    @Override
    public List<Airport> findAllAirport() {
        return airportRepository.findAll();
    }

    private Sort getSort(String type) {
        if ("".equals(type))
            return null;
        if ("priceDown".equals(type)) {
            return Sort.by(Sort.Direction.DESC, sortType.get(type));
        }
        return Sort.by(sortType.get(type));
    }

    private final Map<String, String> sortType = new HashMap<>();
    {
        sortType.put("priceUp", "price");
        sortType.put("priceDown", "price");
        sortType.put("time", "departureTime");
        sortType.put("branch", "airline.name");
    }

}
