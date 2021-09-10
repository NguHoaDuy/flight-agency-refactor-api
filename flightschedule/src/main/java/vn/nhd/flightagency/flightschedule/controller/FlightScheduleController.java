package vn.nhd.flightagency.flightschedule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.nhd.flightagency.flightschedule.config.FlightUri;
import vn.nhd.flightagency.flightschedule.domain.Airport;
import vn.nhd.flightagency.flightschedule.domain.Schedule;
import vn.nhd.flightagency.flightschedule.model.SearchFlightRequestModel;
import vn.nhd.flightagency.flightschedule.service.ScheduleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(FlightUri.API_VERSION)
public class FlightScheduleController {

    @Autowired
    ScheduleService scheduleService;


    /**
     * test
     */
    @GetMapping("/flight-schedule/{page}")
    public ResponseEntity<List<Schedule>> search(@PathVariable(name = "page") int page,
                                                 @RequestParam(name ="departureDate", required = true) String departureDate) {
        LocalDate tmp = LocalDate.parse(departureDate);
        List<Schedule> schedules = scheduleService.findByDepartureDate(tmp);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping(FlightUri.SEARCH)
    public ResponseEntity<List<Schedule>> searchFlightSchedule(@RequestBody SearchFlightRequestModel searchFlightRequestModel) {
        List<Schedule> schedules = scheduleService.searchFlights(searchFlightRequestModel);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping(FlightUri.AIRPORT)
    public ResponseEntity<List<Airport>> retrieveAirport() {
        List<Airport> airports = scheduleService.findAllAirport();
        return ResponseEntity.ok(airports);
    }
}
