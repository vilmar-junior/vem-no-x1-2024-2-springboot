package br.sc.senac.vemnox1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.sc.senac.vemnox1.auth.AuthenticationService;
import br.sc.senac.vemnox1.model.entity.Jogador;
import br.sc.senac.vemnox1.model.enums.PerfilAcesso;
import br.sc.senac.vemnox1.service.JogadorService;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JogadorService jogadorService;

    /**
     * Método de login padronizado -> Basic Auth
     * 
     *  O parâmetro Authentication já encapsula login (username) e senha (password)
     *  Basic <Base64 encoded username and password>
     * @param authentication
     * @return o JWT gerado
     */
    @PostMapping("/authenticate")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
    
    @PostMapping("/novo-jogador")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registrarJogador(@RequestBody Jogador novoJogador) {
    	
      String senhaCifrada = passwordEncoder.encode(novoJogador.getSenha()); 	
    	
      novoJogador.setSenha(senhaCifrada);
      novoJogador.setPerfil(PerfilAcesso.JOGADOR);
      
      jogadorService.inserir(novoJogador);
    }
      
}
