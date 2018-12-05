package tests.bytemaptest;

public abstract class AbstractObjIntGetTest implements ByteMapTest {
    byte[][] m_keys;
    @Override
    public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            m_keys[i] = new byte[16];
            System.arraycopy(keys[i], 0, m_keys[i], 0, 16);
        }
    }
}
