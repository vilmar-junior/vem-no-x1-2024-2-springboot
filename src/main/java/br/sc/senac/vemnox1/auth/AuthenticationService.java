package br.sc.senac.vemnox1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.model.repository.JogadorRepository;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    
    @Autowired
    private JogadorRepository jogadorRepository;
    
    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.getGenerateToken(authentication);
    }
    
    public Jogador getUsuarioAutenticado() throws VemNoX1Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jogador jogadorAutenticado = null;
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            
            if(principal instanceof Jogador) {
            	jogadorAutenticado = (Jogador) principal;
            }
            
            if(principal instanceof Jwt) {
            	Jwt jwt = (Jwt) principal;
            	String login = jwt.getClaim("sub");
            	
            	jogadorAutenticado = jogadorRepository.findByEmail(login)
            			.orElseThrow(() -> new VemNoX1Exception("Usuário não encontrado"));
            }
        }
        return jogadorAutenticado;
    }
}
