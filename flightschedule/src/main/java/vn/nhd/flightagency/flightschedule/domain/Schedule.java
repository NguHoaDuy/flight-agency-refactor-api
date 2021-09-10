package vn.nhd.flightagency.flightschedule.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(
        name = "schedules",
        indexes = {
                @Index(name = "idx_date", columnList = "departure_date"),
                @Index(name = "idx_time", columnList = "departure_time, arrival_time")
        }
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NamedEntityGraph(
        name = "schedule-with-airline-name",
        attributeNodes = {
                @NamedAttributeNode(value = "airline"),
                @NamedAttributeNode(value = "departureAirport"),
                @NamedAttributeNode(value = "arrivalAirport")
        }
        /*
        subgraphs = {
                @NamedSubgraph(
                        name = "name-only",
                        attributeNodes = @NamedAttributeNode("name")
                ),
                @NamedSubgraph(
                        name = "dep-name-only",
                        attributeNodes = @NamedAttributeNode("name")
                ),
                @NamedSubgraph(
                        name = "arr-name-only",
                        attributeNodes = @NamedAttributeNode("name")
                )
        }
        */

)
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

    @Column(name = "flight_id", length = 10)
    private String flightId;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "capacity")
    private byte capacity;

    @Column(name ="price")
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "airline_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "departure_airport_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "arrival_airport_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private Airport arrivalAirport;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Schedule schedule = (Schedule) o;

        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return 1998924127;
    }
}

