package br.sc.senac.vemnox1.model.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.sc.senac.vemnox1.model.entity.Carta;
import br.sc.senac.vemnox1.model.seletor.CartaSeletor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CartaSpecification {
	/*
	 * 
	 * root: é uma referência à entidade raiz que você está consultando. 
	 * No contexto do JPA, isso representa a tabela do banco de dados correspondente 
	 * à entidade Carta.
	 * 
     * query: O parâmetro query representa a consulta JPA que está sendo construída. 
     * Ele é usado para adicionar cláusulas WHERE, JOIN, ORDER BY, entre outras, 
     * à consulta.
     * 
     * cb (CriteriaBuilder): é uma interface que oferece métodos para construção 
     * de cláusulas de consulta de forma programática. 
     * Você usa métodos deste objeto para criar expressões de predicado,
     * funções agregadas e outras partes da consulta.
     * 
	 * */
    public static Specification<Carta> comFiltros(CartaSeletor seletor) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(seletor.getNome() != null && seletor.getNome().trim().length() > 0) {
            	// WHERE/AND COLUNA OPERADOR VALOR
            	// WHERE      nome   like    '%Popó%'
               predicates.add(cb.like(root.get("nome"), "%" + seletor.getNome() + "%"));
            }
            
            if(seletor.getNomeColecao() != null && seletor.getNomeColecao().trim().length() > 0) {
            	//Predicado --> operador (comparação), atributo/coluna, valor
            	//Forma 1: usando somente get
            	predicates.add(cb.like(root.get("colecao").get("nome"), "%" + seletor.getNomeColecao() + "%"));
            }
            
            if(seletor.getCorColecao() != null && seletor.getCorColecao().trim().length() > 0) {
            	//Forma 2: usando join
            	predicates.add(cb.like(root.join("colecao").get("cor"), "%" + seletor.getCorColecao() + "%"));
            }
            
            
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getForcaMinima(), seletor.getForcaMaxima(), "forca");
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getInteligenciaMinima(), seletor.getInteligenciaMaxima(), "inteligencia");
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getVelocidadeMinima(), seletor.getVelocidadeMaxima(), "velocidade");

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

	private static void aplicarFiltroPeriodo(Root<Carta> root, 
			CriteriaBuilder cb, List<Predicate> predicates,
			Integer valorMinimo, Integer valorMaximo, String nomeAtributo) {
		  if(valorMinimo != null && valorMaximo != null) {
          	//WHERE atributo BETWEEN min AND max
          	predicates.add(cb.between(root.get(nomeAtributo), valorMinimo, valorMaximo));
          } else if(valorMinimo != null) {
          	//WHERE atributo >= min
          	predicates.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), valorMinimo));
          } else if(valorMaximo != null) {
          	//WHERE atributo <= max
          	predicates.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), valorMaximo));
          }
	}
}