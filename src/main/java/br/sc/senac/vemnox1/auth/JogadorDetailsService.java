package br.sc.senac.vemnox1.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.model.repository.JogadorRepository;

@Service
public class JogadorDetailsService implements UserDetailsService {

    public final JogadorRepository jogadorRepository;

    public JogadorDetailsService(JogadorRepository userLogin) {
        this.jogadorRepository = userLogin;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jogadorRepository.findByEmail(username)
				                .orElseThrow(
				                        () -> new UsernameNotFoundException("Usuário não encontrado" + username)
				                );
    }
}
