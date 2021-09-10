package vn.nhd.flightagency.account.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.nhd.flightagency.account.annotation.UniqueEmail;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.repository.AccountRepository;
import vn.nhd.flightagency.account.service.AccountService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Account> account = accountRepository.findAccountByEmail(s);
        return !account.isPresent();
    }
}
