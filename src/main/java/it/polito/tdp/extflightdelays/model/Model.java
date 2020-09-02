package it.polito.tdp.extflightdelays.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	ExtFlightDelaysDAO dao;
	private Graph<Airport,DefaultWeightedEdge> graph;
	List<Arco> archi;
	Map<Integer, Airport> idMap;
	
	Double bestPeso;
	List<Airport> bestCammino;
	
	
	public Model(){
		dao= new ExtFlightDelaysDAO();
	}
	
	public void creaGrafo(int x) {
		this.graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);	
		
		 idMap = new HashMap<>();
		 
		 dao.loadAllAirports(idMap);
		 //Graphs.addAllVertices(graph, idMap.values());
		 
		archi=this.dao.getArco(x,idMap);
		 for(Arco a: this.archi) {
			 if(!this.graph.containsEdge(a.getA1(),a.getA2())) {
				 
				 Graphs.addEdgeWithVertices(graph, a.getA1(), a.getA2(),a.getPeso());
				
			 }
		 }
		
	}
	
	public Collection<Airport> getVertici(){
		return idMap.values();
	}
	
	//NUMERO VERTICI:

	public int nVertici() {
			return this.graph.vertexSet().size();
		}

	//NUMERO ARCHI:

		public int nArchi() {
			return this.graph.edgeSet().size();
		}
		
		public List<Arco> getConnessi(Airport a){
			List<Arco> result= new ArrayList<>();
			List<Airport> vicini=Graphs.neighborListOf(graph, a);
			
			for(Airport v:vicini) {
				Double peso=this.graph.getEdgeWeight(this.graph.getEdge(a, v));
				Arco arco= new Arco(a,v,peso);
				result.add(arco);
			}
			
			Collections.sort(result);
			return result;
		}

		
		public void trovaPercorso(Airport partenza, int m) {
			this.bestCammino=new ArrayList<>();
			this.bestCammino.add(partenza);
			this.bestPeso=0.0;
			
			
			List<Airport> parziale= new ArrayList<>();
			parziale.add(partenza);
			
			ricorsione(parziale,0,m);
			
		}
		private void ricorsione(List<Airport> parziale, double l, int m) {
	
			 if(parziale.size() > this.bestCammino.size() || l==m || (parziale.size()== this.bestCammino.size() && l> this.bestPeso)) {
					this.bestCammino = new ArrayList<>(parziale);
					this.bestPeso = l;
				}
			 
			Airport last= parziale.get(parziale.size()-1);
			List<Airport> vicini=Graphs.neighborListOf(graph, last);
			
			for(Airport v: vicini) {
				Double peso = this.graph.getEdgeWeight(this.graph.getEdge(last, v));
				if(!parziale.contains(v) && (m-l) >= peso ) {
					parziale.add(v);
					l += peso;
					ricorsione(parziale, l, m);
					
					parziale.remove(parziale.size()-1);
					l -= peso;
				}
			}
			
					
		}

	

		public Double getBestPeso() {
			return bestPeso;
		}

		public List<Airport> getBestCammino() {
			return bestCammino;
		}
		
		
		
		
}
