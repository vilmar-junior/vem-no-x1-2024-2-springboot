package br.sc.senac.vemnox1.model.seletor;

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
	
	public int getOffset() {
		return this.limite * (this.pagina - 1);
	}
	
	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	public int getLimite() {
		return limite;
	}

	public void setLimite(int limite) {
		this.limite = limite;
	}
}
