import java.util.LinkedList;

public class User {
    private Version version;
    private LinkedList<User> coaches;
    private LinkedList<User> students;

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
