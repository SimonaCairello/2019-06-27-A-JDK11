package it.polito.tdp.crimes.model;

public class VerticePeso {
	
	private String vertice;
	private Integer peso;
	
	public VerticePeso(String vertice, Integer peso) {
		this.vertice = vertice;
		this.peso = peso;
	}

	public String getVertice() {
		return vertice;
	}

	public void setVertice(String vertice) {
		this.vertice = vertice;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "VerticePeso [vertice=" + vertice + ", peso=" + peso + "]";
	}
	
	

}
