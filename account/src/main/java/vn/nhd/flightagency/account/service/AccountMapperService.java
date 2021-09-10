package vn.nhd.flightagency.account.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import vn.nhd.flightagency.account.domain.Account;
import vn.nhd.flightagency.account.model.AccountRequestModel;

@Mapper
public interface AccountMapperService {

    AccountMapperService INSTANCE = Mappers.getMapper(AccountMapperService.class);

    @Mapping(target = "role", constant = "ROLE_USER")
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "provider", constant = "LOCAL")
    Account accountMapper(AccountRequestModel accountRequestModel);
}
