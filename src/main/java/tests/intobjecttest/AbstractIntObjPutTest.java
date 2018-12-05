package tests.intobjecttest;

public abstract class AbstractIntObjPutTest implements IntMapTest {
    int [] m_keys;
    int [] m_keys2;

    @Override
    public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new int[keys.length];
        System.arraycopy(keys, 0, m_keys, 0, keys.length);
        m_keys2 = new int[keys.length];
        System.arraycopy(keys, 0, m_keys2, 0, keys.length);
    }
}
