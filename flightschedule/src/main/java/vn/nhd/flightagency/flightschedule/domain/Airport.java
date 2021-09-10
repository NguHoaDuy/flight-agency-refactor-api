package vn.nhd.flightagency.flightschedule.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "airports")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Airport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "Code", length = 3, unique = true)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "city", length = 50)
    private String city;

}
