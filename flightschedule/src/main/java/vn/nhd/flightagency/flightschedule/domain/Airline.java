package vn.nhd.flightagency.flightschedule.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "airlines")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airline implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "code", length = 3, unique = true)
    private String code;

    @Column(name = "name", length = 50)
    private String name;


    /*
    Description: this is will auto gen airport_id in schedules
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "airline_id",
            referencedColumnName = "id" //name of col in db
    )
    private List<Schedule> scheduleList;
     */

}
