package tests.longobjecttest;

public interface LongMapTest {
    public void setup(final long[] keys, final float fillFactor, final int oneFailureOutOf);

    public int test();
}
