package tests.longobjecttest;

public abstract class AbstractLongObjGetTest implements LongMapTest {
    long[] m_keys;
    @Override
    public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new long[keys.length];
        System.arraycopy(keys, 0, m_keys, 0, keys.length);
    }
}
