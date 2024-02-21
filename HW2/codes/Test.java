import java.io.*;
import java.util.*;

public class Test {
    static int n, m;
    static ArrayList<Integer>[] adj, revAdj;
    public static void main(String[] args) throws IOException {
        //version_2
        ArrayList<Integer>[] adjList = null;
        ArrayList<Integer>[] adjList_rev = null;
        //version_1
        int[][] adjMatrix = null;
        //version_3
        Graph_adj_array G = null;

        ArrayList<ArrayList<Integer>> sccs = null;

        //To get result
//        int version = 2;
        int file_num = 1;

        BufferedReader br = new BufferedReader(new FileReader("./input/" + file_num + ".in"));
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(br.readLine());
//        if (version == 1) {
//                adjMatrix = new int[n + 1][n + 1];
//                for (int i = 0; i < m; i++) {
//                    line = br.readLine().split(" ");
//                    int u = Integer.parseInt(line[0]);
//                    int v = Integer.parseInt(line[1]);
//                    adjMatrix[u][v] = 1;
//                }
//                br.close();
//                sccs = TarjanAlgorithm_test.findSCCsUsingAdjMatrix(adjMatrix);
//
//            } else if (version == 2) {
//                adjList = new ArrayList[n + 1];
//                adjList_rev = new ArrayList[n+1];
//
//            for (int i = 1; i <= n; i++) {
//                adjList[i] = new ArrayList<>();
//                adjList_rev[i] = new ArrayList<>();
//            }
//            for (int i = 0; i < m; i++) {
//                line = br.readLine().split(" ");
//                int u = Integer.parseInt(line[0]);
//                int v = Integer.parseInt(line[1]);
//                adjList[u].add(v);
//                adjList_rev[v].add(u);
//            }
//            br.close();
//            sccs = TarjanAlgorithm_test.findSCCsUsingAdjList(adjList, adjList_rev, n);
//
//        } else if (version == 3) {
//            G = new Graph_adj_array(n);
//            for (int i = 0; i < m; i++) {
//                line = br.readLine().split(" ");
//                int s = Integer.parseInt(line[0]);
//                int t = Integer.parseInt(line[1]);
//                G.insert(s, t);
//            }
//            br.close();
//            StronglyConnectedComponents S = new StronglyConnectedComponents();
//            sccs = S.findSCC(G);
//
//        }
//        br.close();
//
//        if (version == 1) {
//            sccs = TarjanAlgorithm_test.findSCCsUsingAdjMatrix(adjMatrix);
//        } else if (version == 2) {
//            sccs = TarjanAlgorithm_test.findSCCsUsingAdjList(adjList, adjList_rev, n);
//        } else if (version == 3) {
//            StronglyConnectedComponents S = new StronglyConnectedComponents();
//            sccs = S.findSCC(G);
//        }

//        int numSCCs = sccs.size();
//        ArrayList<Integer> xorResults = new ArrayList<>();
//        int i = 0;
//        for (ArrayList<Integer> scc : sccs) {
//            int xor = 0;
//            for (int vertex : scc) {
//                xor ^= vertex;
//            }
//            xorResults.add(xor);
//        }
//        Collections.sort(xorResults);
//
//        System.out.println(numSCCs);
//        System.out.println(xorResults);

        // To get time

        //version 1
        long start_time = System.nanoTime();
        adjMatrix = new int[n + 1][n + 1];
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            adjMatrix[u][v] = 1;
        }
        //br.close();
        sccs = TarjanAlgorithm_test.findSCCsUsingAdjMatrix(adjMatrix);
        long v1_time = System.nanoTime() - start_time;

        //version 2
        BufferedReader br_2 = new BufferedReader(new FileReader("./input/test10.in"));
        String[] line_2 = br_2.readLine().split(" ");
        n = Integer.parseInt(line_2[0]);
        m = Integer.parseInt(br_2.readLine());

        start_time = System.nanoTime();
        adjList = new ArrayList[n + 1];
        adjList_rev = new ArrayList[n+1];

        for (int i = 1; i <= n; i++) {
            adjList[i] = new ArrayList<>();
            adjList_rev[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            line_2 = br_2.readLine().split(" ");
            int u = Integer.parseInt(line_2[0]);
            int v = Integer.parseInt(line_2[1]);
            adjList[u].add(v);
            adjList_rev[v].add(u);
        }
        //br.close();
        sccs = TarjanAlgorithm_test.findSCCsUsingAdjList(adjList, adjList_rev, n);
        long v2_time = System.nanoTime() - start_time;

        //version 3
        BufferedReader br_3 = new BufferedReader(new FileReader("./input/test10.in"));
        String[] line_3 = br_3.readLine().split(" ");
        n = Integer.parseInt(line_3[0]);
        m = Integer.parseInt(br_3.readLine());

        start_time = System.nanoTime();
        G = new Graph_adj_array(n);
        for (int i = 0; i < m; i++) {
            line_3 = br_3.readLine().split(" ");
            int s = Integer.parseInt(line_3[0]);
            int t = Integer.parseInt(line_3[1]);
            G.insert(s, t);
        }
        br.close();
        StronglyConnectedComponents S = new StronglyConnectedComponents();
        sccs = S.findSCC(G);
        long v3_time = System.nanoTime() - start_time;

        System.out.println("Adj Matrix : " + v1_time + " ns");
        System.out.println("Adj List : " + v2_time + " ns");
        System.out.println("Adj Array : " + v3_time + " ns");

    }
}

class InputGenerator {
    public static void main(String[] args) {
        int numV = 500;
        double density = 0.75;
        String outputPath = "./input/1.in";
        int numE = 0;

        Random random = new Random(125);

        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(numV + "\n");

            StringBuilder line = new StringBuilder();
            for (int i = 1; i <= numV; i++) {
                //List<Integer> adj = new ArrayList<>();
                for (int j = 1; j <= numV; j++) {
                    if (i != j && random.nextDouble() < density) {
                        line.append(i).append(" ").append(j).append("\n");
                        numE ++;
                    }
                }
            }
            fw.write(numE + "\n");
            fw.write(line.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class TarjanAlgorithm_test {

    //Adj List
    static int n, sccIdx;
    static boolean[] visited;
    static ArrayList<Integer>[] adj, revAdj;
    static int[] scc;
    static ArrayList<ArrayList<Integer>> sccs;

    //Adj Matrix
    private static int index;
    private static int[] lowlink;
    private static boolean[] onStack;
    private static Stack<Integer> matrix_stack;
    private static int[] ids;

    //Adj Array
    static int[] adjVertices;
    static int[] endingIndex;
    //private boolean[] visited;
    //private List<List<Integer>> sccs;
    private static Stack<Integer> stack;
    private static int numVertices;

    public static ArrayList<ArrayList<Integer>> findSCCsUsingAdjMatrix(int[][] adjMatrix) {
        int n = adjMatrix.length;
        index = 0;
        lowlink = new int[n];
        onStack = new boolean[n];
        matrix_stack = new Stack<Integer>();
        ids = new int[n];
        Arrays.fill(ids, -1);

        for (int u = 1; u < n; u++) {
            if (ids[u] == -1) {
                strongConnectUsingAdjMatrix(adjMatrix, u);
            }
        }

        ArrayList<ArrayList<Integer>> sccs = new ArrayList<ArrayList<Integer>>();
        int[] sccSizes = new int[n];
        for (int i = 1; i < n; i++) {
            sccSizes[ids[i]]++;
        }
        for (int i = 1; i < n; i++) {
            sccs.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            sccs.get(ids[i]).add(i);
        }
        sccs.removeIf(ArrayList::isEmpty);
        return sccs;
    }

    private static void strongConnectUsingAdjMatrix(int[][] adjMatrix, int u) {
        ids[u] = index;
        lowlink[u] = index;
        index++;
        matrix_stack.push(u);
        onStack[u] = true;

        for (int v = 1; v < adjMatrix[u].length; v++) {
            if (adjMatrix[u][v] != 0) {
                if (ids[v] == -1) {
                    strongConnectUsingAdjMatrix(adjMatrix, v);
                    lowlink[u] = Math.min(lowlink[u], lowlink[v]);
                } else if (onStack[v]) {
                    lowlink[u] = Math.min(lowlink[u], ids[v]);
                }
            }
        }

        if (ids[u] == lowlink[u]) {
            int v;
            do {
                v = matrix_stack.pop();
                onStack[v] = false;
                ids[v] = ids[u];
            } while (v != u);
        }
    }

    public static ArrayList<ArrayList<Integer>> findSCCsUsingAdjList(ArrayList<Integer>[] adjList, ArrayList<Integer>[] revAdjList, int n) {

        visited = new boolean[n + 1];
        scc = new int[n + 1];
        sccIdx = 0;
        sccs = new ArrayList<>();

        adj = adjList;
        revAdj = revAdjList;

        // First DFS pass to get the order of vertices
        Stack<Integer> stack = new Stack<>();
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs1(i, stack);
            }
        }

        // Second DFS pass to get the strongly connected components
        visited = new boolean[n + 1];
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                ArrayList<Integer> scc = new ArrayList<>();
                dfs2(v, scc);
                sccs.add(scc);
                sccIdx++;
            }
        }
        return sccs;
    }

    // First DFS pass to get the order of vertices
    static void dfs1(int v, Stack<Integer> stack) {
        visited[v] = true;
        for (int u : adj[v]) {
            if (!visited[u]) {
                dfs1(u, stack);
            }
        }
        stack.push(v);
    }

    // Second DFS pass to get the strongly connected components
    static void dfs2(int v, ArrayList<Integer> scc) {
        visited[v] = true;
        scc.add(v);
        for (int u : revAdj[v]) {
            if (!visited[u]) {
                dfs2(u, scc);
            }
        }
    }

    public static ArrayList<ArrayList<Integer>> findSCCsUsingAdjArray(int[] adjVertices, int[] endingIndex) {
        TarjanAlgorithm_test.adjVertices = adjVertices;
        TarjanAlgorithm_test.endingIndex = endingIndex;

        int n = endingIndex.length - 1;

        visited = new boolean[n + 1];
        stack = new Stack<>();
        sccs = new ArrayList<>();

        // First DFS to fill the stack
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs1_array(i);
            }
        }

        // Transpose the graph
        int[] transposedAdj = transposeGraph();

        // Reset visited array
        Arrays.fill(visited, false);

        // Second DFS to find SCCs
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                ArrayList<Integer> scc = new ArrayList<>();
                dfs2_array(v, transposedAdj, scc);
                sccs.add(scc);
            }
        }

        return sccs;
    }

    private static void dfs1_array(int v) {
        visited[v] = true;
        for (int i = endingIndex[v - 1] + 1; i <= endingIndex[v]; i++) {
            int u = adjVertices[i];
            if (!visited[u]) {
                dfs1_array(u);
            }
        }
        stack.push(v);
    }

    private static void dfs2_array(int v, int[] adj, ArrayList<Integer> scc) {
        visited[v] = true;
        scc.add(v);
        for (int i = endingIndex[v - 1] + 1; i <= endingIndex[v]; i++) {
            int u = adj[i];
            if (!visited[u]) {
                dfs2_array(u, adj, scc);
            }
        }
    }

    private static int[] transposeGraph() {
        int n = endingIndex.length - 1;
        int[] transposedAdj = new int[adjVertices.length];
        int[] count = new int[n + 1];

        // Count the number of outgoing edges for each vertex
        for (int i = 1; i < adjVertices.length; i++) {
            count[adjVertices[i]]++;
        }

        // Compute the starting index of each vertex in the transposed graph
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            int temp = count[i];
            count[i] = sum;
            sum += temp;
        }

        // Transpose the edges
        for (int i = 1; i < adjVertices.length; i++) {
            int vertex = adjVertices[i];
            int idx = count[vertex];
            transposedAdj[idx] = i;
            count[vertex]++;
        }

        return transposedAdj;
    }
}

//// in java

//class Graph_adj_array {
//    public int vertex_num;
//    public List<list_Node> graph;
//    public List<Integer> visited;
//
//    public Graph_adj_array(int vertex_num) {
//        this.vertex_num = vertex_num;
//        this.graph = new ArrayList<>();
//        this.visited = new ArrayList<>();
//
//        for (int i = 0; i < vertex_num; i++) {
//            graph.add(new list_Node(0));
//            visited.add(0);
//            graph.get(i).next = new ArrayList<>();
//        }
//    }
//
//    public void insert(int node_from, int node_to) {
//        List<Integer> target_array = graph.get(node_from - 1).next;
//        int l = graph.get(node_from - 1).value;
//
//        if (l == 0) {
//            target_array.add(node_to);
//            graph.get(node_from - 1).value += 1;
//        } else {
//            int i = 0;
//            while (i < l) {
//                if (target_array.get(i) > node_to) {
//                    break;
//                }
//                i++;
//            }
//            target_array.add(i, node_to);
//            graph.get(node_from - 1).value += 1;
//        }
//    }
//
//    public void visit(int visit_node) {
//        visited.set(visit_node - 1, 1);
//    }
//
//    public List<Integer> get_list(int node_from) {
//        List<Integer> target = graph.get(node_from).next;
//        List<Integer> adj = new ArrayList<>();
//
//        for (int i : target) {
//            adj.add(i - 1);
//        }
//        return adj;
//    }
//
//    public Graph_adj_array transpose() {
//        Graph_adj_array A_t = new Graph_adj_array(vertex_num);
//        for (int i = 0; i < vertex_num; i++) {
//            for (int j = 0; j < graph.get(i).value; j++) {
//                A_t.insert(graph.get(i).next.get(j), i + 1);
//            }
//        }
//        return A_t;
//    }
//}

//class list_Node {
//    public int value;
//    public List<Integer> next;
//
//    public list_Node(int value) {
//        this.value = value;
//        this.next = new ArrayList<>();
//    }
//}
//
//class StronglyConnectedComponents {
//    public static void startWithOrder(Graph_adj_array G, int homeNode, List<Integer> order) {
//        G.visited.set(homeNode, 1);
//        List<Integer> adjNode = G.get_list(homeNode);
//        if (adjNode.isEmpty()) {
//            order.add(homeNode);
//        } else {
//            for (int i : adjNode) {
//                if (G.visited.get(i) == 0) {
//                    startWithOrder(G, i, order);
//                }
//            }
//            order.add(homeNode);
//        }
//    }
//
//    public static void start(Graph_adj_array G, int homeNode, List<Integer> SCC) {
//        G.visited.set(homeNode, 1);
//        SCC.add(homeNode + 1);
//        List<Integer> adjNode = G.get_list(homeNode);
//        if (!adjNode.isEmpty()) {
//            for (int i : adjNode) {
//                if (G.visited.get(i) == 0) {
//                    start(G, i, SCC);
//                }
//            }
//        }
//    }
//
//    public static ArrayList<Integer> firstDFS(Graph_adj_array G) {
//        int vertexNum = G.vertex_num;
//        List<Integer> visited = G.visited;
//        ArrayList<Integer> order = new ArrayList<>();
//        for (int i = 0; i < vertexNum; i++) {
//            if (visited.get(i) == 0) {
//                startWithOrder(G, i, order);
//            }
//        }
//        return order;
//    }
//
//    public static ArrayList<ArrayList<Integer>> secondDFS(Graph_adj_array G, List<Integer> order) {
//        int vertexNum = G.vertex_num;
//        List<Integer> visited = G.visited;
//        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
//        for (int i = vertexNum - 1; i >= 0; i--) {
//            if (visited.get(order.get(i)) == 0) {
//                ArrayList<Integer> subSCC = new ArrayList<>();
//                start(G, order.get(i), subSCC);
//                result.add(subSCC);
//            }
//        }
//        return result;
//    }
//
//    public static ArrayList<ArrayList<Integer>> findSCC(Graph_adj_array G) {
//        ArrayList<Integer> order = firstDFS(G);
//        Graph_adj_array G_t = G.transpose();
//        ArrayList<ArrayList<Integer>> SCCList = secondDFS(G_t, order);
//        return SCCList;
//    }
