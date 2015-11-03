import java.util.LinkedList;

public class User {

    private static final Version LATEST_VERSION = new Version(1, 0, 0);

    public static class Builder {
        private LinkedList<User> coaches = new LinkedList<>();
        private LinkedList<User> students = new LinkedList<>();

        public Builder addCoach(User user) {
            if (user != null) {
                coaches.add(user);
            }
            return this;
        }

        public LinkedList<User> getCoaches() {
            return new LinkedList<>(coaches);
        }

        public Builder addStudent(User user) {
            if (user != null) {
                students.add(user);
            }
            return this;
        }

        public LinkedList<User> getStudents() {
            return new LinkedList<>(students);
        }

        public User build(String username) {
            if (username == null) {
                throw new IllegalArgumentException("Username cannot be null");
            }
            return new User(username, this);
        }
    }


    private final String userName;
    private Version currentVersion;
    private LinkedList<User> coaches;
    private LinkedList<User> students;

    private User(String userName, Builder userBuilder) {
        this.userName = userName;
        currentVersion = LATEST_VERSION;
        coaches = userBuilder.getCoaches();
        students = userBuilder.getStudents();
    }

    public String getUserName() {
        return userName;
    }

    public Version getCurrentVersion() {
        return new Version(currentVersion);
    }

    public void setCurrentVersion(Version currentVersion) {
        this.currentVersion = currentVersion;
    }

    public LinkedList<User> getStudents() {
        return new LinkedList<>(students);
    }

    public void addStudent(User newStudent) {
        students.add(newStudent);
    }

    public void removeStudent(User student) {
        students.remove(student);
    }

    public LinkedList<User> getCoaches() {
        return new LinkedList<>(coaches);
    }

    public void addCoach(User newCoach) {
        coaches.add(newCoach);
    }

    public void removeCoach(User coach) {
        coaches.remove(coach);
    }
}
