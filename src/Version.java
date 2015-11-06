/*
    Represents a software version for the project.
 */
public class Version implements Comparable<Version> {

    private final Integer majorNumber;
    private final Integer minorNumber;
    private final Integer revisionNumber;

    public Version(Integer majorNumber, Integer minorNumber, Integer revisionNumber) {
        this.majorNumber = majorNumber;
        this.minorNumber = minorNumber;
        this.revisionNumber = revisionNumber;
    }

    // For creating copies of Version
    public Version(Version version) {
        majorNumber = version.getMajorNumber();
        minorNumber = version.getMinorNumber();
        revisionNumber = version.getRevisionNumber();
    }

    public Integer getMajorNumber() {
        return majorNumber;
    }

    public Integer getMinorNumber() {
        return minorNumber;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public int compareTo(Version otherVersion) {
        if (otherVersion == null) {
            return 1;
        }

        Integer majorDifference = getMajorNumber() - otherVersion.getMajorNumber();
        Integer minorDifference = getMinorNumber() - otherVersion.getMinorNumber();
        Integer revisionDifference = getRevisionNumber() - otherVersion.getRevisionNumber();

        if (majorDifference != 0) {
            return majorDifference;
        }
        if (minorDifference != 0) {
            return minorDifference;
        }
        if (revisionDifference != 0) {
            return revisionDifference;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Version) {
            Version otherVersion = (Version)o;
            boolean majorsEqual = this.getMajorNumber().equals(otherVersion.getMajorNumber());
            boolean minorsEqual = this.getMinorNumber().equals(otherVersion.getMinorNumber());
            boolean revisionsEqual = this.getRevisionNumber().equals(otherVersion.getRevisionNumber());
            return majorsEqual && minorsEqual && revisionsEqual;
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return majorNumber + "." + minorNumber + "." + revisionNumber;
    }
}
