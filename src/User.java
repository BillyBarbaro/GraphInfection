import java.util.LinkedList;

public class User {

    private final String userName;
    private Version currentVersion;
    private LinkedList<User> coaches;
    private LinkedList<User> students;

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public Version getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Version currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setStudents(LinkedList<User> students) {
        this.students = students;
    }

    public LinkedList<User> getCoaches() {
        return coaches;
    }

    public void setCoaches(LinkedList<User> coaches) {
        this.coaches = coaches;
    }

    public LinkedList<User> getStudents() {
        return students;
    }
}
