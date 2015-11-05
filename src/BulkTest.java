import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BulkTest {

    private static final int NUMBER_OF_TEAMS = 500;

    private static final int MIN_NUMBER_COACHES = 8;
    private static final int MAX_NUMBER_COACHES = 100;

    private static final int MIN_NUMBER_STUDENTS = 15;
    private static final int MAX_NUMBER_STUDENTS = 2500;

    private static final int MIN_NUMBER_COACHES_FOR_STUDENT = 1;
    private static final int MAX_NUMBER_COACHES_FOR_STUDENT = 8;


    private static void addTeamToGraph(UserGraph graph, int teamNumber) {
        int numberOfCoaches = ThreadLocalRandom.current().nextInt(MIN_NUMBER_COACHES, MAX_NUMBER_COACHES + 1);
        User coaches[] = new User[numberOfCoaches];
        for (int i = 0; i < numberOfCoaches; i++) {
            coaches[i] = new User.Builder().build("Coach" + teamNumber + "_" + i);
            graph.addUser(coaches[i]);
        }

        int numberOfStudents = ThreadLocalRandom.current().nextInt(MIN_NUMBER_STUDENTS, MAX_NUMBER_STUDENTS + 1);
        for (int i = 0; i < numberOfStudents; i++) {
            User student = new User.Builder().build("Student" + teamNumber + "_" + i);
            int studentCoachCount =  ThreadLocalRandom.current().nextInt(MIN_NUMBER_COACHES_FOR_STUDENT, MAX_NUMBER_COACHES_FOR_STUDENT + 1);
            for (int j = 0; j < studentCoachCount; j++) {
                student.addCoach(coaches[ThreadLocalRandom.current().nextInt(0, numberOfCoaches)]);
            }
            graph.addUser(student);
        }
    }

    public static void main(String args[]) {
        UserGraph testGraph = new UserGraph.Builder().build();

        for (int i = 0; i < NUMBER_OF_TEAMS; i++) {
            addTeamToGraph(testGraph, i);
        }

        ArrayList<Team> teams = testGraph.findTeams();
        ArrayList<Double> ratios = new ArrayList<>();

        System.out.println("Desired\tActual\tRatio");
        for (Team team : teams) {
            int desiredInfected = ThreadLocalRandom.current().nextInt(1, team.getSize() + 1);
            int actualInfected = testGraph.limitedInfection(team.getTeamMembers().get(0), new Version(1, 1, 1), desiredInfected);
            ratios.add((double)actualInfected/desiredInfected);
            System.out.println(desiredInfected + "\t" + actualInfected + "\t" + (double)actualInfected/desiredInfected);

            // Check student to teacher same version ratio.
        }
        System.out.println("Average ratio of actual to desired: " + (ratios.stream().mapToDouble(a -> a).average()).getAsDouble());
    }
}
