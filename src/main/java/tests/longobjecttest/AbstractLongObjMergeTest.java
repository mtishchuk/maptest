package tests.longobjecttest;

public abstract class AbstractLongObjMergeTest implements LongMapTest {
    long [] m_keys;
    long [] m_keys2;

    @Override
    public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new long[keys.length / 2];
        m_keys2 = new long[keys.length];
        System.arraycopy(keys, 0, m_keys2, 0, keys.length);
        int index = 0;
        for (int i = 0; i < keys.length; i++) {
            if (i % 2 == 0) {
                m_keys[index++] = keys[i];
            }

        }
    }
}