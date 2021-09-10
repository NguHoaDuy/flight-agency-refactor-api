package vn.nhd.flightagency.flightschedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nhd.flightagency.flightschedule.domain.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @EntityGraph(value = "schedule-with-airline-name", type = EntityGraph.EntityGraphType.FETCH)
    Page<Schedule> findByDepartureDate(LocalDate departureDate, Pageable pageable);

    @EntityGraph(value = "schedule-with-airline-name", type = EntityGraph.EntityGraphType.FETCH)
    List<Schedule> findByDepartureDate(LocalDate departureDate);

    @Query(value =
            "select s from Schedule s where " +
                    "s.departureAirport.id = ?1 and " +
                    "s.arrivalAirport.id = ?2 and " +
                    "(s.departureDate >= ?3 and s.departureTime >= ?4) and " +
                    "(s.departureDate <= ?5 and s.departureTime <= ?6)"
    )
    @EntityGraph(value = "schedule-with-airline-name", type = EntityGraph.EntityGraphType.FETCH)
    List<Schedule> findAllFlightSchedules(
            Long depCode, Long arrCode,
            LocalDate depDateFrom, LocalTime depTimeFrom,
            LocalDate depDateTo, LocalTime depTimeTo,
            Sort sortBy
    );
}
