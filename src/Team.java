import java.util.LinkedList;
import java.util.Objects;

public class Team {

    private LinkedList<User> teamMembers;


    public Team() {
        teamMembers = new LinkedList<>();
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

    public boolean equals(Object o) {
        if (o instanceof Team) {
            return ((Team) o).getTeamMembers().equals(this.getTeamMembers());
        }
        else {
            return false;
        }
    }
}
