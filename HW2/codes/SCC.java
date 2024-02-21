import java.util.*;
import java.io.*;

public class SCC {
    static int n, m;
    static ArrayList<Integer>[] adj, revAdj;
    static boolean[] visited;
    static int[] order, scc;
    static int orderIdx, sccIdx;
    static Stack<Integer> stack;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(br.readLine());

        // Initialize adjacency list and reverse adjacency list
        adj = new ArrayList[n+1];
        revAdj = new ArrayList[n+1];
        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
            revAdj[i] = new ArrayList<>();
        }

        // Read in edges
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            adj[u].add(v);
            revAdj[v].add(u);
        }

        // Find strongly connected components
        visited = new boolean[n+1];
        order = new int[n];
        orderIdx = 0;
        stack = new Stack<>();
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs1(i);
            }
        }
        visited = new boolean[n+1];
        scc = new int[n+1];
        sccIdx = 0;
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                dfs2(v);
                sccIdx++;
            }
        }

        // Calculate XOR of vertices in each SCC
        int[] sccXOR = new int[sccIdx];
        for (int i = 1; i <= n; i++) {
            sccXOR[scc[i]] ^= i;
        }

        // Count number of SCCs and output XOR results in increasing order
        Arrays.sort(sccXOR);
        System.out.println(sccIdx);
        for (int i = 0; i < sccIdx; i++) {
            System.out.print(sccXOR[i] + " ");
        }
    }

    // First DFS pass to get the reverse postorder of the vertices
    static void dfs1(int v) {
        visited[v] = true;
        for (int u : adj[v]) {
            if (!visited[u]) {
                dfs1(u);
            }
        }
        order[orderIdx++] = v;
        stack.push(v);
    }

    // Second DFS pass to get the strongly connected components
    static void dfs2(int v) {
        visited[v] = true;
        scc[v] = sccIdx;
        for (int u : revAdj[v]) {
            if (!visited[u]) {
                dfs2(u);
            }
        }
    }
}

