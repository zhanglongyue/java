package priv.yue.sboot.auth.realm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

public interface GetAuthorizationAble {

    @JsonIgnore
    Collection<String> getRolesStr();

    @JsonIgnore
    Collection<String> getPermissionsStr();
}
