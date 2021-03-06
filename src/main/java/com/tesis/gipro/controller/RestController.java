package com.tesis.gipro.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tesis.gipro.estructuras.Estudiantes;
import com.tesis.gipro.estructuras.GruposTrabajo;
import com.tesis.gipro.estructuras.evaluaciones;

@Controller
@RequestMapping("/")
public class RestController {

	@Autowired
	private MongoTemplate mt;
	
	
	
	private List<String[]> generar(Estudiantes [] universo, List<evaluaciones> evaluaciones, String universo_posible[],Map<String, Object> valor){
		
		String posibles [][] = new String[universo.length][universo.length];
		
		
		
		
		for (int j=0;j<universo.length;j++)
		{
			for (int i=0;i<universo.length;i++)
			{
				if(i<j)
					posibles[i][j] = universo[i].get_id() + "," + universo[j].get_id();
				else
					posibles[i][j] = "vacio";
				
			}
			
		}
		
		for (int j=0;j<universo.length;j++)
		{
			for (int i=0;i<universo.length;i++)
			{
				System.out.print(posibles[i][j] + "	");
			}
			
			System.out.print("\n");
		}
		
		
		List<String[]> homo_all = new ArrayList<String[]>();
		
		for (int j=0;j<universo.length;j++)
		{

			for (int i=0;i<universo.length;i++)
			{
				if(i<j)
				{
					String tokens[] = posibles[i][j].split(",");
					homo_all.add(tokens);
				}
				
			}
			
			
		}
		Map<Integer,Boolean> final_recursiva = new HashMap();
		
		final_recursiva.put(1, false);
		
		List<String[]>  posibles_grupos = new ArrayList<String[]>();
		
		for(int j=0;j<universo.length;j++)
		{
			for(int i=0;i<universo.length;i++)
			{
				if(i<j){
					
					String tokens[] = posibles[i][j].split(",");
					Boolean entro = false;
					List<String[]> homo_list = new ArrayList<String[]>();
					
					if(tokens.length > 1){
						
						Estudiantes estu_1 = this.buscar_estudiante(universo, tokens[0]);
						Estudiantes estu_2 = this.buscar_estudiante(universo, tokens[1]);
						
						
						
						Map mapa = (Map) valor.get(estu_1.getType());
						if((Double)mapa.get(estu_2.getType()) > 50){
							entro = true;
						};
						
					}if(tokens.length == 1 ){
						
						Estudiantes estu_1 = this.buscar_estudiante(universo, tokens[0]);
					
						Map mapa = (Map) valor.get(estu_1.getType());
						if((Double)mapa.get(estu_1.getType()) > 50){
							entro = true;
						};
						
					}
					
					if(entro){
						
						List<String[]> retu = this.buscar_posibilidades(tokens, homo_list, homo_all,valor,universo_posible,final_recursiva,universo); 
						if( retu != null)
						{
							return retu;
							
						}
					}
						
				}
			}
			
		}
			
		return posibles_grupos;
	}
	
	
	private List<String[]> buscar_posibilidades(String[] axioma, List<String[]> homo_list, List<String[]> homo_all, Map<String, Object> valor, String universo_posible[], Map<Integer,Boolean> final_recursiva, Estudiantes[] universo )
	{
		List<String[]> posibles = new ArrayList<String[]>();

		homo_list.add(axioma);
		
 		for(int i=0;i<homo_all.size();i++)
		{
			List<String[]> home_list_clone = new ArrayList<String[]>(homo_list);
			
			if(final_recursiva.get(1))
			{
				return homo_list;
			}
			
			String solo = "";
			if(this.aplica(homo_all.get(i), home_list_clone) || !(solo = this.aplica_solo(universo, home_list_clone)).equals("")){
				
				//System.out.println("posibles : " + homo_all.get(i)[0] + " , " +  homo_all.get(i)[1] );
				List<String[]> posibles_inner;
				
				if(solo.equals("")){
					posibles_inner = this.buscar_posibilidades(homo_all.get(i), home_list_clone, homo_all,valor, universo_posible,final_recursiva,universo);
				}
				else{
					String[] solo_array = { solo };
					posibles_inner = this.buscar_posibilidades(solo_array, home_list_clone, homo_all,valor, universo_posible,final_recursiva,universo);
				}
				
				if(final_recursiva.get(1))
				{
					return posibles_inner;
				}
				
				posibles = home_list_clone;
				
				Double valorx = 0.00;
				
				for(int l=0;l<posibles.size();l++){
					
					 
					if(posibles.get(l).length > 1)
					{
						Estudiantes x = this.buscar_estudiante(universo, posibles.get(l)[0]);
						Map valor_inner = (Map) valor.get(x.getType());
						Estudiantes y = this.buscar_estudiante(universo, posibles.get(l)[1]);
						valorx += (Double) valor_inner.get(y.getType());
						
					}else if(posibles.get(l).length == 1)
					{
						
						Estudiantes x = this.buscar_estudiante(universo, posibles.get(l)[0]);
						Map valor_inner = (Map) valor.get(x.getType());
						valorx += (Double) valor_inner.get(x.getType());
						
					}
					
				}
				
				if( posibles.size() >= universo.length/2  ){
					System.out.println("promedio = " + valorx/posibles.size() );
				}
					
				if(valorx/posibles.size() >= 50 && posibles.size() >= universo.length/2  ){ 
					final_recursiva.put(1, true);  
					System.out.println("encontro" + valorx/posibles.size());
					return posibles;
					
				}
				
				
			}
			
		}
		
 		return posibles;
		
	}
	
	private Estudiantes buscar_estudiante(Estudiantes[] estudiantes,String index){
		
		for(int i=0;i<estudiantes.length;i++)
		{
			if(estudiantes[i].get_id().equals(index)){
				return estudiantes[i];
			}
		}
	
		return null;
	}
	
	
	private boolean aplica(String[] axioma_list, List<String[]> axioma_all )
	{
		for(int i=0;i<axioma_list.length;i++){
			
			for(int j=0;j<axioma_all.size();j++){
			
				for(int z=0;z<axioma_all.get(j).length;z++)
				{
					if(axioma_list[i].equals(axioma_all.get(j)[z])){
						return false;
					}
				}
					
			}
			
		}
		
		return true;
	}
	
	private String aplica_solo(Estudiantes[] estudiantes, List<String[]> axioma_all )
	{
		List<String> normalizada = new ArrayList();
		
		List<String> est_normalizada = new ArrayList();
		
		for(int i=0;i<axioma_all.size();i++){
			
			for(int j=0;j<axioma_all.get(i).length;j++){
				
				normalizada.add(axioma_all.get(i)[j]);

			}
		}
		
		for(int i=0;i<estudiantes.length;i++){
			
			est_normalizada.add(estudiantes[i].get_id());
			
		}
		
		
		est_normalizada.removeAll(normalizada);
		
		if(est_normalizada.size() == 1 ){
			
			return est_normalizada.get(0);
			
		}else{
			
			return "";
		}
		
	
	}
	
	@RequestMapping(value="/gipro/{curso}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public List<GruposTrabajo> sayHello(ModelMap model,@RequestBody List<Estudiantes> estudiantes, @PathVariable String curso) {
		
		
		Estudiantes[] universo = new Estudiantes[estudiantes.size()];
		universo = estudiantes.toArray(universo);
		
		System.out.println( "holaaaaa ----> " + curso);
		
		BasicDBObject allQuery = new BasicDBObject();
		allQuery.put("curso", curso);

		DB db = mt.getDb();
		DBCollection collection = db.getCollection("Posibilidades");
		DBCursor cursor = collection.find(allQuery);
		
		List<evaluaciones> evaluaciones = new ArrayList<evaluaciones>();
		
		while (cursor.hasNext()) {
			DBObject obj = cursor.next(); 
			evaluaciones eva = mt.getConverter().read(evaluaciones.class, obj);
			System.out.println(eva.getId());
			System.out.println(eva.getNotas());
			System.out.println(obj);
			evaluaciones.add(eva);
			
		}
		
		
		String universo_posible[] = {"ISTJ",	"ISFJ",	"INFJ",	"INTJ",
		                "ISTP",	"ISFP",	"INFP",	"INTP",
		                "ESTP",	"ESFP",	"ENFP",	"ENTP",
		                "ESTJ",	"ESFJ",	"ENFJ",	"ENTJ"
		};
		
		
		
		Map<String, Object> valor=new HashMap<String, Object>();
		
		
		for(int i=0;i<universo_posible.length;i++){
			
			Map<String, Double> valor_inner=new HashMap<String, Double>();
			
			for( int j=0;j<evaluaciones.size();j++)
			{
				
				if(evaluaciones.get(j).getPosibles()[0].equals(universo_posible[i]))
				{
					valor_inner.put(evaluaciones.get(j).getPosibles()[1], evaluaciones.get(j).getValor());
					
				}else if(evaluaciones.get(j).getPosibles()[1].equals(universo_posible[i]))
				{
					valor_inner.put(evaluaciones.get(j).getPosibles()[0], evaluaciones.get(j).getValor());
					
				}
			
				
			}
			
			valor.put(universo_posible[i],valor_inner);
			
			
		}
		
		List<String[]> grupos = this.generar(universo, evaluaciones, universo_posible, valor);
		
		List<Estudiantes[]> grupos_x = new ArrayList();
		
		List<GruposTrabajo> grupostrabajo = new ArrayList<GruposTrabajo>();
		
		for(int i=0;i<grupos.size();i++)
		{
			GruposTrabajo grupostrabajo_inner = new GruposTrabajo();
			
			if(grupos.get(i).length > 1 )
			{
				Estudiantes estu_1 = this.buscar_estudiante(universo, grupos.get(i)[0]);
				Estudiantes estu_2 = this.buscar_estudiante(universo, grupos.get(i)[1]);
				
				
				Estudiantes[] students = {
						estu_1 , estu_2
				};

				grupostrabajo_inner.setStudents(students);
				Map mapa = (Map) valor.get(estu_1.getType());
				grupostrabajo_inner.setValor((Double)mapa.get(estu_2.getType()));
				
				grupostrabajo_inner.setId_eval(this.id_eval(evaluaciones, estu_1.getType() + "," + estu_2.getType()));
				
			}else{
				
				Estudiantes estu_1 = this.buscar_estudiante(universo, grupos.get(i)[0]);
				
				Estudiantes[] students = {
						estu_1
				};
				
				grupostrabajo_inner.setStudents(students);
				Map mapa = (Map) valor.get(estu_1.getType());
				grupostrabajo_inner.setValor((Double)mapa.get(estu_1.getType()));
				
				grupostrabajo_inner.setId_eval(this.id_eval(evaluaciones, estu_1.getType() + "," + estu_1.getType()));
			}
			
			grupostrabajo.add(grupostrabajo_inner);
		}
		
		return grupostrabajo;
	}


	private String id_eval (List<evaluaciones> evaluaciones, String candidatos){
		
		String[] axioma = candidatos.split(","); 
		
		for(int i=0;i<evaluaciones.size();i++)
		{
			if(evaluaciones.get(i).getPosibles()[0].equals(axioma[0])){
				if(evaluaciones.get(i).getPosibles()[1].equals(axioma[1])){
					return evaluaciones.get(i).getId();
				}
			}
			
			if(evaluaciones.get(i).getPosibles()[1].equals(axioma[0])){
				if(evaluaciones.get(i).getPosibles()[0].equals(axioma[1])){
					return evaluaciones.get(i).getId();
				}
			}
		}
		
		return "";
	}
	
	@RequestMapping(value="/helloagain", method = RequestMethod.GET)
	public String sayHelloAgain(ModelMap model) {
		model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC");
		return "welcome";
	}

}

