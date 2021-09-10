package vn.nhd.flightagency.flightschedule.model;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class SearchFlightRequestModel {

    private String sortBy;
    private Long departure;
    private Long arrival;
    private LocalDate depDate;
    private byte babies;
    private byte children;
    private byte adults;
}
