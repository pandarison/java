package student_solution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.zip.CRC32;

import graph_entities.IEdge;
import graph_entities.IGraph;
import graph_entities.IVertex;
import graph_entities.Label;
import graph_entities.Result;


// public class Graph<T> implements IGraph<T>
// {
//     // TODO: Implement interface IGraph appropriately



// }


public class Graph<T> implements IGraph<T>{
	
	private HashMap<String, IVertex<T>> vertices;
	private ArrayList<IEdge<T>> edges;
	private boolean found = false;
	
	public Graph() {
		this.vertices = new HashMap<>();
		this.edges = new ArrayList<>();
	}

	@Override
	public void addVertex(String vertexId, IVertex<T> vertex) {
		Label<T> label = new Label<>();
		label.setName(vertexId);
		vertex.setLabel(label);
		vertices.put(vertexId, vertex);
		
	}

	@Override
	public void addEdge(String vertexSrcId, String vertexTgtId, Float cost) {
		IVertex<T> source = vertices.get(vertexSrcId);
		IVertex<T> target = vertices.get(vertexTgtId);
		IEdge<T> edge = new Edge<T>(target, cost);
		source.addEdge(edge);
		edges.add(edge);
	}

	@Override
	public Collection<IVertex<T>> getVertices() {
		return vertices.values();
	}

	@Override
	public Collection<String> getVertexIds() {
		return vertices.keySet();
	}

	@Override
	public IVertex<T> getVertex(String vertexId) {
		return vertices.get(vertexId);
	}
	
	public String toDotRepresentation(){
		String string = "digraph {\n";
		for(IVertex<T> vertex : vertices.values()){
			string += "\t" + vertex.getLabel().getName() + "\n";
		}
		for(IVertex<T> vertex : vertices.values()){
			for(IEdge<T> edge : vertex.getSuccessors()){
				string += String.format("\t%s -> %s[label=\"%s\"];\n", vertex.getLabel().getName(), edge.getTgt().getLabel().getName(), edge.getCost());
			}
		}
		string += "}";
		return string;
	}
	
	public void fromDotRepresentation(String dotFilePath){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dotFilePath));
			while(true){
				String line = reader.readLine();
				if(line == null)break;
				if(line.lastIndexOf('{') != -1){
					continue;
				}
				if(line.lastIndexOf('}') != -1){
					break;
				}
				line = line.replace(" ", "");
				line = line.replace("\t", "");
				if(line.equals(""))continue;
				if(line.contains("\"")){
					// edge
					int minus = line.indexOf('-');
					int left = line.indexOf('[');
					String va = line.substring(0, minus);
					String vb = line.substring(minus+2, left);
					String cost = line.split("\"")[1];
					this.addEdge(va, vb, Float.parseFloat(cost));
				}else{
					// vertex
					Vertex<T> vertex = new Vertex<T>();
					this.addVertex(line, vertex);
				}
				//System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public Result<T> breadthFirstSearchFrom(String vertexId, Predicate< IVertex<T> > pred)
    {
		Queue<IVertex<T>> queue = new LinkedBlockingQueue<>();
		
		IVertex<T> startingVertex = this.getVertex(vertexId);
		startingVertex.getLabel().setCost(0.0f);
		queue.add(startingVertex);
		
		Result<T> result = new Result<>();
		ArrayList< IVertex<T> > path = new ArrayList<>();
		ArrayList< IVertex<T> > visited = new ArrayList<>();
		result.setPath(path);
		result.setVisitedVertices(visited);
		
		
		while(!queue.isEmpty()){
			System.out.println(queue);
			IVertex<T> current = queue.poll();
			if(!visited.contains(current)){
				visited.add(current);
				
				if(pred.test(current)){ // found path
					IVertex<T> t = current;
					result.setPathCost(current.getLabel().getCost());
					while(true){
						path.add(t);
						if(t.equals(startingVertex)){
							break;
						}
						t = t.getLabel().getParentVertex().get();
					}
					Collections.reverse(path);
					break;
				}
				
				for(IEdge<T> edge: current.getSuccessors()){
					IVertex<T> next = edge.getTgt();
					next.getLabel().setParentVertex(current);
					next.getLabel().setCost(current.getLabel().getCost() + edge.getCost());
					queue.add(next);
				}
			}	
		}
		
		return result;
    }

	public Result<T> depthFirstSearchFrom(String vertexId, Predicate< IVertex<T> > pred)
    {
		Result<T> result = new Result<>();
		ArrayList< IVertex<T> > path = new ArrayList<>();
		ArrayList< IVertex<T> > visited = new ArrayList<>();
		result.setPath(path);
		result.setVisitedVertices(visited);
		result.setPathCost(0.0f);
		this.found = false;
		_depthFirstSearchFrom(vertexId, result, pred);
		return result;
    }
	private void _depthFirstSearchFrom(String vertexId, Result<T> result, Predicate<IVertex<T>> pred){
		if(this.found == true){
			return;
		}
		IVertex<T> current = this.getVertex(vertexId);
		result.getPath().get().add(current);
		current.getLabel().setCost(result.getPathCost().get());
		
		
		if(pred.test(current)){
			this.found = true;
			result.getVisitedVertices().get().add(current);
			return ;
		}
		//System.out.println("Checking: " + result.getPath().get().toString() + " cost: " + result.getPathCost().get());
		if(! result.getVisitedVertices().get().contains(current)){
			result.getVisitedVertices().get().add(current);
			for(IEdge<T> edge : current.getSuccessors()){
				Float oldCost = result.getPathCost().get();
				result.setPathCost(result.getPathCost().get() + edge.getCost());
				_depthFirstSearchFrom(edge.getTgt().getLabel().getName(), result, pred);
				if(this.found == false)
					result.setPathCost(oldCost);
			}
		}
		if(this.found == false)
			result.getPath().get().remove(current);
	}

	public Result<T> dijkstraFrom(String vertexId, Predicate< IVertex<T> > pred)
    {
		Result<T> result = new Result<>();
		ArrayList< IVertex<T> > path = new ArrayList<>();
		ArrayList< IVertex<T> > visited = new ArrayList<>();
		result.setPath(path);
		result.setVisitedVertices(visited);
		result.setPathCost(0.0f);
		this.found = false;
		
		for(IVertex<T> vertex: this.vertices.values()){
			vertex.getLabel().setCost(Float.MAX_VALUE);
		}
		IVertex<T> startingVertex = this.getVertex(vertexId);
		startingVertex.getLabel().setCost(0.0f);
		Queue<IVertex<T>> queue = new LinkedBlockingQueue<>();
		queue.add(startingVertex);
		IVertex<T> endingVertex = null;
		while(!queue.isEmpty()){
			System.out.println(queue);
			IVertex<T> current = queue.poll();
			if(!visited.contains(current))visited.add(current);
			if(pred.test(current)) endingVertex = current;
			for(IEdge<T> edge: current.getSuccessors()){
				IVertex<T> next = edge.getTgt();
				Float cost = current.getLabel().getCost() + edge.getCost();
				if(next.getLabel().getCost() > cost){
					next.getLabel().setCost(cost);
					next.getLabel().setParentVertex(current);
					queue.add(next);
				}
			}
		}
		result.setPathCost(endingVertex.getLabel().getCost());
		IVertex<T> t = endingVertex;
		while(true){
			path.add(t);
			if(t.equals(startingVertex))break;
			t = t.getLabel().getParentVertex().get();
			
		}
		Collections.reverse(path);
		return result;
    }

	public Result<T> aStar(String startVertexId, String endVertexId, BiFunction< IVertex<T>, IVertex<T>, Float > heuristics)
    {
		Queue<IVertex<T>> queue = new LinkedBlockingQueue<>();
		
		IVertex<T> startingVertex = this.getVertex(startVertexId);
		IVertex<T> endingVertex = this.getVertex(endVertexId);
		for(IVertex<T> vertex: this.vertices.values()){
			vertex.getLabel().setCost(Float.MAX_VALUE);
		}
		startingVertex.getLabel().setCost(0.0f);
		queue.add(startingVertex);
		
		
		
		Result<T> result = new Result<>();
		ArrayList< IVertex<T> > path = new ArrayList<>();
		ArrayList< IVertex<T> > visited = new ArrayList<>();
		result.setPath(path);
		result.setVisitedVertices(visited);
		
		
		while(!queue.isEmpty()){
			System.out.println(queue);
			IVertex<T> current = queue.poll();
			if(current.equals(endingVertex)){
				System.out.println("found");
				break;
			}
			if(!visited.contains(current)) visited.add(current);
			
			for(IEdge<T> edge : current.getSuccessors()){
				Vertex<T> next = (Vertex<T>) edge.getTgt();
				if(!visited.contains(next)){
					Float cost = current.getLabel().getCost() + edge.getCost();
					if(!queue.contains(next) || cost < next.getLabel().getCost()){
						next.getLabel().setCost(cost);
						next.getLabel().setParentVertex(current);
						next.setHeuristic_value(next.getLabel().getCost() + heuristics.apply(next, endingVertex));
						if(!queue.contains(next))queue.add(next);
					}
				}
			}
		}
		result.setPathCost(endingVertex.getLabel().getCost());
		IVertex<T> t = endingVertex;
		while(true){
			path.add(t);
			if(t.equals(startingVertex))break;
			t = t.getLabel().getParentVertex().get();
			
		}
		Collections.reverse(path);
		
		
		return result;
    }
	
}

     