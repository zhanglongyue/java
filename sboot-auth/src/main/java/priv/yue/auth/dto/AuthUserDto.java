package priv.yue.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class AuthUserDto implements Serializable {

    private static final long serialVersionUID = 685998961226965202L;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private boolean rememberMe = false;
}
