package vn.nhd.flightagency.account.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeAccessTokenRequestModel {

    private String code;
    private String clientId;
    private String clientSecret;
    private String grantType;
}
