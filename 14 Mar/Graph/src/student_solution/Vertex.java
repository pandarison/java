package student_solution;


import java.util.ArrayList;
import java.util.Collection;

import graph_entities.IEdge;
import graph_entities.IVertex;
import graph_entities.Label;

// public class Vertex<T> implements IVertex<T>
// {
// 	// TODO: Implement interface IVertex appropriately


// }

public class Vertex<T> implements IVertex<T>{
	
	private ArrayList<IEdge<T>> edges;
	private Label<T> label;
	private Float heuristic_value;
	
	public Vertex() {
		this.edges = new ArrayList<>();
	}

	@Override
	public int compareTo(IVertex<T> o) {
		return (int) (this.heuristic_value - ((Vertex<T>)o).getHeuristic_value());
	}

	@Override
	public void addEdge(IEdge<T> edge) {
		edges.add(edge);
	}

	@Override
	public Collection<IEdge<T>> getSuccessors() {
		return edges;
	}

	@Override
	public Label<T> getLabel() {
		return label;
	}

	@Override
	public void setLabel(Label<T> label) {
		this.label = label;
	}
	
	@Override
	public String toString(){
		return this.label.getName();
	}

	public Float getHeuristic_value() {
		return heuristic_value;
	}

	public void setHeuristic_value(Float heuristic_value) {
		this.heuristic_value = heuristic_value;
	}
	
}