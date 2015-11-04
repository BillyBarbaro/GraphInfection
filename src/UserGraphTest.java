import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class UserGraphTest {

    @Test
    public void testInfectSingleUser() {
        User user1 = new User.Builder().build("TestUser");
        Queue<User> infectQueue = new LinkedList<>();
        Version newVersion = new Version(1, 1, 1);

        UserGraph testGraph = new UserGraph.Builder().addUser(user1).build();
        testGraph.infectUser(user1, infectQueue, newVersion);

        assertEquals("Version updated correctly", new Version(1, 1, 1), user1.getCurrentVersion());
        assertEquals("No users added to queue", new LinkedList<User>(), infectQueue);
    }

    @Test
    public void testInfectSeveralUsers() {
        User coach1 = new User.Builder().build("Coach1");
        User coach2 = new User.Builder().build("Coach2");

        User student1 = new User.Builder().addCoach(coach1).build("Student1");
        User student2 = new User.Builder().addCoach(coach1).build("Student2");
        User student3 = new User.Builder().addCoach(coach1).addCoach(coach2).build("Student3");
        User student4 = new User.Builder().addCoach(coach2).build("Student4");
        User student5 = new User.Builder().addCoach(coach2).addStudent(student1).build("Student5");

        Queue<User> infectQueue = new LinkedList<>();
        Version newVersion = new Version(1, 1, 1);

        UserGraph testGraph = new UserGraph.Builder()
                .addUser(coach1)
                .addUser(coach2)
                .addUser(student1)
                .addUser(student2)
                .addUser(student3)
                .addUser(student4)
                .addUser(student5)
                .build();

        testGraph.infectUser(student5, infectQueue, newVersion);

        Queue<User> expectedQueue = new LinkedList<>();
        expectedQueue.add(coach2);
        expectedQueue.add(student1);

        assertEquals("Version updated correctly", new Version(1, 1, 1), student5.getCurrentVersion());
        assertEquals("No users added to queue", expectedQueue, infectQueue);


    }
}