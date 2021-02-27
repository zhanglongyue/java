package priv.yue.sboot.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

public interface GetAuthorizationAble {

    @JsonIgnore
    Collection<String> getRolesStr();

    @JsonIgnore
    Collection<String> getPermissionsStr();
}
