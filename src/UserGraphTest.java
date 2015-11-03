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
}