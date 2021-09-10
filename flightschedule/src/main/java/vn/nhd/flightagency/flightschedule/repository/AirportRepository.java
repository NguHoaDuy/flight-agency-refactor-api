package vn.nhd.flightagency.flightschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.nhd.flightagency.flightschedule.domain.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
}
