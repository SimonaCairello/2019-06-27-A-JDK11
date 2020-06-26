package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<String> bestCamminoMin;
	private Integer pesoMin;

	public Model() {
		this.dao = new EventsDao();
		this.bestCamminoMin = new ArrayList<>();
		this.pesoMin = 0;
	}
	
	public List<String> getOffenseCategory() {
		return this.dao.getOffenseCategory();
	}
	
	public List<Integer> getYears(){
		return this.dao.getYears();
	}
	
	public void generateGraph(String offenseCategory, Integer year) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, this.dao.getOffenseType(offenseCategory, year));
		
		List<Adiacenza> adiacenze = this.dao.getAdiacenze(offenseCategory, year);
		for(Adiacenza a : adiacenze) {
			Graphs.addEdge(this.graph, a.getType1(), a.getType2(), a.getPeso());
		}
	}
	
	public Integer getNumVertici() {
		return this.graph.vertexSet().size();
	}
	
	public Integer getNumArchi() {
		return this.graph.edgeSet().size();
	}
	
	public Integer getPesoMax() {
		Integer max = 0;
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			if(this.graph.getEdgeWeight(e)>max)
				max = (int) this.graph.getEdgeWeight(e);
		}
		return max;
	}
	
	public List<Adiacenza> getArchiPesoMax() {
		Integer max = this.getPesoMax();
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			if(this.graph.getEdgeWeight(e)==max) {
				Adiacenza a = new Adiacenza(this.graph.getEdgeSource(e), this.graph.getEdgeTarget(e), (int)this.graph.getEdgeWeight(e));
				adiacenze.add(a);
			}			
		}
		return adiacenze;
	}
	
	public List<Adiacenza> getArchi() {
		List<Adiacenza> archi = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			Adiacenza a = new Adiacenza(this.graph.getEdgeSource(e), this.graph.getEdgeTarget(e), (int) this.graph.getEdgeWeight(e));
			archi.add(a);
		}
		return archi;
	}
	
	public List<String> getCamminoPesoMin(Adiacenza adiacenza) {
		List<String> parziale = new ArrayList<>();
		parziale.add(adiacenza.getType1());
		this.trovaPercorso(parziale, adiacenza.getType2(), 0);
		return this.bestCamminoMin;
	}
	
	public void trovaPercorso(List<String> parziale, String finale, Integer peso) {
		if(parziale.size()==this.graph.vertexSet().size()) {
			if(parziale.get(parziale.size()-1).equals(finale)) {
				if(this.bestCamminoMin.size()==0) {
					this.pesoMin = peso;
					this.bestCamminoMin.addAll(parziale);
					return;
				}
			
				if(this.pesoMin>peso) {
					this.pesoMin = peso;
					this.bestCamminoMin = new ArrayList<>(parziale);
					return;
				}
				return;
			}
			return;
		}
		
		String ultimoAggiunto = parziale.get(parziale.size()-1);
		Integer p = 0;
		
		for(String s : Graphs.neighborListOf(this.graph, ultimoAggiunto)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				p = (int) this.graph.getEdgeWeight(this.graph.getEdge(ultimoAggiunto, s));
				peso += p;
			
				trovaPercorso(parziale, finale, peso);
			
				// backtracking
				parziale.remove(parziale.size()-1);
				peso -= p;
			}
		}
	}
}
