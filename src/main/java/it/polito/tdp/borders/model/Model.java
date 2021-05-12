package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

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
	
	public Set getDegree(Country c) {
		ConnectivityInspector ci = new ConnectivityInspector(grafo);
		Set<Country> set = ci.connectedSetOf(c);
		System.out.println(set.size());
		return set;
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
		ConnectivityInspector ci = new ConnectivityInspector(grafo);
		System.out.println("Grafo creato!");
		System.out.println("#Vertici: "+grafo.vertexSet().size()+"\n");
		System.out.println("#Archi: "+grafo.edgeSet().size()+"\n");
		//System.out.println("Is connected? "+ci.isConnected());
	
		
	}
	
	public SimpleGraph getGrafo() {
		return this.grafo;
	}
	
	
	public String infoGrafo(SimpleGraph grafo) {
		String s ="";
		s+="#Vertici: "+grafo.vertexSet().size()+", #Archi: "+grafo.edgeSet().size()+"\n";
		
		return s;
	}
	
	//ESERCIZIO 2
	
	public List<Country> getRaggiungibili(Country c){
		final List<Country> visitati = new ArrayList<>();
		final List<Country> daVisitare = new ArrayList<Country>();
		  grafo = getGrafo();
		BreadthFirstIterator<Country,DefaultEdge> bfv = new BreadthFirstIterator<Country,DefaultEdge>(grafo,c);
		visitati.add(c);
		while(!daVisitare.isEmpty()) {
			bfv.addTraversalListener(new TraversalListener() {

				@Override
				public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void edgeTraversed(EdgeTraversalEvent e) {
					Country c1 = grafo.getEdgeSource((DefaultEdge) e.getEdge());
					Country c2 = grafo.getEdgeTarget((DefaultEdge) e.getEdge());
					if(!visitati.contains(c2) && visitati.contains(c1) && !daVisitare.contains(c2)) {
						daVisitare.add(c2);
					} else if(visitati.contains(c2) && !visitati.contains(c1) && !daVisitare.contains(c1)) {
						daVisitare.add(c1);
					}
					
					
				}

				@Override
				public void vertexTraversed(VertexTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void vertexFinished(VertexTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		
		}
				while(bfv.hasNext()) {
			Country paese = bfv.next();
			if(!visitati.contains(paese)) {
				visitati.add(paese);
				daVisitare.remove(paese);
			}
		}
		System.out.println(visitati.size());
		
		return visitati;
	
	}

	public List<Country> trovaAdiacenti(final Country c){
		final List<Country> lStati = new ArrayList();
		final SimpleGraph grafo = getGrafo();
		BreadthFirstIterator<Country,DefaultEdge> bfv = new BreadthFirstIterator<Country,DefaultEdge>(this.getGrafo(),c);
		final Map<Country,Country> mapaC = new HashMap<>();
		
		mapaC.put(c, null);//ho messo la radice
		 int count =0;
		bfv.addTraversalListener(new TraversalListener<Country,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				Country c1 = (Country) grafo.getEdgeSource(e.getEdge());
				Country c2 = (Country) grafo.getEdgeTarget(e.getEdge());
				if(mapaC.containsKey(c1) && !mapaC.containsKey(c2) && !lStati.contains(c2)) {
					mapaC.put(c2,c1 );
					lStati.add(c2);
				}else if(!mapaC.containsKey(c1)&& mapaC.containsKey(c2) && !lStati.contains(c1)) {
					mapaC.put(c1,c2 );
					lStati.add(c1);
				}
				
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
			
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}});
		
		while(bfv.hasNext()) {
			Country paese=bfv.next();
			if(!paese.equals(c) && !lStati.contains(paese)) {
				lStati.add(paese);
			}
			
		}
	
	
		
		return lStati;
		
	}
	
	public List<Country> trovaAdiacentiProfundita(final Country c){
		//Visita di profundita
		DepthFirstIterator<Country,DefaultEdge> dfv = new DepthFirstIterator<Country,DefaultEdge>(this.getGrafo(),c);
		final List<Country> nStati = new ArrayList();
		final SimpleGraph grafo = this.getGrafo();
		 final Map<Country,Country> mapa = new HashMap<>();
		mapa.put(c, null);//messo la radice
		
		dfv.addTraversalListener(new TraversalListener() {

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent e) {
				Country c1 = (Country) grafo.getEdgeSource(e.getEdge());
				Country c2 = (Country) grafo.getEdgeTarget(e.getEdge());
				if(mapa.containsKey(c1) && !mapa.containsKey(c2) && !nStati.contains(c2)) {
					mapa.put(c2,c1);
					nStati.add(c2);
				} else if(!mapa.containsKey(c1) && mapa.containsKey(c2) && !nStati.contains(c1)) {
					mapa.put(c1, c2);
					nStati.add(c1);
				}
				
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		if(dfv.hasNext()) {
			Country paese =dfv.next();
			SimpleGraph g = this.getGrafo();
			int i = g.degreeOf(paese);
			System.out.println(paese.toString()+" ,"+i+"\n");
			if(!nStati.contains(paese) && !paese.equals(c) ) {
				nStati.add(paese);
			}
		}
		
		
		return nStati;
	}
	
	
}
