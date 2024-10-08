package br.sc.senac.vemnox1.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.sc.senac.vemnox1.model.enums.PerfilAcesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Jogador implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;  
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	private String senha;
	
	@Column(nullable = false)
	private LocalDate dataNascimento;
	
	@JsonBackReference
    @OneToMany(mappedBy = "jogador")
    private List<Partida> partidas;

	private int totalPartidas;
	private double percentualVitorias;
	
	private PerfilAcesso perfil;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//TODO
		
		return null;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
}