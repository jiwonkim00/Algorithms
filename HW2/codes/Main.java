import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static int n, m;
    public static void main(String[] args) throws IOException {
        //version_1
        int[][] adjMatrix = null;
        //version_2
        ArrayList<Integer>[] adjList = null;
        ArrayList<Integer>[] adjList_rev = null;
        //version_3
        Graph_adj_array G = null;

        //For Submission
        int version = Integer.parseInt(args[0]);
        String inputFile = args[1];
        String outputFile = args[2];

        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        n = Integer.parseInt(br.readLine());
        m = Integer.parseInt(br.readLine());

        if (version == 1) {
            adjMatrix = new int[n + 1][n + 1];
            for (int i = 0; i < m; i++) {
                String[] line = br.readLine().split(" ");
                int u = Integer.parseInt(line[0]);
                int v = Integer.parseInt(line[1]);
                adjMatrix[u][v] = 1;
            }

        } else if (version == 2) {
            adjList = new ArrayList[n + 1];
            adjList_rev = new ArrayList[n+1];

            for (int i = 1; i <= n; i++) {
                adjList[i] = new ArrayList<>();
                adjList_rev[i] = new ArrayList<>();
            }
            for (int i = 0; i < m; i++) {
                String[] line = br.readLine().split(" ");
                int u = Integer.parseInt(line[0]);
                int v = Integer.parseInt(line[1]);
                adjList[u].add(v);
                adjList_rev[v].add(u);
            }

        } else if (version == 3) {
            G = new Graph_adj_array(n);
            for (int i = 0; i < m; i++) {
                String[] line = br.readLine().split(" ");
                int s = Integer.parseInt(line[0]);
                int t = Integer.parseInt(line[1]);
                G.insert(s, t);
            }

        }

        br.close();

        ArrayList<ArrayList<Integer>> sccs = null;
        if (version == 1) {
            sccs = TarjanAlgorithm.findSCCsUsingAdjMatrix(adjMatrix);
        } else if (version == 2) {
            sccs = TarjanAlgorithm.findSCCsUsingAdjList(adjList, adjList_rev, n);
        } else if (version == 3) {
            StronglyConnectedComponents S = new StronglyConnectedComponents();
            sccs = S.findSCC(G);
        }

        int numSCCs = sccs.size();
        ArrayList<Integer> xorResults = new ArrayList<>();
        int i = 0;
        for (ArrayList<Integer> scc : sccs) {
            int xor = 0;
            for (int vertex : scc) {
                xor ^= vertex;
            }
            xorResults.add(xor);
        }
        Collections.sort(xorResults);

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        bw.write(Integer.toString(numSCCs));
        bw.newLine();
        for (int j = 0; j < numSCCs; j++) {
            bw.write(Integer.toString(xorResults.get(j)));
            if (j < numSCCs - 1) {
                bw.write(" ");
            }
        }
        bw.close();
    }
}

class TarjanAlgorithm {

    //Adj List
    static int sccIdx;
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

    public static ArrayList<ArrayList<Integer>> findSCCsUsingAdjMatrix(int[][] adjMatrix) {
        int m_length = adjMatrix.length;
        index = 0;
        lowlink = new int[m_length];
        onStack = new boolean[m_length];
        matrix_stack = new Stack<Integer>();
        ids = new int[m_length];
        Arrays.fill(ids, -1);

        for (int u = 1; u < m_length; u++) {
            if (ids[u] == -1) {
                strongConnectUsingAdjMatrix(adjMatrix, u);
            }
        }

        ArrayList<ArrayList<Integer>> sccs = new ArrayList<ArrayList<Integer>>();
        int[] sccSizes = new int[m_length];
        for (int i = 1; i < m_length; i++) {
            sccSizes[ids[i]]++;
        }
        for (int i = 1; i < m_length; i++) {
            sccs.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < m_length; i++) {
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

        visited = new boolean[n+1];
        scc = new int[n+1];
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
        visited = new boolean[n+1];
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

//    public static ArrayList<ArrayList<Integer>> findSCCsUsingAdjArray (int[]array_vertices, ArrayList<Integer>[] array_edges, int n, int m) {
//
//        ArrayList<ArrayList<Integer>> sccs = new ArrayList<>();
//        Stack<Integer> stack = new Stack<>();
//        int[] low = new int[n+1];
//        int[] ids = new int[n+1];
//        boolean[] onStack = new boolean[n+1];
//        int id = 0;
//
//        for (int i = 1; i <= n; i++) {
//            if (ids[i] == 0) {
//                dfs(i, array_vertices, array_edges, stack, low, ids, onStack, id, sccs);
//            }
//        }
//
//        return sccs;
//    }
//    private static void dfs(int at, int[] array_vertices, ArrayList<Integer>[] array_edges, Stack<Integer> stack, int[] low, int[] ids, boolean[] onStack, int id, ArrayList<ArrayList<Integer>> sccs) {
//        stack.push(at);
//        onStack[at] = true;
//        ids[at] = low[at] = ++id;
//
//        for (int to : array_edges[at]) {
//            if (ids[to] == 0) {
//                dfs(to, array_vertices, array_edges, stack, low, ids, onStack, id, sccs);
//                low[at] = Math.min(low[at], low[to]);
//            } else if (onStack[to]) {
//                low[at] = Math.min(low[at], ids[to]);
//            }
//        }
//
//        if (ids[at] == low[at]) {
//            ArrayList<Integer> scc = new ArrayList<>();
//            int node;
//            do {
//                node = stack.pop();
//                onStack[node] = false;
//                scc.add(node);
//            } while (node != at);
//            sccs.add(scc);
//        }
//    }
}

class Graph_adj_array {
    public int vertex_num;
    public List<list_Node> graph;
    public List<Integer> visited;

    public Graph_adj_array(int vertex_num) {
        this.vertex_num = vertex_num;
        this.graph = new ArrayList<>();
        this.visited = new ArrayList<>();

        for (int i = 0; i < vertex_num; i++) {
            graph.add(new list_Node(0));
            visited.add(0);
            graph.get(i).next = new ArrayList<>();
        }
    }

    public void insert(int node_from, int node_to) {
        List<Integer> target_array = graph.get(node_from - 1).next;
        int l = graph.get(node_from - 1).value;

        if (l == 0) {
            target_array.add(node_to);
            graph.get(node_from - 1).value += 1;
        } else {
            int i = 0;
            while (i < l) {
                if (target_array.get(i) > node_to) {
                    break;
                }
                i++;
            }
            target_array.add(i, node_to);
            graph.get(node_from - 1).value += 1;
        }
    }

    public void visit(int visit_node) {
        visited.set(visit_node - 1, 1);
    }

    public List<Integer> get_list(int node_from) {
        List<Integer> target = graph.get(node_from).next;
        List<Integer> adj = new ArrayList<>();

        for (int i : target) {
            adj.add(i - 1);
        }
        return adj;
    }

    public Graph_adj_array transpose() {
        Graph_adj_array A_t = new Graph_adj_array(vertex_num);
        for (int i = 0; i < vertex_num; i++) {
            for (int j = 0; j < graph.get(i).value; j++) {
                A_t.insert(graph.get(i).next.get(j), i + 1);
            }
        }
        return A_t;
    }
}

class list_Node {
    public int value;
    public List<Integer> next;

    public list_Node(int value) {
        this.value = value;
        this.next = new ArrayList<>();
    }
}

class StronglyConnectedComponents {
    public static void startWithOrder(Graph_adj_array G, int homeNode, List<Integer> order) {
        G.visited.set(homeNode, 1);
        List<Integer> adjNode = G.get_list(homeNode);
        if (adjNode.isEmpty()) {
            order.add(homeNode);
        } else {
            for (int i : adjNode) {
                if (G.visited.get(i) == 0) {
                    startWithOrder(G, i, order);
                }
            }
            order.add(homeNode);
        }
    }

    public static void start(Graph_adj_array G, int homeNode, List<Integer> SCC) {
        G.visited.set(homeNode, 1);
        SCC.add(homeNode + 1);
        List<Integer> adjNode = G.get_list(homeNode);
        if (!adjNode.isEmpty()) {
            for (int i : adjNode) {
                if (G.visited.get(i) == 0) {
                    start(G, i, SCC);
                }
            }
        }
    }

    public static ArrayList<Integer> firstDFS(Graph_adj_array G) {
        int vertexNum = G.vertex_num;
        List<Integer> visited = G.visited;
        ArrayList<Integer> order = new ArrayList<>();
        for (int i = 0; i < vertexNum; i++) {
            if (visited.get(i) == 0) {
                startWithOrder(G, i, order);
            }
        }
        return order;
    }

    public static ArrayList<ArrayList<Integer>> secondDFS(Graph_adj_array G, List<Integer> order) {
        int vertexNum = G.vertex_num;
        List<Integer> visited = G.visited;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = vertexNum - 1; i >= 0; i--) {
            if (visited.get(order.get(i)) == 0) {
                ArrayList<Integer> subSCC = new ArrayList<>();
                start(G, order.get(i), subSCC);
                result.add(subSCC);
            }
        }
        return result;
    }

    public static ArrayList<ArrayList<Integer>> findSCC(Graph_adj_array G) {
        ArrayList<Integer> order = firstDFS(G);
        Graph_adj_array G_t = G.transpose();
        ArrayList<ArrayList<Integer>> SCCList = secondDFS(G_t, order);
        return SCCList;
    }
}