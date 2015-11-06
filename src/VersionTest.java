import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionTest {

    @Test
    public void testEqualVersions() {
        Version v1 = new Version(0, 1, 14);
        Version v2 = new Version(0, 1, 14);
        assertTrue("Versions should be equlivalent", v1.compareTo(v2) == 0);
        assertTrue("Versions should be equlivalent", v2.compareTo(v1) == 0);
    }

    @Test
    public void testCompareToMajor() {
        Version v1 = new Version(1, 14, 9);
        Version v2 = new Version(2, 2, 3);
        assertTrue("Major comparison incorrect", v1.compareTo(v2) < 0);
        assertTrue("Major comparison incorrect", v2.compareTo(v1) > 0);
    }

    @Test
    public void testCompareToMinor() {
        Version v1 = new Version(1, 14, 9);
        Version v2 = new Version(1, 2, 3);
        assertTrue("Minor comparison incorrect", v1.compareTo(v2) > 0);
        assertTrue("Minor comparison incorrect", v2.compareTo(v1) < 0);
    }

    @Test
    public void testCompareToRevision() {
        Version v1 = new Version(4, 3, 9);
        Version v2 = new Version(4, 3, 3);
        assertTrue("Major comparison incorrect", v1.compareTo(v2) > 0);
        assertTrue("Major comparison incorrect", v2.compareTo(v1) < 0);
    }

    @Test
    public void testToString() {
        Version v1 = new Version(4, 3, 9);
        assertEquals("Incorrect version printed", "4.3.9", v1.toString());
    }
}