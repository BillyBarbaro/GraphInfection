public class Version implements Comparable<Version> {

    private Integer majorNumber;
    private Integer minorNumber;
    private Integer revisionNumber;

    public Version(Integer majorNumber, Integer minorNumber, Integer revisionNumber) {
        this.majorNumber = majorNumber;
        this.minorNumber = minorNumber;
        this.revisionNumber = revisionNumber;
    }

    @Override
    public int compareTo(Version otherVersion) {
        if (otherVersion == null) {
            return 1;
        }

        Integer majorDifference = this.majorNumber - otherVersion.majorNumber;
        Integer minorDifference = this.minorNumber - otherVersion.minorNumber;
        Integer revisionDifference = this.revisionNumber - otherVersion.revisionNumber;

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
}
