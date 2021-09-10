package vn.nhd.flightagency.account.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestModel {
    private String password;
    private String code;
}
