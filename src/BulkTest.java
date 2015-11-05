public class BulkTest {

    private static final int NUMBER_OF_TEAMS = 500;

    private static final int MIN_NUMBER_COACHES = 5;
    private static final int MAX_NUMBER_COACHES = 100;

    private static final int MIN_NUMBER_STUDENTS = 15;
    private static final int MAX_NUMBER_STUDENTS = 2500;

    private static final int MIN_NUMBER_COACHES_FOR_STUDENT = 1;
    private static final int MAX_NUMBER_COACHES_FOR_STUDENT = 8;


    private static void addTeamToGraph(UserGraph graph) {

    }

    public static void main(String args[]) {
        UserGraph testGraph = new UserGraph.Builder().build();

        for (int i = 0; i < NUMBER_OF_TEAMS; i++) {
            addTeamToGraph(testGraph);
        }

        // Partition into Teams

        // Run a partial infection on the graph

        // Check how close the returned infection is to the desired

        // Check student to teacher same version ratio.
    }
}
