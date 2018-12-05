package tests.bytemaptest;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;

import java.util.Arrays;

public class FastUtilObj2IntCustomWithNullGetTest extends FastUtilObj2IntCustomTest {
    @Override
    public ByteMapTest getTest() {
        return new FastUtilObjectIntGetTest();
    }

    private static class FastUtilObjectIntGetTest extends AbstractObjIntGetTest {
        private Object2IntOpenCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<byte[]>(keys.length, fillFactor, deviceIdHashing);
            for (int i = 0; i < keys.length; ++i) {
                if (i % oneFailureOutOf != 0) m_map.put(keys[i], (int) keys[i][0]);
            }

        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res = res ^ i;
            return res;
        }
    }
}
