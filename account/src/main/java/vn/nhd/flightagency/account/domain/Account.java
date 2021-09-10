package vn.nhd.flightagency.account.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(
        name = "account-user",
        attributeNodes = {
               @NamedAttributeNode("user")
        }
)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        }
)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) default 'LOCAL'")
    private Provider provider;

    @Column(columnDefinition = "varchar(10) default 'ROLE_USER'")
    private String role;

    @Column(length = 64)
    private String verificationCode;

    @Column(length = 64)
    private String resetPasswordCode;

    @Column(columnDefinition = "boolean default false")
    private Boolean enabled;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;

        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return 2083479647;
    }
}
