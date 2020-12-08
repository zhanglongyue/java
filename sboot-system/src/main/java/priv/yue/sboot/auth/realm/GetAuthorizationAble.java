package priv.yue.sboot.auth.realm;

import java.util.Collection;

public interface GetAuthorizationAble {

    Collection<String> getRolesStr();

    Collection<String> getPermissionsStr();
}
