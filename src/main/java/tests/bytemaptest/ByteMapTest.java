package tests.bytemaptest;

public interface ByteMapTest {
    public void setup( final byte[][] keys, final float fillFactor, final int oneFailureOutOf );

    public int test();
}
