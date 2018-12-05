package tests.intobjecttest;

public abstract class AbstractIntObjMergeTest implements IntMapTest {
    int [] m_keys;
    int [] m_keys2;

    @Override
    public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new int[keys.length / 2];
        m_keys2 = new int[keys.length];
        System.arraycopy(keys, 0, m_keys2, 0, keys.length);
        int index = 0;
        for (int i = 0; i < keys.length; i++) {
            if (i % 2 == 0) {
                m_keys[index++] = keys[i];
            }

        }
    }
}