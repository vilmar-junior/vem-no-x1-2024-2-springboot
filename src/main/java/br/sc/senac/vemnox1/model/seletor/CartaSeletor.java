package br.sc.senac.vemnox1.model.seletor;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CartaSeletor extends BaseSeletor{
	
	private String nome;
	private int forcaMinima;
	private int forcaMaxima;
	private int inteligenciaMinima;
	private int inteligenciaMaxima;
	private int velocidadeMinima;
	private int velocidadeMaxima;
	
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
