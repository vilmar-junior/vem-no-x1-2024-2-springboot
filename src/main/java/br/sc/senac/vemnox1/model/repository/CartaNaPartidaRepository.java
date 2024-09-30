package br.sc.senac.vemnox1.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.entity.CartaNaPartida;
import br.sc.senac.vemnox1.model.entity.CartaNaPartidaPk;


@Repository
public interface CartaNaPartidaRepository extends JpaRepository<CartaNaPartida, CartaNaPartidaPk>, JpaSpecificationExecutor<CartaNaPartida>{

	//Exemplo com JPQL
	@Query("SELECT cp FROM CartaNaPartida cp WHERE "
			+ " cp.partida.id = :idPartida AND cp.carta.id = :idCarta")
	CartaNaPartida findByJogada(int idPartida, int idCarta);
	
	 // Exemplo com JPQL para contar a quantidade de partidas jogadas pela CPU
    @Query("SELECT COUNT(cp) FROM CartaNaPartida cp WHERE cp.carta.id = :idCarta AND cp.pertenceAoJogador = :pertenceAoJogador")
    long countByPartidas(Integer idCarta, Boolean pertenceAoJogador);

    // Exemplo com JPQL para contar a quantidade de partidas jogadas pela CPU
    @Query("SELECT COUNT(cp) FROM CartaNaPartida cp WHERE cp.carta.id = :idCarta")
	long countByIdCarta(Integer idCarta);
	
}
