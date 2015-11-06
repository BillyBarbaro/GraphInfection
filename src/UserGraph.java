import java.util.*;

/*
    Graph holding/representing the relations between all users of the application
 */
public class UserGraph {

    /* Inner class to hold instances in the graph.  Allows us to add attributes to keep track of properties
    internal to the graph */
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
            hasTeam = assigned;
        }

        public User getUser() {
            return user;
        }
    }

    // Builder pattern to make it easier to add initial users to the graph.
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

    // Map Username to the instance of the user.
    private HashMap<String, UserNode> userNodes;

    private UserGraph(Builder userGraphBuilder) {
        userNodes = userGraphBuilder.getUserNodes();
    }

    // Return a map of Usernames -> Users
    public HashMap<String, User> getUsers() {
        HashMap<String, User> users = new HashMap<>();
        for (String userName : userNodes.keySet()) {
            users.put(userName, userNodes.get(userName).getUser());
        }
        return users;
    }

    // Add a previously non-existant user to the graph
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

    // Return the user with the specified username
    public User getUser(String username) {
        return userNodes.get(username).getUser();
    }

    public boolean isUserInGraph(User user) {
        return userNodes.containsKey(user.getUserName());
    }

    /* Total infection is a limited infection where we attempt to infect as many users as possible from a point.
        Returns the number of users infected by the call.
     */
    public int totalInfection(User userZero, Version newVersion) {
        return limitedInfection(userZero, newVersion, Integer.MAX_VALUE);
    }

    /* Proceeds in waves out from the inital user until the threshold is met or there or no more connected users to
        infect.  Returns the number of users it infected.
    */
    public int limitedInfection(User userZero, Version newVersion, Integer desiredUsers) {
        if (isUserInGraph(userZero) && !userZero.getCurrentVersion().equals(newVersion)) {
            Integer infectCount = 0;
            Queue<User> infectQueue = new LinkedList<>();
            infectQueue.add(userZero);

            while (infectCount < desiredUsers && !infectQueue.isEmpty()) {
                Queue<User> nextWave = new LinkedList<>();
                infectCount += infectWave(newVersion, infectQueue, nextWave);
                infectQueue = nextWave;
            }
            return infectCount;
        }
        else {
            if (!isUserInGraph(userZero)) {
                throw new IllegalArgumentException("The specified user is not in the graph");
            }
            else {
                throw new IllegalArgumentException("The specified user already has the given version");
            }
        }
    }

    /*
        Attempts to infect all users in the given queue with the given version.  Populates the nextWave queue with all
        connected users, then returns the number of users that were infected.
    */
    private Integer infectWave(Version newVersion, Queue<User> infectQueue, Queue<User> nextWave) {
        int infectCount = 0;
        for (User user : infectQueue) {
            // This could be changed if you want newer users to not be downgraded.
            if (isUserInGraph(user) && user.getCurrentVersion().compareTo(newVersion) != 0) {
                updateQueue(user, nextWave);
                infectUser(user, newVersion);
                infectCount++;
            }
        }
        return infectCount;
    }

    // Given a user and a queue, adds all connected users to the queue.
    void updateQueue(User currentUser, Queue<User> queue) {
        queue.addAll(currentUser.getCoaches());
        queue.addAll(currentUser.getStudents());
    }

    // Used to give the specified version of the app to the user.
    void infectUser(User currentUser, Version newVersion) {
        currentUser.setCurrentVersion(newVersion);
        // Add other infection code here as needed.
    }

    private void resetTeams() {
        for (UserNode node : userNodes.values()) {
            node.setHasTeam(false);
        }
    }

    // Finds all the teams (connected components) within the graph of users.
    public ArrayList<Team> findTeams() {
        resetTeams();
        ArrayList<Team> teams = new ArrayList<>();
        /* Loop through each node, if it has not yet been put in a team, we create a team and determine all members
            of the team
        */
        for (UserNode node : userNodes.values()) {
            if (!node.hasTeam()) {
                teams.add(createTeam(node.getUser()));
            }
        }
        return teams;
    }

    // Creates a team by iterating over all the users connected to the given user.
    private Team createTeam(User firstUser) {
        Team team = new Team();

        Queue<User> teamQueue = new LinkedList<>();
        teamQueue.add(firstUser);

        while (!teamQueue.isEmpty()) {
            User user = teamQueue.poll();
            if (isUserInGraph(user) && !userNodes.get(user.getUserName()).hasTeam()) {
                team.addTeamMember(user);
                updateQueue(user, teamQueue);
                userNodes.get(user.getUserName()).setHasTeam(true);
            }
        }
        return team;
    }

    public boolean totalInfectionExact(Version newVersion, int desiredUsersCount) {
        ArrayList<Team> allTeams = findTeams();
        Collections.sort(allTeams, (team1, team2) -> team1.getSize() - team2.getSize());
        List<Team> teams = removeLargerValues(allTeams, desiredUsersCount);

        // The trivial case where each individual team is larger than the desired number of infections
        if (teams.size() == 0) {
            return false;
        }

        // The trivial case where there is a team of the desired size.
        if (teams.get(teams.size() - 1).getSize() == desiredUsersCount) {
            totalInfection(teams.get(teams.size() - 1).getTeamMembers().get(0), newVersion);
            return true;
        }

        List<Team> toInfect = findInfectList(teams, desiredUsersCount);
        if (toInfect != null) {
            infectTeams(toInfect, newVersion);
        }
        return toInfect != null;
    }

    private List<Team> removeLargerValues(List<Team> teams, int maxValue) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            if (teams.get(i).getSize() <= maxValue) {
                return teams.subList(0, i+1);
            }
        }
        return new ArrayList<>();
    }

    private List<Team> findInfectList(List<Team> teams, int desiredUsersCount) {
        class SearchNode {
            int sum;
            List<Team> teams;

            public SearchNode(Team initialTeam) {
                sum = 0;
                teams = new LinkedList<>();
                addTeam(initialTeam);
            }

            public SearchNode(SearchNode node) {
                sum = node.getSum();
                teams = new ArrayList<>(node.getTeams());
            }

            public int getSum() {
                return sum;
            }

            public void addTeam(Team newTeam) {
                sum += newTeam.getSize();
                teams.add(newTeam);
            }
            public List<Team> getTeams() {
                return teams;
            }
        }

        SearchNode nodes[] = new SearchNode[desiredUsersCount];
        for (Team team : teams) {
            for (int i = 0; i < nodes.length; i++) {
                SearchNode currentNode = nodes[i];
                if (currentNode != null) {
                    int newSize = currentNode.getSum() + team.getSize();
                    if (newSize == desiredUsersCount) {
                        currentNode.addTeam(team);
                        return currentNode.getTeams();
                    }
                    else if (newSize <= desiredUsersCount && nodes[newSize] == null) {
                        SearchNode newNode = new SearchNode(currentNode);
                        newNode.addTeam(team);
                        nodes[newSize] = newNode;
                    }
                    else {
                        // There's already a way to sum to this. No need to recompute
                    }
                }
            }
            nodes[team.getSize()] = new SearchNode(team);
        }
        return null;
    }

    private void infectTeams(List<Team> toInfect, Version newVersion) {
        for (Team team : toInfect) {
            totalInfection(team.getTeamMembers().get(0), newVersion);
        }
    }
}
