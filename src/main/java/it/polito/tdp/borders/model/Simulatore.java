package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	//modello
	private Graph<Country, DefaultEdge> grafo;
	
	//tipi di evento
	private PriorityQueue<Evento> queue;
	
	//parametri
	private int MIGRATI = 1000;
	private Country partenza;
	
	//valorri di output
	private int T;
	private Map<Country, Integer> stanziali;
	

	public void init(Country partenza, Graph<Country, DefaultEdge> grafo) {
		this.partenza= partenza;
		this.grafo=grafo;
		
		//impostazione stato iniziale
		this.T=1;
		stanziali = new HashMap<>();
		for (Country c : this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		//creo la coda
		this.queue= new PriorityQueue<Evento>();
		//inserisco il primo evento
		this.queue.add(new Evento(T, partenza, MIGRATI));
	}
	
	public void run() {
		//finche la coda non si svuota estraggo uv evento per volta e lo eseguo
		Evento e;
		while ((e= this.queue.poll())!= null) {
			this.T= e.getT();
			//eseguo l'evento
			int nPersone = e.getN();
			Country stato = e.getStato();
			//cerco i vicini di stato
			List<Country> vicini = Graphs.neighborListOf(this.grafo, stato);
			
			int migranti = (nPersone/2)/vicini.size();
			
			if (migranti>0) {
				//le persone si possono muovere
				for (Country confinante: vicini) {
					queue.add(new Evento(e.getT()+1, confinante, migranti));
				}
			}
			
			int stanziali = nPersone - migranti*vicini.size();
			this.stanziali.put(stato, this.stanziali.get(stato)+stanziali);
		}
		
	}

	public Map<Country, Integer> getStanziali() {
		return stanziali;
	}

	public int getT() {
		return T;
	}
	
	
	
}
