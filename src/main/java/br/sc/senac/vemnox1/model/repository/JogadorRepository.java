package br.sc.senac.vemnox1.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Jogador;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Integer>, JpaSpecificationExecutor<Jogador>{
	UserDetails findUserByEmail(String email);
	
	Optional<Jogador> findByEmail(String email);
}
