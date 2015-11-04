import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class UserGraphTest {

    private UserGraph generateTestUserGraph() {
        User coach1 = new User.Builder().build("Coach1");
        User coach2 = new User.Builder().build("Coach2");

        User student1 = new User.Builder().addCoach(coach1).build("Student1");
        User student2 = new User.Builder().addCoach(coach1).build("Student2");
        User student3 = new User.Builder().addCoach(coach1).addCoach(coach2).build("Student3");
        User student4 = new User.Builder().addCoach(coach2).build("Student4");
        User student5 = new User.Builder().addCoach(coach2).build("Student5");
        User student6 = new User.Builder().build("Student6");

        return new UserGraph.Builder()
                .addUser(coach1)
                .addUser(coach2)
                .addUser(student1)
                .addUser(student2)
                .addUser(student3)
                .addUser(student4)
                .addUser(student5)
                .addUser(student6)
                .build();
    }

    @Test
    public void testTotalInfection() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", testGraph.totalInfection(testGraph.getUser("Student1"), newVersion), 7);
        for (String username : testGraph.getUsers().keySet()) {
            User user = testGraph.getUser(username);
            if (!username.equals("Student6")) {
                assertEquals("User not infected", new Version(1, 1, 1), user.getCurrentVersion());
            }
            else {
                assertEquals("User infected but shouldn't be", new Version(1, 0, 0), user.getCurrentVersion());
            }
        }
    }

    @Test
     public void testLimitedInfection0() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 0, testGraph.limitedInfection(testGraph.getUser("Student1"), newVersion, 0));
        for (String username : testGraph.getUsers().keySet()) {
            User user = testGraph.getUser(username);
            assertEquals("User infected but shouldn't be", new Version(1, 0, 0), user.getCurrentVersion());
        }
    }

    @Test
    public void testLimitedInfection1() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 1, testGraph.limitedInfection(testGraph.getUser("Student1"), newVersion, 1));
        for (String username : testGraph.getUsers().keySet()) {
            User user = testGraph.getUser(username);
            if (username.equals("Student1")) {
                assertEquals("User not infected", new Version(1, 1, 1), user.getCurrentVersion());
            }
            else {
                assertEquals("User infected but shouldn't be", new Version(1, 0, 0), user.getCurrentVersion());
            }
        }
    }

    @Test
    public void testLimitedInfectionIsolated() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 1, testGraph.limitedInfection(testGraph.getUser("Student6"), newVersion, 1000));
        for (String username : testGraph.getUsers().keySet()) {
            User user = testGraph.getUser(username);
            if (username.equals("Student6")) {
                assertEquals("User not infected", new Version(1, 1, 1), user.getCurrentVersion());
            }
            else {
                assertEquals("User infected but shouldn't be", new Version(1, 0, 0), user.getCurrentVersion());
            }
        }
    }

    @Test
    public void testLimitedInfectionTwoWaves() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 4, testGraph.limitedInfection(testGraph.getUser("Student1"), newVersion, 4));
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student1").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student2").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student3").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student4").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student5").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student6").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Coach1").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Coach2").getCurrentVersion());
    }

    @Test
    public void testLimitedInfectionThresholdExceeded() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 4, testGraph.limitedInfection(testGraph.getUser("Student1"), newVersion, 3));
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student1").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student2").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student3").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student4").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student5").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student6").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Coach1").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Coach2").getCurrentVersion());
    }

    @Test
    public void testLimitedInfectionMultipleCoaches() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 7, testGraph.limitedInfection(testGraph.getUser("Student3"), newVersion, 4));
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student1").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student2").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student3").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student4").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student5").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student6").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Coach1").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Coach2").getCurrentVersion());
    }

    @Test
    public void testLimitedInfectionFromCoach() {
        UserGraph testGraph = generateTestUserGraph();
        Version newVersion = new Version(1, 1, 1);
        assertEquals("Incorrect number of infections", 4, testGraph.limitedInfection(testGraph.getUser("Coach1"), newVersion, 2));
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student1").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student2").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Student3").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student4").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student5").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Student6").getCurrentVersion());
        assertEquals("User not infected", new Version(1, 1, 1), testGraph.getUser("Coach1").getCurrentVersion());
        assertEquals("User infected but shouldn't be", new Version(1, 0, 0), testGraph.getUser("Coach2").getCurrentVersion());
    }
}