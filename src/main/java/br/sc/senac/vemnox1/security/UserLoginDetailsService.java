package br.sc.senac.vemnox1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.model.dto.UsuarioLoginDTO;
import br.sc.senac.vemnox1.model.entity.UsuarioLogin;
import br.sc.senac.vemnox1.model.repository.UsuarioLoginRepository;


@Service
public class UserLoginDetailsService implements UserDetailsService {

	@Autowired
    private UsuarioLoginRepository userLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userLoginRepository.findByLogin(username)
                .map(usuarioLogin -> new UserAuthenticated(ConvertDTO(usuarioLogin)))
                .orElseThrow(
                        () -> new UsernameNotFoundException("UsuÃ¡rio nÃ£o encontrado" + username)
                );
    }

    public UsuarioLoginDTO ConvertDTO(UsuarioLogin usuarioLogin){
        return new UsuarioLoginDTO(usuarioLogin.getId(), usuarioLogin.getLogin(), usuarioLogin.getPassword());
    }
}
