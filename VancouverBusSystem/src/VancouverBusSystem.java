import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
public class VancouverBusSystem implements Comparable<VancouverBusSystem>
{
	
	NodeWeighted destination;
	NodeWeighted src;
	double weight;

	VancouverBusSystem(NodeWeighted s, NodeWeighted d, double w) 
	{
		src = s;
		weight = w;
		destination = d;
	}
	public String toString()
	{
		return String.format("(%s -> %s, %f)", src.name, destination.name, weight);
	}

	public int compareTo(VancouverBusSystem otherEdge)
	{
		if (this.weight > otherEdge.weight)
		{
			return 1;
		}
		else return -1;
	}
	public static class NodeWeighted 
	{
		String name;
		int n;
		private boolean isVisited;
		LinkedList<VancouverBusSystem> edgeList;

		NodeWeighted(int n, String name) 
		{
			this.n = n;
			this.name = name;
			isVisited = false;
			edgeList = new LinkedList<>();
		}

		boolean isVisited() 
		{
			return isVisited;
		}

		void visit()
		{
			isVisited = true;
		}

		void unvisited()
		{
			isVisited = false;
		}
	}
	public static class GraphWeighted 
	{
		private Set<NodeWeighted> nodeSet;
		private boolean isDirected;

		GraphWeighted(boolean directed)
		{
			this.isDirected = directed;
			nodeSet = new HashSet<>();
		}
		
		public void addNode(NodeWeighted... n) 
		{
			nodeSet.addAll(Arrays.asList(n));
		}
		public void addEdge(NodeWeighted src, NodeWeighted destination, double weight) 
		{

			nodeSet.add(src);
			nodeSet.add(destination);

			addEdgeHelper(src, destination, weight);

			if (!isDirected && src != destination)
			{     
				addEdgeHelper(destination, src, weight);
			}
		}


		private void addEdgeHelper(NodeWeighted j, NodeWeighted k, double weight)
		{
			for (VancouverBusSystem edge : j.edgeList) 
			{
				if (edge.src == j && edge.destination == k) 
				{
					edge.weight = weight;
					return;
				}
			}

			j.edgeList.add(new VancouverBusSystem(j, k, weight));

		}
		public void printEdges() {
			for (NodeWeighted node : nodeSet) {
				LinkedList<VancouverBusSystem> edges = node.edgeList;

				if (edges.isEmpty()) {
					System.out.println("Node " + node.name + " has no edges.");
					continue;
				}
				System.out.print("Node " + node.name + " has edges to: ");

				for (VancouverBusSystem edge : edges) {
					System.out.print(edge.destination.name + "(" + edge.weight + ") ");
				}
				System.out.println();
			}

		}
	

	public void DijkstraShortestPath(NodeWeighted start, NodeWeighted end) 
	{

		HashMap<NodeWeighted, NodeWeighted> changedAt = new HashMap<>();
		changedAt.put(start, null);

		HashMap<NodeWeighted, Double> shortestPathMap = new HashMap<>();

		for (NodeWeighted node : nodeSet) 
		{
			if (node == start)
				shortestPathMap.put(start, 0.0);
			else shortestPathMap.put(node, Double.POSITIVE_INFINITY);
		}
		for (VancouverBusSystem edge : start.edgeList) {
			shortestPathMap.put(edge.destination, edge.weight);
			changedAt.put(edge.destination, start);
		}
		start.visit();	
		while (true) {
			NodeWeighted currentNode = closestReachableUnvisited(shortestPathMap);
			
			if (currentNode == null)
			{
				System.out.println("There isn't a path between " + start.name + " and " + end.name);
				return;
			}

			
			if (currentNode == end)
			{
				System.out.println("The path with the lowest cost from Stop "
						+ start.name + " to Stop " + end.name + " is:");

				NodeWeighted child = end;
				String path = end.name;
				while (true) {
					NodeWeighted parent = changedAt.get(child);
					if (parent == null)
					{
						break;
					}
					path = parent.name + " " + path;
					child = parent;
				}
				System.out.println(path);
				System.out.println("The path costs: " + shortestPathMap.get(end) + " $");
				return;
			}
			currentNode.visit();

			for (VancouverBusSystem edge : currentNode.edgeList)
			{
				if (edge.destination.isVisited())
					continue;

				if (shortestPathMap.get(currentNode)
						+ edge.weight
						< shortestPathMap.get(edge.destination))
				{
					shortestPathMap.put(edge.destination,
							shortestPathMap.get(currentNode) + edge.weight);
					changedAt.put(edge.destination, currentNode);
				}
			}
		}
	}
	private NodeWeighted closestReachableUnvisited(HashMap<NodeWeighted, Double> shrstPMap) 
	{

		double shrstDist = Double.POSITIVE_INFINITY;
		NodeWeighted closeReachableNode = null;
		for (NodeWeighted node : nodeSet) 
		{
			if (node.isVisited())
				continue;

			double currDist = shrstPMap.get(node);
			if (currDist == Double.POSITIVE_INFINITY)
				continue;

			if (currDist < shrstDist) 
			{
				shrstDist = currDist;
				closeReachableNode = node;
			}
		}
		return closeReachableNode;
	}
}

public static class Graph 
{
	static String fileName = "";
	static int nodes = 0;
	static int edges = 0;
	static double cost = 0;
	static ArrayList<String> stopID = new ArrayList<String>();
	static ArrayList<String> stopID1 = new ArrayList<String>();
	static ArrayList<String> tripID = new ArrayList<String>();
	static ArrayList<Integer> StopIDint = new ArrayList<>();
	static ArrayList<String> tripData = new ArrayList<>();
	static ArrayList<String> arrivalTime = new ArrayList<>();
	static ArrayList<String> arrayOfTrips = new ArrayList<>();
	static ArrayList<String> transferData = new ArrayList<>();
	static ArrayList<String> minTransferTime = new ArrayList<>();
	static ArrayList<Integer> minTransferTimeint = new ArrayList<>();
	static ArrayList<String> transferType = new ArrayList<>();
	static ArrayList<String> fromStop = new ArrayList<>();
	static ArrayList<String> toStop = new ArrayList<>();
	static ArrayList<Double> costList = new ArrayList<>();
	
	/* public class TernarySearchTree {

	    private class Node {
	        public char data; 
	        public Node leftNode;
	        public Node middNode;
	        public Node rightNode;
	        boolean isLast = false;

	        public Node(char data) {
	            this.data = data;
	        }
	    }

	    Node head;

	    public TernarySearchTree() {
	        head = null;
	    }


	    public void put(String word) {
	        head = put(head, word, 0);
	    }

	    private Node put(Node node, String word, int index) {
	        char letter = word.charAt(index);
	        if (node == null)
	            node = new Node(letter);
	        if (letter < node.data)
	            node.leftNode = put(node.leftNode, word, index);
	        else if (letter > node.data)
	            node.rightNode = put(node.rightNode, word, index);
	        else if (index < word.length() - 1)
	            node.middNode = put(node.middNode, word, index + 1);
	        else
	            node.isLast = true;
	        return node;
	    }

	    public String get(String word) {
	        return get(head, word, 0, "");
	    }

	    private String get(Node node, String word, int index, String result) {
	        if (node == null)
	            return null;
	        char letter = word.charAt(index);
	        if (letter < node.data) {
	            return get(node.leftNode, word, index, result);
	        } else if (letter > node.data) {
	            return get(node.rightNode, word, index, result);
	        } else if (index < word.length() - 1) {
	            return get(node.middNode, word, index + 1, result + letter);
	        } else
	            return result + letter;
	    }

	    public String[] getMultiple(String word) {
	        return getMultiple(head, word, 0, "");
	    }

	    private String[] getMultiple(Node node, String word, int index, String substring) {
	        if (node == null)
	            return null;
	        char letter = word.charAt(index);
	        if (letter < node.data) {
	            return getMultiple(node.leftNode, word, index, substring);
	        } else if (letter > node.data) {
	            return getMultiple(node.rightNode, word, index, substring);
	        } else if (index < word.length() - 1) {
	            return getMultiple(node.middNode, word, index + 1, substring + letter);
	        } else {
	            ArrayList<String> res = new ArrayList<>();
	            collect(node, substring, res);
	            return res.toArray(new String[res.size()]);
	        }
	    }

	    private void collect(Node node, String substring, ArrayList<String> soFar) {
	        if (node != null) {
	            collect(node.leftNode, substring, soFar);
	            if(node.isLast)
	                soFar.add(substring + node.data);
	            collect(node.middNode, substring + node.data, soFar);
	            collect(node.rightNode, substring, soFar);
	        }
	    }
	}

*/

	public static void parseFile() throws IOException
	{
		//read through file to make ArrayList of stops
		String file = "stops.txt"; 
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		try {
			@SuppressWarnings("unused")
			String headerLine = br.readLine();
			while((line=br.readLine())!=null)
			{
				String[] columns = line.split(",");
				stopID.add(columns[0]);
			}

		} catch (Exception e) 
		{
			br.close();
		}
		{
			String file2 = "stop_times.txt"; 
			@SuppressWarnings("resource")
			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			String line2;
			try {
				@SuppressWarnings("unused")
				String headerLine = br2.readLine();
				while( (line2 = br2.readLine()) != null)
				{
					tripData.add(line2);
				}

			} catch (Exception e)
{
				br.close();

			}

			String[] g = stopID.toArray(new String[0]);
			for (int i = 0; i < stopID.size(); i++) 
			{
				int j = Integer.parseInt(g[i]);
				StopIDint.add(j);
			}

			file = "stop_times.txt"; 
			BufferedReader br1 = new BufferedReader(new FileReader(file));
			String line1;
			try {
				@SuppressWarnings("unused")
				String headerLine = br1.readLine();
				while((line1=br1.readLine())!=null)
				{
					String[] columns = line1.split(",");
					tripID.add(columns[0]);
					stopID1.add(columns[3]);
					arrivalTime.add(columns[2]);
				}

			} catch (Exception e) {
				br1.close();
			}
		}

	}
	public static void readTransfers() throws IOException
	{
		String file = "transfers.txt"; 
		BufferedReader br3 = new BufferedReader(new FileReader(file));
		String line3;
		try {
			@SuppressWarnings("unused")
			String headerLine = br3.readLine();
			while( (line3 = br3.readLine()) != null)
			{	String[] columns = line3.split(",");
			fromStop.add(columns[0]);
			toStop.add(columns[1]);
			transferType.add(columns[2]);
			transferData.add(line3);
			}

		} catch (Exception e)
		{
			br3.close();
		}

		for (int i = 0; i < transferData.size(); i++) 
		{
			String str = transferData.get(i);
			String[] columns = str.split(",", -1); 
			minTransferTime.add(columns[3]);
		}
		System.out.println(minTransferTime.get(1));
		for (int i = 0; i < transferData.size(); i++) 
		{
			if (transferType.get(i).equals("2"))
			{
				double temp =	Integer.parseInt( minTransferTime.get(i));
				costList.add(temp/100);

			} else costList.add((double)0);

		}


	}
	public static void printTime(String s0) throws IOException
	{
		parseFile();	
		readTransfers();
		if (arrivalTime.contains(s0))
		{
			System.out.println("\n Here are all of the trips that have at the inputted arrival time:");
			System.out.println("\nTripID: Arrival Time: DepartureTime: StopID: stop_sequence: stop_headsign: pickup_type: drop_off_type: shape_dist_traveled: ");
			for (int i = 0; i < arrivalTime.size(); i++)
			{

				String s1 = arrivalTime.get(i);
				if (s1.equals(s0)) {
					arrayOfTrips.add((tripData.get(i)));
					System.out.println(tripData.get(i));
				}

			} 
		}else System.out.println("Error, input is not in the accepted format or there are no trips at given time has found.");
	}
	public static void setup(String busStop1, String busStop2) throws IOException {

		GraphWeighted graphWeighted = new GraphWeighted(true);
		parseFile();	
		readTransfers();
		if (stopID.contains(busStop1) && stopID.contains(busStop2))
		{
			List<NodeWeighted> nodeList = new ArrayList<>(stopID.size());
			HashMap<String, Integer> hashMap = new HashMap<>();
			for(int i=0; i<stopID.size(); i++) {
				NodeWeighted nodeName = new NodeWeighted(i,stopID.get(i));
				hashMap.put(stopID.get(i),i);
				nodeList.add(nodeName);
			}	
			for (int i = 1; i < stopID1.size(); i++) {
				if (tripID.get(i).equals(tripID.get(i-1))){
					graphWeighted.addEdge(nodeList.get(hashMap.get(stopID1.get(i-1))),
							nodeList.get(hashMap.get(stopID1.get(i))), 1);
				}
			}
			for (int i = 0; i < transferData.size(); i++) {
				graphWeighted.addEdge(nodeList.get(hashMap.get(fromStop.get(i))),
						nodeList.get(hashMap.get(toStop.get(i))), costList.get(i));	
			}
			graphWeighted.DijkstraShortestPath(nodeList.get(hashMap.get(busStop1)),nodeList.get(hashMap.get(busStop2)));
		}
		else {
			System.out.println("Error, entered bus stop doesn't exist.");
		}
	}
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		boolean isValid = true;
		String time = "";
		System.out.println("Welcome to the Vancouver Bus System.");
		do {

			System.out.println("\n Please input \"D\" for dijkstra algorithm or \"S\" for search by time or \"N\" to search for a bus stop by full name or by the first few characters  (or enter Quit)");
			String checker = input.next();
			if (checker.equalsIgnoreCase("Quit"))
			{
				System.out.println("Program has terminated.");
				isValid = false;
			}
			if (checker.equals("D")) {
				System.out.println("\nPlease input two bus stops to find the shortest path.");
				String busStop1 = input.next();
				if (busStop1.equalsIgnoreCase("Quit"))
				{
					System.out.println("Program has terminated.");
					isValid = false;
				}
				String busStop2 = input.next();
				if (busStop2.equalsIgnoreCase("Quit"))
				{
					System.out.println("Program has terminated.");
					isValid = false;
				}
				System.out.println("Wait...");
				setup(busStop1,busStop2);
			}
			/*
			else if (checker.equals("N")) {
				System.out.println("\nPlease enter the bus stop name or the first few characters: ");
				String stopName = input.next();	
				tree = new ArrayList(tripData);
				Iterable<String> listOfStops = tree.keysWithPrefix(stopName.toUpperCase());
				findStopsUsingTST(tripData, tree, listOfStops);		
				//Checks whether valid stop
				if(tree.checkIfValidStop(listOfStops))
				{
					isValid = true;
				}
				else
				{
					System.out.println("No stops found.\n");
				}
			}
			*/
			else if (checker.equals("S")){
				System.out.println("\n Please enter an input as a time to search for in the hh:mm:ss format.");
				String tmp = input.next();
				if (tmp.equalsIgnoreCase("Quit"))
				{
					System.out.println("Program has terminated.");
					isValid = false;
				}
				if (tmp.length() == 7) {
				 time = " " + tmp;
				}
				else time = tmp;
				printTime(time);
			}
			else {
				System.out.println("Error, incorrect input is entered.");
			}
		}
		while(isValid);
	}
}

public boolean isVisited() {
	
	return false;
}

}







