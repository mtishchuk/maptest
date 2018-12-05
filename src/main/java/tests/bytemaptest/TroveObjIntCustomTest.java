package tests.bytemaptest;

import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import gnu.trove.strategy.HashingStrategy;

import java.util.Arrays;

public class TroveObjIntCustomTest implements ByteTestSet {
    @Override
    public ByteMapTest putAndUpdateTest() {
        return new TroveObjIntPutAndUpdateTest();
    }

    @Override
    public ByteMapTest twoPutOneRemoveTest() {
        return new TroveObjIntTwoPutOneRemoveTest();
    }

    public static final HashingStrategy<byte[]> deviceIdHashing = new HashingStrategy<byte[]>() {
        @Override
        public int computeHashCode(byte[] bytes) {
            return Arrays.hashCode(bytes);
        }

        @Override
        public boolean equals(byte[] a, byte[] b) {
            return Arrays.equals(a, b);
        }
    };

    @Override
    public ByteMapTest getTest() {
        return new TroveObjIntGetTest();
    }

    @Override
    public ByteMapTest putTest() {
        return new TroveObjIntPutTest();
    }

    @Override
    public ByteMapTest removeTest() {
        return new TroveObjIntRemoveTest();
    }

    @Override
    public ByteMapTest mergeTest() {
        return new TroveObjIntMergeTest();
    }

    private static class TroveObjIntGetTest extends AbstractObjIntGetTest {

        private TObjectIntCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<>(deviceIdHashing, keys.length, fillFactor);
            for (int i = 0; i < keys.length; ++i) {
                if (i % oneFailureOutOf != 0) m_map.put(keys[i], (int) keys[i][0]);
            }
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                res = res ^ m_map.get(m_keys[i]);
            return res;
        }

    }

    private static class TroveObjIntPutTest extends AbstractObjIntPutTest {
        private TObjectIntCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<byte[]>(deviceIdHashing, keys.length, fillFactor);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], j);
            }
            return m_map.size();
        }

    }

    private static class TroveObjIntPutAndUpdateTest extends AbstractObjIntPutTest {
        private TObjectIntCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<byte[]>(deviceIdHashing, keys.length, fillFactor);
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

    private static class TroveObjIntRemoveTest extends AbstractObjIntGetTest {
        private TObjectIntCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<>(deviceIdHashing, keys.length, fillFactor);
            for (int j = 0; j < keys.length; j++) {
                m_map.put(keys[j], (int) keys[j][0]);
            }
        }

        @Override
        public int test() {

            for (int i = 0; i < m_keys.length; i++) {
                m_map.remove(m_keys[i]);
            }
            return m_map.size();
        }

    }

    private static class TroveObjIntTwoPutOneRemoveTest extends AbstractObjIntGetTest {
        private TObjectIntCustomHashMap<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<>(deviceIdHashing, keys.length, fillFactor);
        }

        @Override
        public int test() {

            int add = 0, remove = 0;
            while (add < m_keys.length) {
                m_map.put(m_keys[add], add);
                ++add;
                m_map.put(m_keys[add], add);
                ++add;
                m_map.remove(m_keys[remove++]);
            }
            return m_map.size();
        }

    }

    private static class TroveObjIntMergeTest extends AbstractObjIntMergeTest {
        private TObjectIntCustomHashMap<byte[]> m_map;
        private TObjectIntCustomHashMap<byte[]> m_map2;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TObjectIntCustomHashMap<byte[]>(deviceIdHashing, keys.length / 2, fillFactor);
            for (byte[] key : m_keys) m_map.put(key, key[0]);
            m_map2 = new TObjectIntCustomHashMap<byte[]>(deviceIdHashing, keys.length, fillFactor);
            for (byte[] key : m_keys2) m_map2.put(key, key[0]);
        }

        @Override
        public int test() {

            m_map2.forEachEntry((key, val) -> {
                if (m_map.containsKey(key)) {
                    m_map.put(key, m_map.get(key) + val);
                }else
                    m_map.put(key, val);
                return true;
            });


            return m_map.size();
        }
    }
}
