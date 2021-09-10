package vn.nhd.flightagency.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.domain.Provider;
import vn.nhd.flightagency.account.repository.AccountRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> account =  accountRepository.findAccountByUsername(s);
        Account account1 = account.orElse(null);
        if(account1 == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return new CustomUserDetailsImpl(account1);
    }
}
