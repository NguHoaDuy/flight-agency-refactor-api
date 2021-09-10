package vn.nhd.flightagency.account.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.nhd.flightagency.account.annotation.UniqueEmail;
import vn.nhd.flightagency.account.annotation.UniqueUser;

@Getter
@Setter
public class AccountRequestModel {

    @UniqueEmail
    private String email;

    @UniqueUser
    private String username;

    private String password;
}
