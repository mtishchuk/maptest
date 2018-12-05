package tests.bytemaptest;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;

import java.util.Arrays;

public class FastUtilObj2IntCustomTest implements ByteTestSet {
    @Override
    public ByteMapTest putAndUpdateTest() {
        return new FastUtilObjectIntPutAndUpdateTest();
    }

    @Override
    public ByteMapTest twoPutOneRemoveTest() {
        return new FastUtilObjectIntTwoPutOneRemoveRemoveTest();
    }

    public static final Hash.Strategy<byte[]> deviceIdHashing = new Hash.Strategy<byte[]>() {
        @Override
        public int hashCode(byte[] t) {
            return Arrays.hashCode(t);
        }

        @Override
        public boolean equals(byte[] a, byte[] b) {
            return Arrays.equals(a, b);
        }
    };

    @Override
    public ByteMapTest getTest() {
        return new FastUtilObjectIntGetTest();
    }

    @Override
    public ByteMapTest putTest() {
        return new FastUtilObjectIntPutTest();
    }

    @Override
    public ByteMapTest removeTest() {
        return new FastUtilObjectIntRemoveTest();
    }

    @Override
    public ByteMapTest mergeTest() {
        return new FastUtilObjectIntMergeTest();
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
                res = res ^ m_map.getInt(m_keys[i]);
            return res;
        }
    }

    private static class FastUtilObjectIntPutAndUpdateTest extends AbstractObjIntPutTest {
        private Object2IntOpenCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<byte[]>(keys.length, fillFactor, deviceIdHashing);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], j);
            }
            for (int i = 0; i < m_keys2.length; i++) {
                m_map.put(m_keys2[i], i);
            }
            return m_map.size();
        }

    }
    private static class FastUtilObjectIntPutTest extends AbstractObjIntPutTest {
        private Object2IntOpenCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<byte[]>(keys.length, fillFactor, deviceIdHashing);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], j);
            }
            return m_map.size();
        }
    }

    private static class FastUtilObjectIntRemoveTest extends AbstractObjIntGetTest {
        private Object2IntOpenCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<>(keys.length, fillFactor, deviceIdHashing);
            for (int j = 0; j < keys.length; j++) {
                m_map.put(keys[j], (int) keys[j][0]);
            }
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; i++) {
                m_map.removeInt(m_keys[i]);
            }
            return m_map.size();
        }
    }
    private static class FastUtilObjectIntTwoPutOneRemoveRemoveTest extends AbstractObjIntGetTest {
        private Object2IntOpenCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<>(keys.length, fillFactor, deviceIdHashing);
        }

        @Override
        public int test() {
            int add = 0, remove = 0;
            while (add < m_keys.length) {
                m_map.put(m_keys[add], add);
                ++add;
                m_map.put(m_keys[add], add);
                ++add;
                m_map.removeInt(m_keys[remove++]);
            }
            return m_map.size();
        }
    }

    private static class FastUtilObjectIntMergeTest extends AbstractObjIntMergeTest {

        Object2IntOpenCustomHashMap<byte[]> m_map;
        Object2IntOpenCustomHashMap<byte[]> m_map2;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Object2IntOpenCustomHashMap<byte[]>(keys.length / 2, fillFactor, deviceIdHashing);
            for (byte[] key : m_keys) m_map.put(key, key[0]);
            m_map2 = new Object2IntOpenCustomHashMap<byte[]>(keys.length, fillFactor, deviceIdHashing);
            for (byte[] key : m_keys2) m_map2.put(key, key[0]);

        }


        @Override
        public int test() {
            m_map2.object2IntEntrySet().fastForEach((item) -> {
                m_map.mergeInt(item.getKey(), item.getIntValue(), (givenValue, curValue) -> givenValue + curValue);
            });
            return m_map.size();
        }
    }
}
