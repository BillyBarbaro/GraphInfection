import java.util.LinkedList;

/*
    A class representing a user of the application
 */
public class User {

    private static final Version LATEST_VERSION = new Version(1, 0, 0);

    // Builder pattern for adding relations when creating a user.
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

    // Called only using the builder
    private User(String userName, Builder userBuilder) {
        this.userName = userName;
        currentVersion = LATEST_VERSION;
        coaches = userBuilder.getCoaches();

        // For every relation, add the reciprocal to the other user.
        for (User coach : coaches) {
            coach.addStudentNotReciprocal(this);
        }

        students = userBuilder.getStudents();
        for (User student : students) {
            student.addCoachNotReciprocal(this);
        }
    }

    public String getUserName() {
        return userName;
    }

    // Return a new instance of version so it can't be modified outside the setter.
    public Version getCurrentVersion() {
        return new Version(currentVersion);
    }

    public void setCurrentVersion(Version currentVersion) {
        this.currentVersion = currentVersion;
    }

    // Returns copy of the list of students.  Mutating the list can't mutate the list for the class
    public LinkedList<User> getStudents() {
        return new LinkedList<>(students);
    }

    // Add a student to the user.  Also adds this user as a coach to the student passed in.
    public User addStudent(User newStudent) {
        addStudentNotReciprocal(newStudent);
        newStudent.addCoachNotReciprocal(this);
        return this;
    }

    private void addStudentNotReciprocal(User newStudent) {
        students.add(newStudent);
    }

    // Removes a student from the User.  Also remove this user from the student's list of coaches.
    public User removeStudent(User student) {
        removeStudentNotReciprocal(student);
        student.removeCoachNotReciprocal(this);
        return this;
    }

    private User removeStudentNotReciprocal(User student) {
        students.remove(student);
        return this;
    }

    // Returns copy of the list of coaches.  Mutating the list can't mutate the list for the class
    public LinkedList<User> getCoaches() {
        return new LinkedList<>(coaches);
    }

    // Add a student to the user.  Also adds this user as a student to the coach passed in.
    public User addCoach(User newCoach) {
        addCoachNotReciprocal(newCoach);
        newCoach.addStudentNotReciprocal(this);
        return this;
    }

    private void addCoachNotReciprocal(User newCoach) {
        coaches.add(newCoach);
    }

    // Removes a coach from the User.  Also remove this user from the coach's list of students.
    public User removeCoach(User coach) {
        removeCoachNotReciprocal(coach);
        coach.removeStudentNotReciprocal(this);
        return this;
    }

    private void removeCoachNotReciprocal(User coach) {
        coaches.remove(coach);
    }

    @Override
    public String toString() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            return ((User) o).getUserName().equals(this.getUserName());
        }
        else {
            return false;
        }
    }
}
