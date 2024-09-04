package br.sc.senac.vemnox1.model.seletor;

import lombok.Data;

@Data
public abstract class BaseSeletor {

	private int pagina;
	private int limite;
	
	public BaseSeletor() {
		this.limite = 0;
		this.pagina = 0;
	}
	
	public boolean temPaginacao() {
		return this.limite > 0 && this.pagina > 0;	
	}
}
