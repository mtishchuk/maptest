package tests.longobjecttest;

import tests.intobjecttest.AbstractIntObjGetTest;
import tests.intobjecttest.AbstractIntObjMergeTest;
import tests.intobjecttest.AbstractIntObjPutTest;
import tests.intobjecttest.IntMapTest;
import tests.intobjecttest.IntTestSet;

import java.util.HashMap;

public class HashMapLongToObjTest implements LongTestSet {


    @Override
    public LongMapTest getTest() {
        return new HashMapIntToObjGetTestLongToObjGetTest();
    }

    @Override
    public LongMapTest putTest() {
        return new HashMapLongToObjPutAndUpdateTest();
    }

    @Override
    public LongMapTest putAndUpdateTest() {
        return new HashMapLongToObjPutAndUpdateTest();
    }

    @Override
    public LongMapTest removeTest() {
        return new HashMapLongToObjRemoveTest();
    }

    @Override
    public LongMapTest twoPutOneRemoveTest() {
        return new HashMapLongToObjTwoPutOneRemoveTest();
    }

    @Override
    public LongMapTest mergeTest() {
        return new HashMapLongToObjMergeTest();
    }

    private static class HashMapIntToObjGetTestLongToObjGetTest extends AbstractLongObjGetTest {
        private HashMap<Long, Long> m_map;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new HashMap<>(keys.length);
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

    private static class HashMapLongToObjPutTest extends AbstractLongObjPutTest {
        private HashMap<Long, Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
        }
        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
            return m_map.size();
        }
    }
    private static class HashMapLongToObjPutAndUpdateTest extends AbstractLongObjPutTest {
        private HashMap<Long, Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
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

    private static class HashMapLongToObjRemoveTest extends AbstractLongObjPutTest {
        private HashMap<Long, Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
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

    private static class HashMapLongToObjTwoPutOneRemoveTest extends AbstractLongObjPutTest {
        private HashMap<Long, Long> m_map;
        @Override
        public void setup(long[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map  = new HashMap<>(m_keys.length / 2 + 1);
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Long(m_keys[i]));
        }
        @Override
        public int test() {
            final HashMap<Long, Long> m_map = new HashMap<>(m_keys.length / 2 + 1);
            final Integer value = 1;
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

    private static class HashMapLongToObjMergeTest extends AbstractLongObjMergeTest {

        HashMap<Long, Long> m_map;
        HashMap<Long, Long> m_map2;

        @Override
        public void setup(long[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new HashMap<>(keys.length / 2);
            for (long key : m_keys) m_map.put(key, new Long(key));
            m_map2 = new HashMap<>(keys.length);
            for (long key : m_keys2) m_map2.put(key, new Long(key));

        }


        @Override
        public int test() {
            m_map2.forEach((obj, i) -> {
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
