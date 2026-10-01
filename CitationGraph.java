
import java.util.*;

public class CitationGraph {

    // Stores all papers
    private HashMap<String, Paper> papers;

    // Adjacency List
    // Key = Paper
    // Value = Papers it cites
    private HashMap<String, ArrayList<String>> graph;

    public CitationGraph() {

        papers = new HashMap<>();
        graph = new HashMap<>();

    }

    // Add Paper
    public void addPaper(Paper paper) {

        papers.put(paper.getPaperId(), paper);

        graph.putIfAbsent(paper.getPaperId(), new ArrayList<>());

    }

    // Add Citation
    public void addCitation(String sourcePaper, String destinationPaper) {

        if(graph.containsKey(sourcePaper) &&
                graph.containsKey(destinationPaper))
        {

            graph.get(sourcePaper).add(destinationPaper);

        }

    }

    // Display Graph
    public void displayGraph() {
        displayGraph(graph.size());
    }

    public void displayGraph(int maxPapers) {

        System.out.println("\n===== Citation Network =====");

        ArrayList<String> paperIds = new ArrayList<>(graph.keySet());
        Collections.sort(paperIds);
        int displayedCount = Math.min(Math.max(maxPapers, 0), paperIds.size());
        System.out.printf("Showing %d of %d papers%n", displayedCount, paperIds.size());
        System.out.println("Citation edges: citing paper ---> cited paper");

        for(int index = 0; index < displayedCount; index++) {
            String paper = paperIds.get(index);
            ArrayList<String> neighbours = graph.get(paper);

            if (neighbours.isEmpty()) {
                System.out.println("[" + paper + "] (no outgoing citations)");
                continue;
            }

            for (String neighbour : neighbours) {
                printCitationEdge(paper, neighbour);
            }

        }

        if (displayedCount < paperIds.size()) {
            System.out.println("... remaining papers omitted");
        }

    }

    private void printCitationEdge(String source, String destination) {
        String sourceLabel = " " + source + " ";
        String destinationLabel = " " + destination + " ";
        String sourceBorder = "+" + "-".repeat(sourceLabel.length()) + "+";
        String destinationBorder = "+" + "-".repeat(destinationLabel.length()) + "+";

        System.out.println(sourceBorder + "     " + destinationBorder);
        System.out.println("|" + sourceLabel + "| ---> |" + destinationLabel + "|");
        System.out.println(sourceBorder + "     " + destinationBorder);
    }

    // Return the graph
    public HashMap<String, ArrayList<String>> getGraph() {
        return graph;
    }

    // Return all papers
    public HashMap<String, Paper> getPapers() {
        return papers;
    }

    // Search paper by ID
    public void searchPaper(String paperId) {

        if (papers.containsKey(paperId)) {

            System.out.println("\n===== Paper Details =====");

            System.out.println(papers.get(paperId));

        } else {

            System.out.println("Paper Not Found!");

        }

    }

}