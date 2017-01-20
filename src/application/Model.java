package application;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import com.sun.javafx.geom.Edge;

import bean.Parola;
import db.Dao;

public class Model {
	
	private Dao dao = new Dao();
	private UndirectedGraph<Parola, DefaultEdge> grafo;
	
	public List<Parola> getParole(int lung){
		List<Parola> all= dao.getParoleLung(lung);
		return all;
	}
	
	public boolean differiscono(Parola p1, Parola p2){
		String s1 = p1.getNome();
		String s2 = p2.getNome();
		int diff=0;
           if(s1.length()==s2.length()){
			for(int i =0; i<s1.length() && i<s2.length(); i++){
				if(s1.charAt(i)!=s2.charAt(i)){
					diff++;
				}
			}
		}
           if(diff==1){
        	   return true;
           }
		return false;
	}
	
	
	public UndirectedGraph<Parola, DefaultEdge> buildGraph(int lung){
		grafo = new SimpleGraph<Parola, DefaultEdge>(DefaultEdge.class);
		Graphs.addAllVertices(grafo, getParole(lung));
		for(Parola p1 : grafo.vertexSet()){
			for(Parola p2 : grafo.vertexSet()){
				if(!p1.equals(p2)){
					if(differiscono(p1,p2)){
						if(!grafo.containsEdge(p1,p2)){
						grafo.addEdge(p1, p2);
						}
					}
				}
			}
		}
		System.out.println(grafo.toString());
		return grafo;
	}
	
	
	public List<Parola> getTrovaVicini(String parola, UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){          //funziona
		List<Parola> vicini= new LinkedList<>();
		Parola p = trovaParola(parola, lunghezza);
		if(p!=null){
			vicini = Graphs.neighborListOf(grafo, p);
			System.out.println(vicini.toString());
			return vicini;
		  }
		return null;
	}
	
	public  Parola trovaParola(String parola,int  lunghezza) {        //ok
		List<Parola> all = dao.getParoleLung(lunghezza);              //tutte le parole di quella lunghezza //vertici grafo
		Parola questa = null;                                       //creo oggetto 
		for(Parola p : all){
			if(p.getNome().equals(parola)){
				questa = p; 
			}
		}
		System.out.println(questa);
		return questa;
	}
	
	public List<Parola> getTrovaConnessi(String parola, UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){    //ok
		Parola p = trovaParola(parola, lunghezza);
		BreadthFirstIterator<Parola, DefaultEdge> visita = new BreadthFirstIterator <Parola, DefaultEdge>(grafo, p); 
		List<Parola> tutti= new LinkedList<>();
		while(visita.hasNext()){
			tutti.add(visita.next());
		}
		System.out.println(tutti.toString());
		return tutti;
		
	}
	
	
	public List<Parola> getConnessi2(String s1, UndirectedGraph<Parola, DefaultEdge> grafo){   //ok
		Parola p1 = trovaParola(s1, 4);
		if(p1!=null){
			DepthFirstIterator<Parola, DefaultEdge> conn = new DepthFirstIterator<Parola, DefaultEdge>(grafo, p1);
			List<Parola> connessi = new LinkedList<>();
			while(conn.hasNext()){
				connessi.add(conn.next());
			}
			System.out.println(connessi.toString());
			return connessi;
		}
		System.out.println("null");
		return null;
	}
	
	
	public int getNumeroComponentiConnesse(UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){
		List<Parola> paroleDuplicate = dao.getParoleLung(lunghezza);
		int cluster=0;
		while(!paroleDuplicate.isEmpty()){
			Parola start = paroleDuplicate.get(0);   //prendo prima parola
			GraphIterator<Parola, DefaultEdge> visita = new DepthFirstIterator<Parola, DefaultEdge>(grafo, start);
			while(visita.hasNext()){
				Parola end = visita.next();
				paroleDuplicate.remove(paroleDuplicate.indexOf(end));
			}
			cluster ++;
		}
		System.out.println(cluster);
		return cluster;
	}

	
	public int getNumeroComponentiConnesse2(UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){  //nn va
		Integer cluster =0;
		List<Parola> temp = new LinkedList<>();
		temp.addAll(grafo.vertexSet());
		while(!temp.isEmpty()){
			cluster++;
			BreadthFirstIterator<Parola, DefaultEdge> visita = new BreadthFirstIterator<Parola, DefaultEdge>(grafo, temp.get(0));
			while(visita.hasNext()){
				temp.remove(visita.next());
			}
		}
		return cluster;
		
	}
	
	//parola col maggiorn numero di connessa : cara= 32
	public Parola getParolaMaxConnex(UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){  //ok
		int max=0;
		Parola trovata = null;
		for(Parola p : grafo.vertexSet()){
			int connessioni = getGrado(p);
			if(connessioni>max){
				max=connessioni;
				trovata = p;
				
			}
		}
		System.out.println(trovata);
		return trovata;
	}
	
	//det vertice a partire dal quale posso raggiungere il maggiorn num di altri vertici:
	public int getConnMax(UndirectedGraph<Parola, DefaultEdge> grafo, int lunghezza){   //3235
		Parola best = null;
		int max=0;
		for(Parola p : grafo.vertexSet()){
			BreadthFirstIterator<Parola , DefaultEdge> visita = new BreadthFirstIterator<Parola , DefaultEdge>(grafo , p);
			int contatore =0;
			while(visita.hasNext()){
				Parola ptemp= visita.next();
				contatore++;
			}
			if(contatore> max){
				best = p;
				max=contatore;
			}
		}
		System.out.println(max);
		return max;
	}
	
	
	private int getGrado(Parola p) {    //ok
		int conn = grafo.degreeOf(p);
	  //  System.out.println(conn);
		return conn;
	}

	
	public List<Parola> getCammino(String s1, String s2){    //ok
		Parola p1 = trovaParola(s1, 4);
		Parola p2 = trovaParola(s2, 4);
		if(p1!=null && p2!=null){
			DijkstraShortestPath<Parola, DefaultEdge> di = new DijkstraShortestPath<Parola, DefaultEdge>(grafo, p1,p2);
			GraphPath<Parola, DefaultEdge> path= di.getPath();
			    if(path== null){
				   return null;
			     }
			    System.out.println(Graphs.getPathVertexList(path));
			return Graphs.getPathVertexList(path);		
		}
		System.out.print("null");
		return null;
	}
	
	
	
	public List<DefaultEdge> mostraPercorsoArchi(String s1, String s2){   //ok
		if(s1!=null && s2!=null){
			Parola p1 = trovaParola(s1, 4);
			Parola p2 = trovaParola(s2, 4);
			if(p1!=null && p2!=null){
				DijkstraShortestPath<Parola, DefaultEdge> di = new DijkstraShortestPath<Parola, DefaultEdge>(grafo, p1, p2);
				List<DefaultEdge> archi = di.getPathEdgeList();
				for(DefaultEdge e : archi){
					System.out.println(e + "\n");
				}
				return archi;
			}
		}
		System.out.println("null");
		return null;
	}
	
	
	//det parola non raggiungibili da nex altra parola:
	public List<Parola> getParoleNonRaggiungibili(){   //ma come è possibili? non sono tutte collegate?
		List<Parola> non = new LinkedList<Parola>();
		for(Parola p : grafo.vertexSet()){
			if(getGrado(p)==0){
				non.add(p);
			}
		}
		System.out.println(non);
		return non;
	}
	
	
	
	public static void main(String [] args){
		Model m = new Model();
		//Parola parola = m.trovaParola("vv", 2);
		UndirectedGraph<Parola, DefaultEdge> grafo = m.buildGraph(4);
		//m.getTrovaVicini("casa", grafo, 4 );
		//m.getTrovaConnessi("casa", grafo, 4);
		//m.getNumeroComponentiConnesse(grafo, 4);
		//m.getParolaMaxConnex(grafo, 4);
		//m.trovaParola("casa", 4);
		//m.getGrado(m.trovaParola("care", 4));
		//m.getCammino("casa","sola");
		//m.getCammino1("casa", "sola", grafo);
		//m.getConnessi2("casa", grafo);
		//m.calcPercorsoarchi("casa", "sola");
		//m.getConnMax(grafo, 4);
		m.getParoleNonRaggiungibili();
		//m.getTrovaVicini(parola, grafo, lunghezza)
		//m.getConnessi2("casa", grafo);
	}
	
	

}
