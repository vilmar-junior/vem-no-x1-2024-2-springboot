package br.sc.senac.vemnox1.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.sc.senac.vemnox1.model.enums.PerfilAcesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
//interface incluída para que o Jogador autentique-se via Spring Security
public class Jogador implements UserDetails { 

	private static final long serialVersionUID = 3667682428012659277L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;  
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	@Column(length = 4000)
	private String senha;
	
	@Column(nullable = false)
	private LocalDate dataNascimento;
	
	@JsonBackReference
    @OneToMany(mappedBy = "jogador")
    private List<Partida> partidas;

	private int totalPartidas;
	private double percentualVitorias;
	
	@Enumerated(EnumType.STRING)
	private PerfilAcesso perfil;

	// Métodos da interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(perfil.toString()));

        return list;
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