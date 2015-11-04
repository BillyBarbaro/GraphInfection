import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Queue;

public class UserGraph {

    public static class Builder {
        private HashMap<String, User> users = new HashMap<>();

        public Builder addUser(User user) {
            if (user != null && !users.containsKey(user.getUserName())) {
                users.put(user.getUserName(), user);
            }
            return this;
        }

        public HashMap<String, User> getUsers() {
            return new HashMap<>(users);
        }

        public UserGraph build() {
            return new UserGraph(this);
        }
    }

    private HashMap<String, User> users;

    private UserGraph(Builder userGraphBuilder) {
        users = userGraphBuilder.getUsers();
    }

    public HashMap<String, User> getUsers() {
        return new HashMap<>(users);
    }

    public void addUser(User user) {
        if (user != null && !users.containsKey(user.getUserName())) {
            users.put(user.getUserName(), user);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            users.remove(user.getUserName());
        }
    }

    public User getUser(String username) {
        return getUsers().get(username);
    }

    public boolean isUserInGraph(User user) {
        return users.containsKey(user.getUserName());
    }

    void updateQueue(User currentUser, Queue<User> infectQueue) {
        infectQueue.addAll(currentUser.getCoaches());
        infectQueue.addAll(currentUser.getStudents());
    }

    void infectUser(User currentUser, Version newVersion) {
        currentUser.setCurrentVersion(newVersion);
    }

    public int totalInfection(User userZero, Version newVersion) {
        if (isUserInGraph(userZero)) {
            Integer infectCount = 0;
            Queue<User> infectQueue = new LinkedList<>();
            infectQueue.add(userZero);

            while (!infectQueue.isEmpty()) {
                User currentUser = infectQueue.poll();
                if (currentUser.getCurrentVersion().compareTo(newVersion) != 0) {
                    updateQueue(currentUser, infectQueue);
                    infectUser(currentUser, newVersion);
                    infectCount++;
                }
            }
            return infectCount;
        }
        else {
            throw new IllegalArgumentException("The specified user is not in the graph");
        }
    }

    public int limitedInfection(User userZero, Version newVersion, Integer desiredUsers) {
        if (isUserInGraph(userZero)) {
            Integer infectCount = 0;
            Queue<User> infectQueue = new LinkedList<>();
            infectQueue.add(userZero);

            while (infectCount < desiredUsers && !infectQueue.isEmpty()) {
                Queue<User> nextWave = new LinkedList<>();
                for (User user : infectQueue) {
                    if (user.getCurrentVersion().compareTo(newVersion) != 0) {
                        updateQueue(user, nextWave);
                        infectUser(user, newVersion);
                        infectCount++;
                    }
                }
                infectQueue = nextWave;
            }
            return infectCount;
        }
        else {
            throw new IllegalArgumentException("The specified user is not in the graph");
        }
    }
}
