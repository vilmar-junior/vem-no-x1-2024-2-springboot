package br.sc.senac.vemnox1.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Integer>{

	@Query("SELECT p FROM Partida p WHERE p.jogador.id = :idJogador")
	List<Partida> findByIdJogador(int idJogador);

}
