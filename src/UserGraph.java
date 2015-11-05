import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class UserGraph {

    public static class Builder {
        private HashMap<String, UserNode> userNodes = new HashMap<>();

        public Builder addUser(User user) {
            if (user != null && !userNodes.containsKey(user.getUserName())) {
                userNodes.put(user.getUserName(), new UserNode(user));
            }
            return this;
        }

        public HashMap<String, UserNode> getUserNodes() {
            return new HashMap<>(userNodes);
        }

        public UserGraph build() {
            return new UserGraph(this);
        }
    }

    private static class UserNode {
        private final User user;
        private boolean hasTeam;

        public UserNode(User user) {
            this.user = user;
        }

        public boolean hasTeam() {
            return hasTeam;
        }

        public void setHasTeam(boolean assigned) {
            hasTeam = false;
        }

        public User getUser() {
            return user;
        }
    }

    private HashMap<String, UserNode> userNodes;

    private UserGraph(Builder userGraphBuilder) {
        userNodes = userGraphBuilder.getUserNodes();
    }

    public HashMap<String, User> getUsers() {
        HashMap<String, User> users = new HashMap<>();
        for (String userName : userNodes.keySet()) {
            users.put(userName, new User(userNodes.get(userName).getUser()));
        }
        return users;
    }

    public void addUser(User user) {
        if (user != null && !userNodes.containsKey(user.getUserName())) {
            userNodes.put(user.getUserName(), new UserNode(user));
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            userNodes.remove(user.getUserName());
        }
    }

    public User getUser(String username) {
        return userNodes.get(username).getUser();
    }

    public boolean isUserInGraph(User user) {
        return userNodes.containsKey(user.getUserName());
    }

    void updateQueue(User currentUser, Queue<User> queue) {
        queue.addAll(currentUser.getCoaches());
        queue.addAll(currentUser.getStudents());
    }

    void infectUser(User currentUser, Version newVersion) {
        currentUser.setCurrentVersion(newVersion);
    }

    public int totalInfection(User userZero, Version newVersion) {
        return limitedInfection(userZero, newVersion, Integer.MAX_VALUE);
    }

    public int limitedInfection(User userZero, Version newVersion, Integer desiredUsers) {
        if (isUserInGraph(userZero)) {
            Integer infectCount = 0;
            Queue<User> infectQueue = new LinkedList<>();
            infectQueue.add(userZero);

            while (infectCount < desiredUsers && !infectQueue.isEmpty()) {
                Queue<User> nextWave = new LinkedList<>();
                for (User user : infectQueue) {
                    if (isUserInGraph(user) && user.getCurrentVersion().compareTo(newVersion) != 0) {
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

    private void resetTeams() {
        for (UserNode node : userNodes.values()) {
            node.setHasTeam(false);
        }
    }

    private Team createTeam(User firstUser) {
        Team team = new Team();

        Queue<User> teamQueue = new LinkedList<>();
        teamQueue.add(firstUser);

        while (!teamQueue.isEmpty()) {
            for (User user : teamQueue) {
                if (isUserInGraph(user) && !userNodes.get(user.getUserName()).hasTeam()) {
                    team.addTeamMember(user);
                    updateQueue(user, teamQueue);
                    userNodes.get(user.getUserName()).setHasTeam(true);
                }
            }
        }
        return team;
    }

    public LinkedList<Team> findTeams() {
        resetTeams();
        LinkedList<Team> teams = new LinkedList<>();
        for (UserNode node : userNodes.values()) {
            if (!node.hasTeam()) {
                teams.add(createTeam(node.getUser()));
            }
        }
        return teams;
    }
}
