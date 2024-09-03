package br.sc.senac.vemnox1.model.seletor;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CartaSeletor extends BaseSeletor{
	
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
		return  (this.nome != null && this.nome.trim().length() > 0) 
			 || (this.forcaMinima > 0)
			 || (this.forcaMaxima > 0)
		   	 || (this.inteligenciaMinima > 0)
			 || (this.inteligenciaMaxima > 0)
			 || (this.velocidadeMinima > 0)
			 || (this.velocidadeMaxima > 0)
			 || (this.dataInicioCadastro != null)
			 || (this.dataFimCadastro != null);
	}
}
