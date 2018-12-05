package tests.longobjecttest;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class FastUtilLongObjTest implements LongTestSet {
    @Override
    public LongMapTest putAndUpdateTest() {
        return new FastUtilLongObjectPutAndUpdateTest();
    }

    @Override
    public LongMapTest twoPutOneRemoveTest() {
        return new FastUtilLongObjectTwoPutOneRemoveRemoveTest();
    }

    @Override
    public LongMapTest getTest() {
        return new FastUtilLongObjectGetTest();
    }

    @Override
    public LongMapTest putTest() {
        return new FastUtilLongObjectPutTest();
    }

    @Override
    public LongMapTest removeTest() {
        return new FastUtilLongObjectRemoveTest();
    }

    @Override
    public LongMapTest mergeTest() {
        return new FastUtilObjectLongMergeTest();
    }

    private static class FastUtilLongObjectGetTest extends AbstractLongObjGetTest {
        private Long2ObjectOpenHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new Long2ObjectOpenHashMap<>(keys.length, fillFactor);
            for (int i = 0; i < keys.length; ++i)
                if (i % oneFailOutOf != 0) m_map.put(keys[i], Long.valueOf(keys[i]));
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res += 1;
            return res;
        }
    }

    private static class FastUtilLongObjectPutTest extends AbstractLongObjPutTest {
        Long2ObjectOpenHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Long2ObjectOpenHashMap<>(m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
            return m_map.size();
        }
    }

    private static class FastUtilLongObjectPutAndUpdateTest extends AbstractLongObjPutTest {
        Long2ObjectOpenHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Long2ObjectOpenHashMap<>(m_keys.length);
        }

        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
            return m_map.size();
        }
    }

    private static class FastUtilLongObjectRemoveTest extends AbstractLongObjGetTest {
        Long2ObjectOpenHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Long2ObjectOpenHashMap<>(m_keys.length);
            for (int i = 0; i < keys.length; i++) {
                m_map.put(keys[i], new Long(keys[i]));
            }
        }

        @Override
        public int test() {

            int add = 0, remove = 0;
            while (add < m_keys.length) {
                m_map.put(m_keys[add], new Long(add));
                ++add;
                m_map.put(m_keys[add], new Long(add));
                ++add;
                m_map.remove(m_keys[remove++]);
            }
            return m_map.size();
        }
    }

    private static class FastUtilLongObjectTwoPutOneRemoveRemoveTest extends AbstractLongObjGetTest {
        Long2ObjectOpenHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Long2ObjectOpenHashMap<>(m_keys.length / 2 + 1);
        }

        @Override
        public int test() {

            int add = 0, remove = 0;
            while (add < m_keys.length) {
                m_map.put(m_keys[add], new Long(add));
                ++add;
                m_map.put(m_keys[add], new Long(add));
                ++add;
                m_map.remove(m_keys[remove++]);
            }
            return m_map.size();
        }
    }

    private static class FastUtilObjectLongMergeTest extends AbstractLongObjMergeTest {

        Long2ObjectOpenHashMap<Long> m_map;
        Long2ObjectOpenHashMap<Long> m_map2;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new Long2ObjectOpenHashMap<>(keys.length / 2, fillFactor);
            for (long key : m_keys) m_map.put(key, new Long(key));
            m_map2 = new Long2ObjectOpenHashMap<>(keys.length, fillFactor);
            for (long key : m_keys2) m_map2.put(key, new Long(key));

        }


        @Override
        public int test() {
            m_map2.long2ObjectEntrySet().fastForEach((item) -> {
                m_map.merge(item.getLongKey(), item.getValue(), (givenValue, curValue) -> givenValue + curValue);
            });
            return m_map.size();
        }
    }
}
