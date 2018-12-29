import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DSP {

    static class Edge {
        int source;
        int destination;
        int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static class HeapNode{ // contains the vertex key and updated distance for the heap
        int vertex;
        int distance;
    }

    static class Graph {
        int numVertices;
        LinkedList<Edge>[] adjacencylist; // list of edges for each vertex index

        Graph(int vertices) {
            this.numVertices = vertices;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i <vertices ; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        public void addEdge(int source, int destination, int weight) {

            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].add(edge); // adding neighbors to the adjacency list

        }

        public void getMinDistances(int sourceVertex){
            int INFINITY = Integer.MAX_VALUE;
            boolean[] SPT = new boolean[numVertices]; // seen/unseen

          //create heapNode for all the vertices

            HeapNode [] heapNodes = new HeapNode[numVertices];
            for (int i = 0; i < numVertices ; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].distance = INFINITY;
            }

            //set dist for src

            heapNodes[sourceVertex].distance = 0; // A -> A = 0

            //add all the vertices to the MinHeap

            MinHeap minHeap = new MinHeap(numVertices); // initialize our minHeap with all the vertices of the graph.

            for (int i = 0; i < numVertices ; i++) {
                minHeap.insert(heapNodes[i]);
            }
            //while minHeap is not empty take the extracted minimum and do your iterative search
            while(!minHeap.isEmpty()){
                //extract the min
                HeapNode minNode = minHeap.extractMin();

                // min vertex
                int minVertex = minNode.vertex;
                SPT[minVertex] = true; // set as seen

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjacencylist[minVertex];
                for (int i = 0; i <list.size(); i++) {
                    Edge edge = list.get(i);
                    int destination = edge.destination;
                    //only if  destination vertex is not present in SPT
                    if(SPT[destination]==false ) {
                        // check for distance update?
                        int newKey =  heapNodes[minVertex].distance + edge.weight ; // distance + new edge weight
                        int currentKey = heapNodes[destination].distance; // the distance already logged in heapNodes
                        if(currentKey>newKey){
                            decreaseKey(minHeap, newKey, destination);
                            heapNodes[destination].distance = newKey;
                        }
                    }
                }
            }
            //print SPT
            print(heapNodes, sourceVertex);
        }

        public void decreaseKey(MinHeap minHeap, int newKey, int vertex){

            //get the index which distance's needs a decrease;
            int index = minHeap.indexes[vertex];

            //get the node and update its value
            HeapNode node = minHeap.minHeap[index];
            node.distance = newKey;
            minHeap.swim(index); // decrease the key in minHeap
        }

        public void print(HeapNode[] resultSet, int sourceVertex){
            System.out.println("Shortest Distance from Source A to B: ");
            System.out.println(resultSet[1].distance);
        }
    }
    static class MinHeap{
        int capacity;
        int currentSize;
        HeapNode[] minHeap;
        int [] indexes; //will be used to decrease the distance


        public MinHeap(int capacity) {
            this.capacity = capacity;
            minHeap = new HeapNode[capacity + 1];
            indexes = new int[capacity];
            minHeap[0] = new HeapNode();
            minHeap[0].distance = Integer.MIN_VALUE;
            minHeap[0].vertex=-1;
            currentSize = 0;
        }

        public void insert(HeapNode node) {
            currentSize++;
            int index = currentSize;
            minHeap[index] = node;
            indexes[node.vertex] = index;
            swim(index);
        }

        public void swim(int pos) {
            int parentIdx = pos/2;
            int currentIdx = pos;
            while (currentIdx > 0 && minHeap[parentIdx].distance > minHeap[currentIdx].distance) {
                HeapNode currentNode = minHeap[currentIdx];
                HeapNode parentNode = minHeap[parentIdx];

                //swap the positions
                indexes[currentNode.vertex] = parentIdx;
                indexes[parentNode.vertex] = currentIdx;
                swap(currentIdx,parentIdx);
                currentIdx = parentIdx;
                parentIdx = parentIdx/2;
            }
        }

        public HeapNode extractMin() {
            HeapNode min = minHeap[1];
            HeapNode lastNode = minHeap[currentSize];
//            update the indexes[] and move the last node to the top
            indexes[lastNode.vertex] = 1;
            minHeap[1] = lastNode;
            minHeap[currentSize] = null;
            sink(1);
            currentSize--;
            return min;
        }

        public void sink(int k) {
            int smallest = k;
            int leftChildIdx = 2 * k;
            int rightChildIdx = 2 * k+1;
            if (leftChildIdx < heapSize() && minHeap[smallest].distance > minHeap[leftChildIdx].distance) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < heapSize() && minHeap[smallest].distance > minHeap[rightChildIdx].distance) {
                smallest = rightChildIdx;
            }
            if (smallest != k) {

                HeapNode smallestNode = minHeap[smallest];
                HeapNode kNode = minHeap[k];

                //swap the positions
                indexes[smallestNode.vertex] = k;
                indexes[kNode.vertex] = smallest;
                swap(k, smallest);
                sink(smallest);
            }
        }

        public void swap(int a, int b) {
            HeapNode temp = minHeap[a];
            minHeap[a] = minHeap[b];
            minHeap[b] = temp;
        }

        public boolean isEmpty() {
            return currentSize == 0;
        }

        public int heapSize(){
            return currentSize;
        }
    }

    public static void main(String[] args) throws IOException {
//        File graphFile = new File("C:\\Users\\Alex\\Documents\\DePaul_MsCS\\CSC421 - Applied Algos\\DjikstrasSP\\src\\Case3.txt");
         File graphFile = new File(args[0]);

        int source = 0;

        ArrayList<String> alpha = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(graphFile));

        // read in # of vertices from first line
        int numVertices = Integer.parseInt(br.readLine());

        System.out.println("File contains " + numVertices + " vertices.");

        Graph graph = new Graph(numVertices);

        // following the alphabetical ordering for index
        String str;

        for (String alphabet : "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ")) {
            alpha.add(alphabet);
        }

        // reading through the data file, building new edges and adding them to graph

        while ((str = br.readLine()) != null) {
            String[] lineData = str.split(" ");
            String v1 = lineData[0];
            String v2 = lineData[1];
            int weight = Integer.parseInt(lineData[2]);
            int indexV1 = alpha.indexOf(v1);    // first vertex becomes index in the arrayList
            int indexV2 = alpha.indexOf(v2);

            graph.addEdge(indexV1, indexV2, weight);
        }

        graph.getMinDistances(source);

    }
}
