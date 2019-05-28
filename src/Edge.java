import java.util.ArrayList;

public class Edge {
	Vertex vertex1;
	Vertex vertex2;
	int weight;
	
	public Edge(String v1,	String v2,	int w) {
		vertex1.name = v1;
		vertex2.name = v2;
		weight = w;
	}
	
	public Edge(Vertex v1, Vertex v2, int w) {
		vertex1 = v1;	// na pewno zadziala?
		vertex2 = v2;
		weight = w;
	}
	
	public Edge() {
		vertex1 = null;
		vertex2 = null;
		weight = 0;
	}
	
	public Edge(int o1, int o2) {
		vertex1.order = o1;
		vertex2.order = o2;
		weight = 0;
	}

	public Edge(String line, ArrayList<Vertex> Vertexes) {
		int temp = line.indexOf(' ');
		vertex1 = new Vertex();
		vertex1.name = line.substring(0, temp);
		for (Vertex v : Vertexes)
			if (v.name.equals(vertex1.name))
				vertex1.order = v.order;		
		int temp2 = line.substring(temp+1).indexOf(' ') + temp + 1;
		vertex2 = new Vertex();
		vertex2.name = line.substring(temp+1, temp2);
		for (Vertex v : Vertexes)
			if (v.name.equals(vertex2.name))
				vertex2.order = v.order;
		line = line.substring(temp2+1);	
		weight = Integer.parseInt(line);
	}
	
}
