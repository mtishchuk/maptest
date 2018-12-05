package tests.intobjecttest;

public abstract class AbstractIntObjGetTest implements IntMapTest {
    int[] m_keys;
    @Override
    public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new int[keys.length];
        System.arraycopy(keys, 0, m_keys, 0, keys.length);
    }
}
