package vn.nhd.flightagency.account.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 13)
    private String passport;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(8) default 'UNKNOWN'")
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 562048007;
    }
}
