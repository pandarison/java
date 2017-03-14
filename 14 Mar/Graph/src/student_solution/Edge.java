package student_solution;

import graph_entities.IVertex;
import graph_entities.IEdge;

// public class Edge<T> implements IEdge<T>
// {

//     // TODO: Implement IEdge appropriately 

// };

public class Edge<T> implements IEdge<T>{
	
	private IVertex<T> target;
	private Float cost;

	public Edge(IVertex<T> target, Float cost) {
		super();
		this.target = target;
		this.cost = cost;
	}

	@Override
	public IVertex<T> getTgt() {
		// TODO Auto-generated method stub
		return target;
	}

	@Override
	public Float getCost() {
		// TODO Auto-generated method stub
		return cost;
	}
	
}