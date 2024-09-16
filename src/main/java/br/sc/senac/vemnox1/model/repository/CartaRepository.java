package br.sc.senac.vemnox1.model.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.sc.senac.vemnox1.model.dto.CartaDTO;
import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;

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
			+ "JOIN cp.carta c "   // Fazendo JOIN a partir de CartaNaPartida para Carta
			+ "GROUP BY c.id")
	ArrayList<CartaDTO> pesquisarTodasDTO();

	//Exemplo de query nativa (SQL puro), para usar a função RAND() no MYSQL
	@Query(value = "SELECT * FROM carta ORDER BY RAND() LIMIT 6", nativeQuery = true)
	ArrayList<Carta> sortearSeisCartasMySQL();

	@Query(value = "SELECT * FROM carta ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
	ArrayList<Carta> sortearSeisCartasPostgres();

	//Exemplo de query com JPQL e operadores com Spring Expression Language (SpEL) (:#{#seletor.nome}). 
	//Isso permite que os valores do seletor sejam utilizados diretamente nos filtros.
	//https://www.baeldung.com/spring-expression-language
	
	//TODO como melhorar os filtros?
    @Query("SELECT new br.sc.senac.vemnox1.model.dto.CartaDTO("
            + "  c.id, c.nome, c.forca, c.inteligencia, c.velocidade, c.dataCadastro, "
            + "  CONCAT('Força: ', c.forca, ', Inteligência: ', c.inteligencia, ', Velocidade: ', c.velocidade), " 
            + "  SUM(CASE WHEN cp.pertenceAoJogador = false THEN 1 ELSE 0 END), "
	        + "  SUM(CASE WHEN cp.pertenceAoJogador = true THEN 1 ELSE 0 END) AS quantidadeUsosEmPartidasPorAlgumJogador " 
            + ") " //fim do construtor de CartaDTO
            + "FROM CartaNaPartida cp " 
			+ "JOIN cp.carta c "   // Fazendo JOIN a partir de CartaNaPartida para Carta
            + "WHERE (:#{#seletor.nome} IS NULL OR c.nome LIKE %:#{#seletor.nome}%) "
            + "AND (:#{#seletor.forcaMinima} IS NULL OR c.forca >= :#{#seletor.forcaMinima}) " 
            + "AND (:#{#seletor.forcaMaxima} IS NULL OR c.forca <= :#{#seletor.forcaMaxima}) " 
            + "AND (:#{#seletor.inteligenciaMinima} IS NULL OR c.inteligencia >= :#{#seletor.inteligenciaMinima}) " 
            + "AND (:#{#seletor.inteligenciaMaxima} IS NULL OR c.inteligencia <= :#{#seletor.inteligenciaMaxima}) " 
            + "AND (:#{#seletor.velocidadeMinima} IS NULL OR c.velocidade >= :#{#seletor.velocidadeMinima}) " 
            + "AND (:#{#seletor.velocidadeMaxima} IS NULL OR c.velocidade <= :#{#seletor.velocidadeMaxima}) " 
            + "AND (:#{#seletor.dataInicioCadastro} IS NULL OR c.dataCadastro >= :#{#seletor.dataInicioCadastro}) " 
            + "AND (:#{#seletor.dataFimCadastro} IS NULL OR c.dataCadastro <= :#{#seletor.dataFimCadastro})")
    List<CartaDTO> pesquisarComSeletorDTO(CartaSeletor seletor);

}
