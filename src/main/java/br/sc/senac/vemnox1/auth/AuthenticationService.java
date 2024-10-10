package br.sc.senac.vemnox1.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.sc.senac.vemnox1.exception.VemNoX1Exception;
import br.sc.senac.vemnox1.model.entity.Jogador;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    
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
            
            if (principal instanceof Jogador) {
                UserDetails userDetails = (Jogador) principal;
                jogadorAutenticado = (Jogador) userDetails; 
            } 
        }
        
        if(jogadorAutenticado == null) {
			throw new VemNoX1Exception("Usuário não encontrado");
		}
        
        return jogadorAutenticado;
    }
}
