package br.sc.senac.vemnox1.model.seletor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.sc.senac.vemnox1.model.entity.Carta;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class CartaSeletor extends BaseSeletor implements Specification<Carta>{
	
	private String nome;
	private Integer forcaMinima;
	private Integer forcaMaxima;
	private Integer inteligenciaMinima;
	private Integer inteligenciaMaxima;
	private Integer velocidadeMinima;
	private Integer velocidadeMaxima;
	
	//Filtros da coleção
	private String nomeColecao;
	private String corColecao;
	
	//filtragem de datas por período (início, fim)
	private LocalDate dataInicioCadastro;
	private LocalDate dataFimCadastro;

	/**
	 * Verifica se este seletro tem algum campo preenchido
	 * @return true caso ao menos um dos atributos tenha sido preenchido
	 */
	public boolean temFiltro() {
		return  (this.stringValida(this.nome)) 
			 || (this.stringValida(this.nomeColecao))
			 || (this.stringValida(this.corColecao))
			 || (this.forcaMinima > 0)
			 || (this.forcaMaxima > 0)
		   	 || (this.inteligenciaMinima > 0)
			 || (this.inteligenciaMaxima > 0)
			 || (this.velocidadeMinima > 0)
			 || (this.velocidadeMaxima > 0)
			 || (this.dataInicioCadastro != null)
			 || (this.dataFimCadastro != null);
	}

	@Override
	public Predicate toPredicate(Root<Carta> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		 List<Predicate> predicates = new ArrayList<>();

         if(this.getNome() != null && this.getNome().trim().length() > 0) {
         	// WHERE/AND COLUNA OPERADOR VALOR
         	// WHERE      nome   like    '%Popó%'
            predicates.add(cb.like(root.get("nome"), "%" + this.getNome() + "%"));
         }
         
         if(this.getNomeColecao() != null && this.getNomeColecao().trim().length() > 0) {
         	//Predicado --> operador (comparação), atributo/coluna, valor
         	//Forma 1: usando somente get
         	predicates.add(cb.like(root.get("colecao").get("nome"), "%" + this.getNomeColecao() + "%"));
         }
         
         if(this.getCorColecao() != null && this.getCorColecao().trim().length() > 0) {
         	//Forma 2: usando join
         	predicates.add(cb.like(root.join("colecao").get("cor"), "%" + this.getCorColecao() + "%"));
         }
         
         
         aplicarFiltroIntervalo(root, cb, predicates, this.getForcaMinima(), this.getForcaMaxima(), "forca");
         aplicarFiltroIntervalo(root, cb, predicates, this.getInteligenciaMinima(), this.getInteligenciaMaxima(), "inteligencia");
         aplicarFiltroIntervalo(root, cb, predicates, this.getVelocidadeMinima(), this.getVelocidadeMaxima(), "velocidade");
         aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicioCadastro(), this.getDataFimCadastro(), "dataCadastro");
         
         return cb.and(predicates.toArray(new Predicate[0]));
	}
}
