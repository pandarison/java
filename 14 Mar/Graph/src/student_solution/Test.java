package student_solution;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import graph_entities.IVertex;
import graph_entities.Result;

public class Test {

	public static void main(String[] args) {
		Graph<String> graph = new Graph<>();
		
		graph.addVertex("A", new Vertex<>());
		graph.addVertex("B", new Vertex<>());
		graph.addVertex("C", new Vertex<>());
		graph.addVertex("D", new Vertex<>());
		graph.addVertex("E", new Vertex<>());
		graph.addVertex("F", new Vertex<>());
		graph.addVertex("G", new Vertex<>());
		graph.addVertex("H", new Vertex<>());
		
		graph.addEdge("A", "B", 1.0f);
		graph.addEdge("A", "C", 3.0f);
		graph.addEdge("A", "D", 1.0f);
		graph.addEdge("C", "E", 3.0f);
		graph.addEdge("D", "F", 1.0f);
		graph.addEdge("F", "E", 4.0f);
		graph.addEdge("F", "H", 1.0f);
		graph.addEdge("H", "G", 1.0f);
		graph.addEdge("G", "E", 1.0f);
		
		Predicate<IVertex<String>> predicate = new Predicate<IVertex<String>>() {

			@Override
			public boolean test(IVertex<String> t) {
				// TODO Auto-generated method stub
				return t.getLabel().getName().equals("E");
			}
		};
		
		BiFunction<IVertex<String>, IVertex<String>, Float> heuristics = new BiFunction<IVertex<String>, IVertex<String>, Float>() {

			@Override
			public Float apply(IVertex<String> t, IVertex<String> u) {
				// TODO Auto-generated method stub
				return 0.0f;
			}
		};
		Result<String> result = graph.aStar("A", "E", heuristics);
		
		System.out.println(result.getPath().get());
		System.out.println(result.getVisitedVertices().get());
		System.out.println(result.getPathCost().get());
		
		
		
	}

}
