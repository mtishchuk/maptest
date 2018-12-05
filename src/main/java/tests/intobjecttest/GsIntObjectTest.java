package tests.intobjecttest;

import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

public class GsIntObjectTest implements IntTestSet {
    @Override
    public IntMapTest putAndUpdateTest() {
        return new GsIntObjectPutAndUpdateTest();
    }

    @Override
    public IntMapTest twoPutOneRemoveTest() {
        return new GsIntObjectTwoPutOneRemoveTest();
    }

    @Override
    public IntMapTest getTest() {
        return new GsIntObjectGetTest();
    }

    @Override
    public IntMapTest putTest() {
        return new GsIntObjectPutTest();
    }

    @Override
    public IntMapTest removeTest() {
        return new GsIntObjectRemoveTest();
    }

    @Override
    public IntMapTest mergeTest() {
        return new GsObjectIntMergeTest();
    }

    private static class GsIntObjectGetTest extends AbstractIntObjGetTest {
        private IntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf) {
            super.setup(keys, fillFactor, oneFailOutOf);
            m_map = new IntObjectHashMap<>(keys.length);
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

    private static class GsIntObjectPutTest extends AbstractIntObjPutTest {
        private IntObjectHashMap<Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new IntObjectHashMap<>(m_keys.length );
        }
        @Override
        public int test() {
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
            return m_map.size();
        }
    }
    private static class GsIntObjectPutAndUpdateTest extends AbstractIntObjPutTest {
        private IntObjectHashMap<Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new IntObjectHashMap<>(m_keys.length );
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

    private static class GsIntObjectRemoveTest extends AbstractIntObjPutTest {
        private IntObjectHashMap<Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map = new IntObjectHashMap<>(m_keys.length );
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

    private static class GsIntObjectTwoPutOneRemoveTest extends AbstractIntObjPutTest {
        private IntObjectHashMap<Integer> m_map;
        @Override
        public void setup(int[] keys, float fillFactor, int oneFailOutOf){
            super.setup(keys, fillFactor,oneFailOutOf);
            m_map  = new IntObjectHashMap<>(m_keys.length / 2 + 1);
            for (int i = 0; i < m_keys.length; ++i)
                m_map.put(m_keys[i], new Integer(m_keys[i]));
        }
        @Override
        public int test() {
            final IntObjectHashMap<Integer> m_map = new IntObjectHashMap<>(m_keys.length / 2 + 1);
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

    private static class GsObjectIntMergeTest extends AbstractIntObjMergeTest {

        IntObjectHashMap<Integer> m_map;
        IntObjectHashMap<Integer> m_map2;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new IntObjectHashMap<>(keys.length / 2);
            for (int key : m_keys) m_map.put(key, new Integer(key));
            m_map2 = new IntObjectHashMap<>(keys.length);
            for (int key : m_keys2) m_map2.put(key, new Integer(key));

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
