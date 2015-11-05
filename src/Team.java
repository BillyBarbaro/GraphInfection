import java.util.LinkedList;

public class Team {

    private LinkedList<User> teamMembers;


    public Team() {
    }

    public LinkedList<User> getTeamMembers() {
        return new LinkedList<>(teamMembers);
    }

    public void addTeamMember(User newMember) {
        teamMembers.add(newMember);
    }

    public int getSize() {
        return teamMembers.size();
    }
}
