import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphImplementation implements Graph {
    private final int vertices;
    private int[][] adjMatrix;

    public GraphImplementation(int vertices){
        this.vertices = vertices;
        adjMatrix = new int[vertices][vertices];

    }

    public void addEdge(int v1, int v2){ //unweighted, directed graph
        if (v1 >= vertices || v2 >= vertices){ //check if vertex 1 and 2 exist
            throw new IndexOutOfBoundsException();
        }
        adjMatrix[v1][v2] = 1;

    }

    public int[] neighbors(int vertex){ //returns an array containing indices of the neighboring vertices of given vertex
        if (vertex >= vertices){ //check if given vertex exists
            throw new IndexOutOfBoundsException();
        }

        int[] neighVertices = new int[neighborCount(vertex)]; //create an array with the size of counted neighbors
        int j = 0;
        for (int i = 0; i < adjMatrix.length; i++){
            if(adjMatrix[vertex][i] != 0){
                neighVertices[j++] = i;
            }
        }
        return neighVertices;
    }

    public int neighborCount(int vertex){
        int count = 0;
        for(int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[vertex][i] != 0) { //if there exists an edge from the given vertex to the traversed vertex
                count++; //increase count
            }
        }
        return count; //return number of neighbors
    }

    public List<Integer> topologicalSort(){
        int[] incident = new int[vertices];
        for (int i = 0; i < vertices; i++){
            incident[i] = 0;
        }

        //update incident array w/adjacency matrix values - column-wise
        int count = 0;
        for(int i = 0; i < adjMatrix.length; i++){
            for(int j = 0; j < adjMatrix[i].length; j++){
                if(adjMatrix[j][i] != 0){
                    count++;
                }
            }
            incident[i] = count; //place the sum of number of edges that the vertex has in the incident array
            count = 0; //reset tracker
        }

        /*System.out.println("Incident Array before sorting");
        for (int value: incident) {
            System.out.print(value + " ");
        }*/

        List<Integer> schedule = new LinkedList<Integer>(); //create a list (treat like a queue)

        // enqueue all vertices w/no incoming edges
        for (int index = 0; index < incident.length; index ++) {
            schedule.add(zeroCount(incident)); //enqueue all vertices with no incoming edges
        }

        if (schedule.contains(-1)) {
            System.out.println("There exists a cycle in the graph!");
            for (int value: schedule) {
                if (value != -1) {
                    System.out.print(value + " "); //print partial solution before cycle is encountered in some cases
                }
            }
            return schedule;
        }

        System.out.print("Topologically sorted list: ");
        for(int i = 0; i < schedule.size(); i++){
            System.out.print(schedule.get(i) + " ");
        }

        return schedule;

    }

   //finds the next node in a graph and checks if graph has a cycle

    private int zeroCount(int[] incident) { //number of zeros encountered in incident array
        for (int index = 0; index < incident.length; index ++) {
            if (incident[index] == 0) {
                incident[index] = -1; //mark vertex as visited

                for(int col = 0; col < adjMatrix.length; col++){ //update each row after marking vertex as visited
                    if(adjMatrix[index][col] != 0){
                        adjMatrix[index][col] = 0;
                        incident[col]--; //update number of edges that particular vertex has in incident array
                    }
                }
                return index;
            }
        }
        //graph has a cycle
        return -1;
    }

}