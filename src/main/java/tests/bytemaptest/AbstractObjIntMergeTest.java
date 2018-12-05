package tests.bytemaptest;

public abstract class AbstractObjIntMergeTest implements ByteMapTest {

    public byte [][] m_keys;
    public byte [][] m_keys2;
    @Override
    public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
        m_keys = new byte[keys.length / 2][];
        m_keys2 = new byte[keys.length][];
        int index = 0;
        for (int i = 0; i < keys.length; i++) {
            if (i % 2 == 0) {
                m_keys[index] = new byte[16];
                System.arraycopy(keys[i], 0, m_keys[index++], 0, 16);
            }
            m_keys2[i] = new byte[16];
            System.arraycopy(keys[i], 0, m_keys2[i], 0, 16);
        }
    }
}
