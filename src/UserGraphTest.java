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
        testGraph.totalInfection(testGraph.getUser("Student1"), newVersion);
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
}