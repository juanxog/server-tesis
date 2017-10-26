package com.tesis.gipro.estructuras;

public class Estudiantes {

	String _id;
	String nombre;
	String apellido;
	String cedula;
	Boolean esprofesor;
	String type;
	String password;
	String user;
	
	
	public String get_id() {
		return _id;
	}



	public void set_id(String _id) {
		this._id = _id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApellido() {
		return apellido;
	}



	public void setApellido(String apellido) {
		this.apellido = apellido;
	}



	public String getCedula() {
		return cedula;
	}



	public void setCedula(String cedula) {
		this.cedula = cedula;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}


	
	public Boolean getEsprofesor() {
		return esprofesor;
	}



	public void setEsprofesor(Boolean esprofesor) {
		this.esprofesor = esprofesor;
	}



	public Estudiantes(String _id, String nombre, String apellido, String cedula, Boolean esprofesor, String type,
			String password, String user) {
		super();
		this._id = _id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.cedula = cedula;
		this.esprofesor = esprofesor;
		this.type = type;
		this.password = password;
		this.user = user;
	}



	public Estudiantes() {
		// TODO Auto-generated constructor stub
	}

}
