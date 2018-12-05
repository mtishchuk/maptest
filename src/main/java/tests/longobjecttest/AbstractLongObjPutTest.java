package tests.longobjecttest;

public abstract class AbstractLongObjPutTest implements LongMapTest {
    long [] m_keys;
    long [] m_keys2;

    @Override
    public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new long[keys.length];
        System.arraycopy(keys, 0, m_keys, 0, keys.length);
        m_keys2 = new long[keys.length];
        System.arraycopy(keys, 0, m_keys2, 0, keys.length);
    }
}
