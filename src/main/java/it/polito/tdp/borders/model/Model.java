package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
		
	private BordersDAO dao;
	private List<Country> countries;
	private Map<Integer,Country> idCountryMap = new HashMap<Integer,Country>();
	SimpleGraph<Country,DefaultEdge> grafo;
		
	public Map<Integer, Country> getIdCountryMap() {
		return idCountryMap;
	}

	public void setIdCountryMap(List<Country> lista) {
		for(Country c : lista) {
			if(c!=null) {
				idCountryMap.put(c.getCode(), c);
			}
		}
	}

	public Model() {
		dao = new BordersDAO();
		countries = dao.loadAllCountries();
	}
	
	public List<Country> getAllCountries(){
		return dao.loadAllCountries();
	}

	public List<Border> getCountryPairs(int x, Map<Integer, Country> idMap){
		idMap = this.idCountryMap;
		return dao.getCountryPairs(x, idMap);
	}
	
	public void creaGrafo(int x) {
		
		 grafo = new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		//Add i vertici
		setIdCountryMap(dao.loadAllCountries());
		Graphs.addAllVertices(grafo, dao.loadAllCountries());	
		Map<Integer, Country> map = getIdCountryMap(); //identityMap
		//Devo inserire i bordi ---> i archi
		List<Border> bordi = new ArrayList(getCountryPairs(x,map));	
		for(Border b : bordi) {
				Country c1 = map.get(b.getC1().getCode());
				Country c2 = map.get(b.getC2().getCode());
			if(grafo.containsVertex(c1) && grafo.containsVertex(c2)) {
				if(!grafo.containsEdge(c2, c1) && !grafo.containsEdge(c1,c2)) {//if the edge doesn't exist
					grafo.addEdge(c2, c1);
				} else {
					continue;
				}
			}
		}
		System.out.println("Grafo creato!");
		System.out.println("#Vertici: "+grafo.vertexSet().size()+"\n");
		System.out.println("#Archi: "+grafo.edgeSet().size()+"\n");
		
	}
	
	public SimpleGraph getGrafo() {
		return this.grafo;
	}
	
	
	public String infoGrafo(SimpleGraph grafo) {
		String s ="";
		s+="#Vertici: "+grafo.vertexSet().size()+", #Archi: "+grafo.edgeSet().size()+"\n";
		return s;
	}
	
	
	
}
