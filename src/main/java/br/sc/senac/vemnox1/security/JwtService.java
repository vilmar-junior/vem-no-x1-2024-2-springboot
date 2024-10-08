package br.sc.senac.vemnox1.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	
	@Autowired
    private JwtEncoder jwtEncoder;

//    public JwtService(JwtEncoder jwtEncoder) {
//        this.jwtEncoder = jwtEncoder;
//    }

    public  String getGenereteToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;

        String scope = authentication
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors
                        .joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("exemplo-spring-security-senac") //emissor do token
                .issuedAt(now) // data/hora em que o token foi emitido
                .expiresAt(now.plusSeconds(expiry)) //expiraÃ§Ã£o do token, em segundos. o token Ã© vÃ¡lido como 36000 segundos (10 horas) apÃ³s a geraÃ§Ã£o do token:
                .subject(authentication.getName()) //nome do usuario
                .claim("scope", scope) //perfis ou permissÃµes 'roles' - "ROLE_USER", "ROLE_ADMIN"
                .build();

        return jwtEncoder.encode(
                        JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}