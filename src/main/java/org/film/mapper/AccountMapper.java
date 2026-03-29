package org.film.mapper;

import org.film.dto.request.account.AccountRegistration;
import org.film.dto.response.AccountInfo;
import org.film.entity.AccountEntity;
import org.film.entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "accountRegistration.name")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "email", source = "accountRegistration.email")
    @Mapping(target = "encodePassword", source = "encodePassword")
    AccountEntity map(AccountRegistration accountRegistration, String encodePassword, Role role);

    @Mapping(target = "role", source = "role.name")
    AccountInfo mapAccountInfo(AccountEntity account);
}
