package tests.longobjecttest;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

public class GsLongObjectTest implements LongTestSet {
    @Override
    public LongMapTest putAndUpdateTest() {
        return new GsLongObjectPutAndUpdateTest();
    }

    @Override
    public LongMapTest twoPutOneRemoveTest() {
        return new GsLongObjectTwoPutOneRemoveTest();
    }

    @Override
    public LongMapTest getTest() {
        return new GsLongObjectGetTest();
    }

    @Override
    public LongMapTest putTest() {
        return new GsLongObjectPutTest();
    }

    @Override
    public LongMapTest removeTest() {
        return new GsLongObjectRemoveTest();
    }

    @Override
    public LongMapTest mergeTest() {
        return new GsObjectLongMergeTest();
    }

    private static class GsLongObjectGetTest extends AbstractLongObjGetTest {
        private LongObjectHashMap<Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new LongObjectHashMap<>(keys.length);
            for (int i = 0; i < keys.length; ++i) if (i % oneFailOutOf != 0) m_map.put(keys[i], new Long(keys[i]));
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res += 1;
            return res;
        }
    }

    private static class GsLongObjectPutTest extends AbstractLongObjPutTest {
        private LongObjectHashMap<Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new LongObjectHashMap<>(m_keys.length );
        }
        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
            return m_map.size();
        }
    }
    private static class GsLongObjectPutAndUpdateTest extends AbstractLongObjPutTest {
        private LongObjectHashMap<Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new LongObjectHashMap<>(m_keys.length);
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

    private static class GsLongObjectRemoveTest extends AbstractLongObjPutTest {
        private LongObjectHashMap<Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new LongObjectHashMap<>(m_keys.length );
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
        }
        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; i++) {
                m_map.remove(m_keys[i]);
            }
            return m_map.size();
        }
    }
    private static class GsLongObjectTwoPutOneRemoveTest extends AbstractLongObjPutTest {
        private LongObjectHashMap<Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new LongObjectHashMap<>(m_keys.length / 2 + 1);
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

    private static class GsObjectLongMergeTest extends AbstractLongObjMergeTest {

        LongObjectHashMap<Long> m_map;
        LongObjectHashMap<Long> m_map2;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new LongObjectHashMap<>(keys.length / 2);
            for (long key : m_keys) m_map.put(key, new Long(key));
            m_map2 = new LongObjectHashMap<>(keys.length);
            for (long key : m_keys2) m_map2.put(key, new Long(key));

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
