package vn.nhd.flightagency.account.validation;

import org.springframework.beans.factory.annotation.Autowired;
import vn.nhd.flightagency.account.annotation.UniqueUser;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.repository.AccountRepository;
import vn.nhd.flightagency.account.service.AccountService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<Account> account = accountRepository.findAccountByUsername(value);
        return !account.isPresent();
    }
}
