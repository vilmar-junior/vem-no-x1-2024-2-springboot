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
	 * à entidade Produto.
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
            	// WHERE      nome   like    %Café%
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" 
                		+ seletor.getNome().toLowerCase() + "%"));
            }
            
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getForcaMinima(), seletor.getForcaMaxima(), "forca");
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getInteligenciaMinima(), seletor.getInteligenciaMaxima(), "inteligencia");
            aplicarFiltroPeriodo(root, cb, predicates, seletor.getVelocidadeMinima(), seletor.getVelocidadeMaxima(), "velocidade");

            //EXEMPLO para um atributo intervalado
//          if(seletor.getPesoMinimo() != null && seletor.getPesoMaximo() != null) {
//          	//WHERE peso BETWEEN min AND max
//          	predicates.add(cb.between(root.get("peso"), seletor.getPesoMinimo(), 
//          			seletor.getPesoMaximo()));
//          } else if(seletor.getPesoMinimo() != null) {
//          	//WHERE peso >= min
//          	predicates.add(cb.greaterThanOrEqualTo(root.get("peso"), seletor.getPesoMinimo()));
//          } else if(seletor.getPesoMaximo() != null) {
//          	//WHERE peso <= max
//          	predicates.add(cb.lessThanOrEqualTo(root.get("peso"), seletor.getPesoMaximo()));
//          }
          
        //EXEMPLO para um atributo de outra tabela
//          if(seletor.getCnpjFabricante() != null 
//          		&& !seletor.getCnpjFabricante().isEmpty()) {
//          	/* select p.* from produtos p 
//				   inner join fabricantes f on p.id_fabricante = f.id  
//				   where f.cnpj = '88222333000022'
//          	 * */
//          	predicates.add(cb.equal(root.join("fabricanteDoProduto").get("cnpj"), 
//          					seletor.getCnpjFabricante()));
//          }
          
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

	private static void aplicarFiltroPeriodo(Root<Carta> root, CriteriaBuilder cb, List<Predicate> predicates,
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