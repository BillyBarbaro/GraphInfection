import java.util.LinkedList;

public class Team {

    private LinkedList<User> teamMembers;


    public Team(LinkedList<User> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public LinkedList<User> getTeamMembers() {
        return teamMembers;
    }

    public void addTeamMember(User newMember) {
        teamMembers.add(newMember);
    }

    public int getSize() {
        return teamMembers.size();
    }
}
