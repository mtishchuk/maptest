package tests.longobjecttest;

import gnu.trove.map.hash.TLongObjectHashMap;

public class TroveLongObjTest implements LongTestSet {
    @Override
    public LongMapTest putAndUpdateTest() {
        return new TroveLongObjPutAndUpdateTest();
    }

    @Override
    public LongMapTest twoPutOneRemoveTest() {
        return new TroveLongObjTwoPutOneRemoveTest();
    }

    @Override
    public LongMapTest getTest() {
        return new TroveLongObjGetTest();
    }

    @Override
    public LongMapTest putTest() {
        return new TroveLongObjPutTest();
    }

    @Override
    public LongMapTest removeTest() {
        return new TroveLongObjRemoveTest();
    }

    @Override
    public LongMapTest mergeTest() {
        return new TroveLongObjMergeTest();
    }

    private static class TroveLongObjGetTest extends AbstractLongObjGetTest {

        private TLongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<>(keys.length, fillFactor);
            for (int i = 0; i < keys.length; ++i) {
                if (i % oneFailureOutOf != 0) m_map.put(keys[i], new Long(keys[i]));
            }
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res += 1;
            return res;
        }

    }

    private static class TroveLongObjPutTest extends AbstractLongObjPutTest {
        private TLongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<Long>(keys.length, fillFactor);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], new Long(j));
            }
            return m_map.size();
        }

    }
    private static class TroveLongObjPutAndUpdateTest extends AbstractLongObjPutTest {
        private TLongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<Long>(keys.length, fillFactor);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], new Long(j));
            }
            for (int i = 0; i < m_keys2.length; i++) {
                m_map.put(m_keys2[i], new Long(i));
            }
            return m_map.size();
        }

    }

    private static class TroveLongObjRemoveTest extends AbstractLongObjGetTest {
        private TLongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<>(keys.length, fillFactor);
            for (int j = 0; j < keys.length; j++) {
                m_map.put(keys[j], new Long(keys[j]));
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
    private static class TroveLongObjTwoPutOneRemoveTest extends AbstractLongObjGetTest {
        private TLongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<>(keys.length, fillFactor);
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

    private static class TroveLongObjMergeTest extends AbstractLongObjMergeTest {
        private TLongObjectHashMap<Long> m_map;
        private TLongObjectHashMap<Long> m_map2;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TLongObjectHashMap<Long>(keys.length / 2, fillFactor);
            for (long key : m_keys) m_map.put(key, new Long(key));
            m_map2 = new TLongObjectHashMap<Long>(keys.length, fillFactor);
            for (long key : m_keys2) m_map2.put(key, new Long(key));
        }

        @Override
        public int test() {
            m_map2.forEachEntry((key, val) -> {
                if (m_map.containsKey(key)) {
                    m_map.put(key, m_map.get(key) + val);
                } else m_map.put(key, val);

                return true;
            });


            return m_map.size();
        }
    }
}
