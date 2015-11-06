import java.util.ArrayList;
import java.util.List;
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

    private static void addRandomTeamToGraph(UserGraph graph, int teamNumber) {
        User[] coaches = addCoachesToGraph(graph, teamNumber);
        addStudentsToGraph(graph, teamNumber, coaches);
    }

    private static User[] addCoachesToGraph(UserGraph graph, int teamNumber) {
        int numberOfCoaches = getRandom(MIN_NUMBER_COACHES, MAX_NUMBER_COACHES);
        User coaches[] = new User[numberOfCoaches];
        for (int i = 0; i < numberOfCoaches; i++) {
            coaches[i] = new User.Builder().build("Coach" + teamNumber + "_" + i);
            graph.addUser(coaches[i]);
        }
        return coaches;
    }

    private static void addStudentsToGraph(UserGraph graph, int teamNumber, User[] coaches) {
        int numberOfStudents = getRandom(MIN_NUMBER_STUDENTS, MAX_NUMBER_STUDENTS);
        for (int i = 0; i < numberOfStudents; i++) {
            User student = new User.Builder().build("Student" + teamNumber + "_" + i);
            addCoachesToStudent(coaches, student);
            graph.addUser(student);
        }
    }

    private static void addCoachesToStudent(User[] coaches, User student) {
        int studentCoachCount =  getRandom(MIN_NUMBER_COACHES_FOR_STUDENT, MAX_NUMBER_COACHES_FOR_STUDENT);
        for (int j = 0; j < studentCoachCount; j++) {
            student.addCoach(coaches[getRandom(0, coaches.length - 1)]);
        }
    }

    private static UserGraph createRandomGraph(int size) {
        UserGraph testGraph = new UserGraph.Builder().build();

        // Adds the specified number of random teams to the graph
        for (int i = 0; i < size; i++) {
            addRandomTeamToGraph(testGraph, i);
        }
        return testGraph;
    }

    public static void main(String args[]) {

        UserGraph testGraph = createRandomGraph(NUMBER_OF_TEAMS);

        ArrayList<Team> teams = testGraph.findTeams();
        ArrayList<Double> infectionRatios = new ArrayList<>();
        ArrayList<Double> alternateVersionRatio = new ArrayList<>();

        if (PRINT_RESULTS) System.out.println("Desired\tActual\tRatio");
        for (Team team : teams) {
            analyzeTeam(testGraph, infectionRatios, alternateVersionRatio, team);
        }
        System.out.println("Average ratio of actual to desired: " +
                (infectionRatios.stream().mapToDouble(a -> a).average()).getAsDouble());
        System.out.println("Average percentage of students per class with the wrong version: " + (
                alternateVersionRatio.stream().mapToDouble(a -> a).average()).getAsDouble());
    }

    // Infects a team and gathers data on them.  Then gathers data from individual coaches.
    private static void analyzeTeam(UserGraph testGraph, ArrayList<Double> infectionRatios, ArrayList<Double> alternateVersionRatio, Team team) {
        Version newVersion = new Version(1, 1, 1);

        gatherPartialInfectionData(testGraph, infectionRatios, team, newVersion);

        gatherTeamData(alternateVersionRatio, team, newVersion);
    }

    // Gathers data about how close the number of users infected by partial infection is to the desired number
    private static void gatherPartialInfectionData(UserGraph testGraph, ArrayList<Double> infectionRatios, Team team, Version newVersion) {
        int desiredInfected = getRandom(1, team.getSize());
        int actualInfected = testGraph.limitedInfection(team.getTeamMembers().get(0), newVersion, desiredInfected);
        infectionRatios.add((double) actualInfected / desiredInfected);

        if (PRINT_RESULTS){
            System.out.println(desiredInfected + "\t" + actualInfected + "\t" + (double) actualInfected/desiredInfected);
        }
    }

    // After the team has been infected, check how many students of each coach are infected.
    private static void gatherTeamData(ArrayList<Double> alternateVersionRatio, Team team, Version newVersion) {
        for (User user : team.getTeamMembers()) {
            if (user.getUserName().contains("Coach")) {
                gatherClassData(alternateVersionRatio, newVersion, user);
            }
        }
    }

    // Find the ratio for each coach's students running alternate versions.
    private static void gatherClassData(ArrayList<Double> alternateVersionRatio, Version newVersion, User coach) {
        int upgraded = countUsersWithVersion(newVersion, coach.getStudents());
        if (coach.getStudents().size() != 0) {
            /* Determine what version the majority of the class is running and calculate the ratio of the class on the
                alternate version
            */
            if (upgraded > coach.getStudents().size() / 2) {
                alternateVersionRatio.add((coach.getStudents().size() - upgraded) / (double) coach.getStudents().size());
            } else {
                alternateVersionRatio.add(upgraded / (double) coach.getStudents().size());
            }
            
            if (PRINT_RESULTS) {
                System.out.println(coach.getStudents().size() - upgraded + "\t" + upgraded + "\t" + (double) upgraded / coach.getStudents().size());
            }
        }
    }

    private static int countUsersWithVersion(Version newVersion, List<User> users) {
        int upgraded = 0;
        for (User student : users) {
            if (student.getCurrentVersion().equals(newVersion)) {
                upgraded++;
            }
        }
        return upgraded;
    }
}
