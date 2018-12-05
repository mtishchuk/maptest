package tests.intobjecttest;

import java.util.HashMap;

public class HashMapIntToObjTest implements IntTestSet {
    @Override
    public IntMapTest getTest() {
        return new HashMapIntToObjGetTest();
    }

    @Override
    public IntMapTest putTest() {
        return new HashMapIntToObjPutTest();
    }

    @Override
    public IntMapTest putAndUpdateTest() {
        return new HashMapIntToObjPutAndUpdateTest();
    }

    @Override
    public IntMapTest removeTest() {
        return new HashMapIntToObjRemoveTest();
    }

    @Override
    public IntMapTest twoPutOneRemoveTest() {
        return new HashMapIntToObjTwoPutOneRemoveTest();
    }

    @Override
    public IntMapTest mergeTest() {
        return new HashMapIntToObjMergeTest();
    }


    private static class HashMapIntToObjGetTest extends AbstractIntObjGetTest {
        private HashMap<Integer, Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new HashMap<>(keys.length);
            for (int i = 0; i < keys.length; ++i) if (i % oneFailOutOf != 0) m_map.put(keys[i], new Integer(keys[i]));
        }

        @Override
        public int test() {
            int res = 0;
            for (int i = 0; i < m_keys.length; ++i)
                if (m_map.get(m_keys[i]) != null) res += 1;
            return res;
        }
    }

    private static class HashMapIntToObjPutTest extends AbstractIntObjPutTest {
        private HashMap<Integer, Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
        }
        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
            return m_map.size();
        }
    }
    private static class HashMapIntToObjPutAndUpdateTest extends AbstractIntObjPutTest {
        private HashMap<Integer, Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
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

    private static class HashMapIntToObjRemoveTest extends AbstractIntObjPutTest {
        private HashMap<Integer, Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new HashMap<>(m_keys.length );
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

    private static class HashMapIntToObjTwoPutOneRemoveTest extends AbstractIntObjPutTest {
        private HashMap<Integer, Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map  = new HashMap<>(m_keys.length / 2 + 1);
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
        }
        @Override
        public int test() {
            final HashMap<Integer, Integer> m_map = new HashMap<>(m_keys.length / 2 + 1);
            final Integer value = 1;
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

    private static class HashMapIntToObjMergeTest extends AbstractIntObjMergeTest {

        HashMap<Integer, Integer> m_map;
        HashMap<Integer, Integer> m_map2;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new HashMap<>(keys.length / 2);
            for (int key : m_keys) m_map.put(key, new Integer(key));
            m_map2 = new HashMap<>(keys.length);
            for (int key : m_keys2) m_map2.put(key, new Integer(key));

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
