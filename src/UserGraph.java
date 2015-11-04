import java.util.HashMap;
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

    void infectUser(User currentUser, Queue<User> infectQueue, Version newVersion) {
        if (currentUser.getCurrentVersion().compareTo(newVersion) != 0) {
            currentUser.setCurrentVersion(newVersion);
            infectQueue.addAll(currentUser.getCoaches());
            infectQueue.addAll(currentUser.getStudents());
        }
    }

    public void totalInfection(User userZero, Version newVersion) {
        if (isUserInGraph(userZero)) {
            Queue<User> infectQueue = new LinkedList<>();
            infectQueue.add(userZero);

            while (!infectQueue.isEmpty()) {
                User currentUser = infectQueue.poll();
                infectUser(currentUser, infectQueue, newVersion);
            }
        }
        else {
            throw new IllegalArgumentException("The specified user is not in the graph");
        }
    }

    public void limitedInfection(User userZero, Version newVersion, int desiredUsers) {
        // TODO: write limitedInfection
    }
}
