package com.tesis.gipro.estructuras;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posibilidades")
public class evaluaciones {

	@Id
	private String id;
	
	private String[] posibles;
	
	private Double valor;
	
	private Double[] notas;
	
	private String curso;
	


	public evaluaciones(String id, String[] posibles, Double valor, Double[] notas, String curso) {
		super();
		this.id = id;
		this.posibles = posibles;
		this.valor = valor;
		this.notas = notas;
		this.curso = curso;
	}



	public String getCurso() {
		return curso;
	}



	public void setCurso(String curso) {
		this.curso = curso;
	}



	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String[] getPosibles() {
		return posibles;
	}


	public void setPosibles(String[] posibles) {
		this.posibles = posibles;
	}


	public Double getValor() {
		return valor;
	}


	public void setValor(Double valor) {
		this.valor = valor;
	}


	public Double[] getNotas() {
		return notas;
	}


	public void setNotas(Double[] notas) {
		this.notas = notas;
	}


	public evaluaciones() {
		// TODO Auto-generated constructor stub
	}

}
