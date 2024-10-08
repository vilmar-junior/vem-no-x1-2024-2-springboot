package br.sc.senac.vemnox1.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.sc.senac.vemnox1.model.dto.UsuarioLoginDTO;

public class UserAuthenticated implements UserDetails {

    private final Long id;
    private final String login;
    private final String password;

    public UserAuthenticated(UsuarioLoginDTO usuarioLoginDTO) {
        this.id = usuarioLoginDTO.getId();
        this.login = usuarioLoginDTO.getLogin();
        this.password = usuarioLoginDTO.getPassword();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	//TODO devolver as roles do usuário?
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}