package org.film.util;


import org.film.entity.AccountEntity;
import org.film.entity.Role;

public class SecurityContextUtils {

//    TODO временное решение
    public static AccountEntity getAccount() {
        Role role = new Role();
        role.setId(1L);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setRole(role);
        return accountEntity;
    }

    //    TODO временное решение
    public static Boolean isAdmin() {
        return true;
    }

}
