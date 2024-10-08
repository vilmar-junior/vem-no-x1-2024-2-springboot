package br.sc.senac.vemnox1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioLoginDTO {
    
	private Long id;
	private String login;
    private String password;
}