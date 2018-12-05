package tests.intobjecttest;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class FastUtilIntObjTest implements IntTestSet {
    @Override
    public IntMapTest putAndUpdateTest() {
        return new FastUtilIntObjectPutAndUpdateTest();
    }

    @Override
    public IntMapTest twoPutOneRemoveTest() {
        return new FastUtilIntObjectTwoPutOneRemoveTest();
    }

    @Override
    public IntMapTest getTest() {
        return new FastUtilIntObjectGetTest();
    }

    @Override
    public IntMapTest putTest() {
        return new FastUtilIntObjectPutTest();
    }

    @Override
    public IntMapTest removeTest() {
        return new FastUtilIntObjectRemoveTest();
    }

    @Override
    public IntMapTest mergeTest() {
        return new FastUtilObjectIntMergeTest();
    }

    private static class FastUtilIntObjectGetTest extends AbstractIntObjGetTest {
        private Int2ObjectOpenHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new Int2ObjectOpenHashMap<>(keys.length, fillFactor);
            for (int i = 0; i < keys.length; ++i)
                if (i % oneFailOutOf != 0) m_map.put(keys[i], Integer.valueOf(keys[i]));
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res += 1;
            return res;
        }
    }

    private static class FastUtilIntObjectPutTest extends AbstractIntObjPutTest {
        Int2ObjectOpenHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Int2ObjectOpenHashMap<>(m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
            return m_map.size();
        }
    }

    private static class FastUtilIntObjectPutAndUpdateTest extends AbstractIntObjPutTest {
        Int2ObjectOpenHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Int2ObjectOpenHashMap<>(m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
            return m_map.size();
        }
    }

    private static class FastUtilIntObjectRemoveTest extends AbstractIntObjGetTest {
        private Int2ObjectOpenHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Int2ObjectOpenHashMap<>(m_keys.length );
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; i++) {
                m_map.remove(m_keys[i]);
            }
            return m_map.size();
        }
    }
    private static class FastUtilIntObjectTwoPutOneRemoveTest extends AbstractIntObjGetTest {
        private Int2ObjectOpenHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Int2ObjectOpenHashMap<>(m_keys.length / 2 + 1);
        }

        @Override
        public int test() {

            int add = 0, remove = 0;
            while (add < m_keys.length) {
                m_map.put(m_keys[add], new Integer(add));
                ++add;
                m_map.put(m_keys[add], new Integer(add));
                ++add;
                m_map.remove(m_keys[remove++]);
            }
            return m_map.size();
        }
    }

    private static class FastUtilObjectIntMergeTest extends AbstractIntObjMergeTest {

        private Int2ObjectOpenHashMap<Integer> m_map;
        private Int2ObjectOpenHashMap<Integer> m_map2;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Int2ObjectOpenHashMap<>(keys.length / 2, fillFactor);
            for (int key : m_keys) m_map.put(key, new Integer(key));
            m_map2 = new Int2ObjectOpenHashMap<>(keys.length, fillFactor);
            for (int key : m_keys2) m_map2.put(key, new Integer(key));

        }


        @Override
        public int test() {
            m_map2.int2ObjectEntrySet().fastForEach((item) -> {
                m_map.merge(item.getIntKey(), item.getValue(), (givenValue, curValue) -> givenValue + curValue);
            });
            return m_map.size();
        }
    }
}
