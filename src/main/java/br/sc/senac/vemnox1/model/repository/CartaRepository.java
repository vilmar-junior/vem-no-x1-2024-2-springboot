package br.sc.senac.vemnox1.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Integer>, JpaSpecificationExecutor<Carta>{

	// Construtor deve conter todos os atributos
	// EXATAMENTE NA ORDEM que estão declarados em CartaDTO
	@Query("SELECT new br.sc.senac.vemnox1.model.dto.CartaDTO("
			+ "c.id, c.nome, c.forca, c.inteligencia, c.velocidade, c.dataCadastro,"
	        + " CONCAT('Força: ', c.forca, ', Inteligência: ', c.inteligencia, ', Velocidade: ', c.velocidade), "
	        + " SUM(CASE WHEN cp.pertenceAoJogador = false THEN 1 ELSE 0 END), "
	        + " SUM(CASE WHEN cp.pertenceAoJogador = true THEN 1 ELSE 0 END) AS quantidadeUsosEmPartidasPorAlgumJogador " 
	        + ") "
			+ "FROM CartaNaPartida cp " 
			+ "RIGHT JOIN cp.carta c "   // Fazendo JOIN a partir de CartaNaPartida para Carta
			+ "GROUP BY c.id")
	List<CartaDTO> pesquisarTodasDTO();

	//Exemplo de query nativa (SQL puro), para usar a função RAND() no MYSQL
	@Query(value = "SELECT * FROM carta ORDER BY RAND() LIMIT 6", nativeQuery = true)
	List<Carta> sortearSeisCartasMySQL();

	@Query(value = "SELECT * FROM carta ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
	List<Carta> sortearSeisCartasPostgres();
}
