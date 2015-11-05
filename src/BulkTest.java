import java.util.ArrayList;
import java.util.Random;

public class BulkTest {

    private static final boolean PRINT_RESULTS = false;

    private static final int NUMBER_OF_TEAMS = 500;

    private static final int MIN_NUMBER_COACHES = 8;
    private static final int MAX_NUMBER_COACHES = 100;

    private static final int MIN_NUMBER_STUDENTS = 15;
    private static final int MAX_NUMBER_STUDENTS = 2500;

    private static final int MIN_NUMBER_COACHES_FOR_STUDENT = 1;
    private static final int MAX_NUMBER_COACHES_FOR_STUDENT = 8;


    private static int getRandom(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static void addTeamToGraph(UserGraph graph, int teamNumber) {
        int numberOfCoaches = getRandom(MIN_NUMBER_COACHES, MAX_NUMBER_COACHES);
        User coaches[] = new User[numberOfCoaches];
        for (int i = 0; i < numberOfCoaches; i++) {
            coaches[i] = new User.Builder().build("Coach" + teamNumber + "_" + i);
            graph.addUser(coaches[i]);
        }

        int numberOfStudents = getRandom(MIN_NUMBER_STUDENTS, MAX_NUMBER_STUDENTS);
        for (int i = 0; i < numberOfStudents; i++) {
            User student = new User.Builder().build("Student" + teamNumber + "_" + i);
            int studentCoachCount =  getRandom(MIN_NUMBER_COACHES_FOR_STUDENT, MAX_NUMBER_COACHES_FOR_STUDENT);
            for (int j = 0; j < studentCoachCount; j++) {
                student.addCoach(coaches[getRandom(0, numberOfCoaches - 1)]);
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
        ArrayList<Double> incorrectVersion = new ArrayList<>();

        if (PRINT_RESULTS) System.out.println("Desired\tActual\tRatio");
        for (Team team : teams) {
            Version newVersion = new Version(1, 1, 1);
            int desiredInfected = getRandom(1, team.getSize());
            int actualInfected = testGraph.limitedInfection(team.getTeamMembers().get(0), newVersion, desiredInfected);
            ratios.add((double) actualInfected / desiredInfected);
            if (PRINT_RESULTS) System.out.println(desiredInfected + "\t" + actualInfected + "\t" + (double) actualInfected/desiredInfected);

            for (User user : team.getTeamMembers()) {
                if (user.getUserName().contains("Coach")) {
                    int upgraded = 0;
                    for (User student : user.getStudents()) {
                        if (student.getCurrentVersion().equals(newVersion)) {
                            upgraded++;
                        }
                    }
                    if (user.getStudents().size() != 0) {
                        if (upgraded > user.getStudents().size() / 2) {
                            incorrectVersion.add((user.getStudents().size() - upgraded) / (double) user.getStudents().size());
                        } else {
                            incorrectVersion.add(upgraded / (double) user.getStudents().size());
                        }
                        if (PRINT_RESULTS)
                            System.out.println(user.getStudents().size() - upgraded + "\t" + upgraded + "\t" + (double) upgraded / user.getStudents().size());
                    }
                }
            }
        }
        System.out.println("Average ratio of actual to desired: " + (ratios.stream().mapToDouble(a -> a).average()).getAsDouble());
        System.out.println("Average percentage of students per class with the wrong version: " + (incorrectVersion.stream().mapToDouble(a -> a).average()).getAsDouble());
    }
}
