import java.util.*;

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
            hasTeam = assigned;
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
            users.put(userName, userNodes.get(userName).getUser());
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
            User user = teamQueue.poll();
            if (isUserInGraph(user) && !userNodes.get(user.getUserName()).hasTeam()) {
                team.addTeamMember(user);
                updateQueue(user, teamQueue);
                userNodes.get(user.getUserName()).setHasTeam(true);
            }
        }
        return team;
    }

    public ArrayList<Team> findTeams() {
        resetTeams();
        ArrayList<Team> teams = new ArrayList<>();
        for (UserNode node : userNodes.values()) {
            if (!node.hasTeam()) {
                teams.add(createTeam(node.getUser()));
            }
        }
        return teams;
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
}
