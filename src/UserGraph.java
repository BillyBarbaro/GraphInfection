import java.util.HashMap;

public class UserGraph {

    public static class Builder {
        private HashMap<String, User> users = new HashMap<>();

        public Builder addUser(User user) {
            if (user != null) {
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
        this.users = new HashMap<>(userGraphBuilder.getUsers());
    }

    public HashMap<String, User> getUsers() {
        return new HashMap<>(users);
    }

    public void addUser(User user) {
        if (user != null) {
            this.users.put(user.getUserName(), user);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            this.users.remove(user.getUserName());
        }
    }
}
