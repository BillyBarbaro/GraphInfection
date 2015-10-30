public class Version implements Comparable<Version> {

    private Integer majorNumber;
    private Integer minorNumber;
    private Integer revisionNumber;

    public Version(Integer majorNumber, Integer minorNumber, Integer revisionNumber) {
        this.majorNumber = majorNumber;
        this.minorNumber = minorNumber;
        this.revisionNumber = revisionNumber;
    }

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
}
