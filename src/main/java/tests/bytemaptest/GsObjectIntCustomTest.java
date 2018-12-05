package tests.bytemaptest;

import org.eclipse.collections.api.block.HashingStrategy;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMapWithHashingStrategy;

import java.util.Arrays;

public class GsObjectIntCustomTest implements ByteTestSet {
    @Override
    public ByteMapTest putAndUpdateTest() {
        return new GsObjectIntPutAndUpdateTest();
    }

    @Override
    public ByteMapTest twoPutOneRemoveTest() {
        return new GsObjectIntTwoPutOneRemoveTest();
    }

    public static final HashingStrategy<byte[]> deviceIdHashing = new HashingStrategy<byte[]>() {

        @Override
        public int computeHashCode(byte[] bytes) {
            return Arrays.hashCode(bytes);
        }

        @Override
        public boolean equals(byte[] bytes, byte[] e1) {
            return Arrays.equals(bytes, e1);
        }

    };

    @Override
    public ByteMapTest getTest() {
        return new GsObjectIntGetTest();
    }

    @Override
    public ByteMapTest putTest() {
        return new GsObjectIntPutTest();
    }

    @Override
    public ByteMapTest removeTest() {
        return new GsObjectIntRemoveTest();
    }

    @Override
    public ByteMapTest mergeTest() {
        return new GsObjectIntMergeTest();
    }

    private static class GsObjectIntGetTest extends AbstractObjIntGetTest {
        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, keys.length);
            for (int i = 0; i < keys.length; i++) {
                if (i % oneFailureOutOf != 0)
                    m_map.put(keys[i], (int) keys[i][0]);
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

    private static class GsObjectIntPutTest extends AbstractObjIntPutTest {

        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], i);
            return m_map.size();
        }

    }
    private static class GsObjectIntPutAndUpdateTest extends AbstractObjIntPutTest {

        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], i);
            for (int i = 0; i < m_keys2.length; ++i)
                m_map.put(m_keys2[i], i);
            return m_map.size();
        }

    }

    private static class GsObjectIntRemoveTest extends AbstractObjIntGetTest {

        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, keys.length);
            for (int j = 0; j < keys.length; j++) {
                if (j % oneFailureOutOf != 0) m_map.put(keys[j], (int) keys[j][0]);
            }
        }

        @Override
        public int test() {
           for (int i = 0; i < m_keys.length; ++i) {
                m_map.remove(m_keys[i]);
            }
            return m_map.size();
        }
    }
    private static class GsObjectIntTwoPutOneRemoveTest extends AbstractObjIntGetTest {

        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, keys.length);
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

    private static class GsObjectIntMergeTest extends AbstractObjIntMergeTest {

        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map;
        private ObjectIntHashMapWithHashingStrategy<byte[]> m_map2;

        @Override
        public void setup(byte[][] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, keys.length / 2);
            for (byte[] key : m_keys) m_map.put(key, key[0]);
            m_map2 = new ObjectIntHashMapWithHashingStrategy<>(deviceIdHashing, keys.length);
            for (byte[] key : m_keys2) m_map2.put(key, key[0]);

        }


        @Override
        public int test() {
            m_map2.forEachKeyValue((obj, i) -> {
                if (m_map.containsKey(obj)) {
                    m_map.put(obj, m_map.get(obj) + i);
                } else {
                    m_map.put(obj, i);
                }
            });
            return m_map.size();
        }
    }

}
