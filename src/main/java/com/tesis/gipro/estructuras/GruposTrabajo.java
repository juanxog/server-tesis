package com.tesis.gipro.estructuras;

public class GruposTrabajo {

	Estudiantes[] students;
	Double valor;
	String id_eval; 
	
	
	
	



	public GruposTrabajo(Estudiantes[] students, Double valor, String id_eval) {
		super();
		this.students = students;
		this.valor = valor;
		this.id_eval = id_eval;
	}



	public String getId_eval() {
		return id_eval;
	}



	public void setId_eval(String id_eval) {
		this.id_eval = id_eval;
	}



	public Estudiantes[] getStudents() {
		return students;
	}



	public void setStudents(Estudiantes[] students) {
		this.students = students;
	}



	public Double getValor() {
		return valor;
	}



	public void setValor(Double valor) {
		this.valor = valor;
	}


	

	public GruposTrabajo(Estudiantes[] students, Double valor) {
		super();
		this.students = students;
		this.valor = valor;
	}



	public GruposTrabajo() {
		// TODO Auto-generated constructor stub
	}

}
