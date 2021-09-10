package vn.nhd.flightagency.account.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.domain.Provider;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(value = "account-user")
    Optional<Account> findAccountByUsernameAndProvider(String username, Provider provider);

    @EntityGraph(value = "account-user")
    Optional<Account> findAccountByUsername(String username);

    @EntityGraph(value = "account-user")
    Optional<Account> findAccountByEmail(String email);

    @EntityGraph(value = "account-user")
    Optional<Account> findAccountByVerificationCode(String verificationCode);

    @EntityGraph(value = "account-user")
    Optional<Account> findAccountByResetPasswordCode(String verificationCode);
}
