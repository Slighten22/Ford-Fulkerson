
public class Vertex {
	String name;
	int cost;
	int order; // position of vertex in adjacency table 

	public Vertex() {
		name = "";
		cost = 0;
		order = -1;
	}
	
	public Vertex(String n) {
		name = n;
		cost = 0;
		order = -1;
	}
	
	public Vertex(String n, int c, int o) {
		name = n;
		cost = c;
		order = o;
	}
	
	public Vertex(String line, int o) {
		int temp = line.indexOf(' ');
		name = line.substring(0, temp);
		cost = Integer.parseInt(line.substring(temp+1, line.length()));
		order = o;
	}
}

