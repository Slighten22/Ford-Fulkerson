import java.util.ArrayList;

public class MinCut {
	int minCutValue;
	ArrayList<Vertex> FirstSet;
	ArrayList<Vertex> SecondSet;
	ArrayList<Integer> FirstSetOrders;
	ArrayList<Integer> BlockingEdges;
	
	public MinCut(int value, ArrayList<Vertex> First, ArrayList<Vertex> Second){
		minCutValue = value;
		FirstSet = First;
		SecondSet = Second;
	}
	
	public MinCut(int value, ArrayList<Vertex> First, ArrayList<Vertex> Second, ArrayList<Integer> BlockingEdg){
		minCutValue = value;
		FirstSet = First;
		SecondSet = Second;
		BlockingEdges = BlockingEdg;
	}
	
	public MinCut(ArrayList<Integer> FirstSetOrd, ArrayList<Integer> BlockingEdg) {
		FirstSetOrders = FirstSetOrd;
		BlockingEdges = BlockingEdg;
	}
	
	public int getMinCutValue() {
		return minCutValue;
	}
	
	public String getFirstSet(){
		String set = "[";
		for(Vertex v : FirstSet)
			set += v.name + ", ";
		set += "]";
		return set;
	}
	
	public String getSecondSet(){
		String set = "[";
		for(Vertex v : SecondSet)
			set += v.name + ", ";
		set += "]";
		return set;
	}
	
	public String whatToDamage() {
		String message = "\n";
		String tmp = new String();
		String tmp2 = new String();
		int tmpCost;
		for(int i=0; i<BlockingEdges.size(); i += 2) {
			if(BlockingEdges.get(i)%2 == 1 || (BlockingEdges.get(i) + BlockingEdges.get(i+1))%4 == 3) { // usunac lacze
				for(Vertex v : FirstSet)
					if(v.order == BlockingEdges.get(i))
						tmp = v.name;
				for(Vertex v : SecondSet)
					if(v.order == BlockingEdges.get(i+1))
						tmp2 = v.name;
				message += (i+2)/2 +". Damage connection from " + tmp + " to " + tmp2 + "\n";
			
			}
			else {	// usunac stanowisko
				for(Vertex v : FirstSet) 
					if(v.order == BlockingEdges.get(i))
						tmp = v.name;
				message += (i+2)/2 +". Damage node (computer) " + tmp + "\n";
			}
		}
		
		return message;
	}
}

