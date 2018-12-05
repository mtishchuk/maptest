package tests.intobjecttest;

import gnu.trove.map.hash.TIntObjectHashMap;

public class TroveIntObjTest implements IntTestSet {
    @Override
    public IntMapTest putAndUpdateTest() {
        return new TroveIntObjPutAndUpdateTest();
    }

    @Override
    public IntMapTest twoPutOneRemoveTest() {
        return new TroveIntObjTwoPutOneRemoveTest();
    }

    @Override
    public IntMapTest getTest() {
        return new TroveIntObjGetTest();
    }

    @Override
    public IntMapTest putTest() {
        return new TroveIntObjPutTest();
    }

    @Override
    public IntMapTest removeTest() {
        return new TroveIntObjRemoveTest();
    }

    @Override
    public IntMapTest mergeTest() {
        return new TroveIntObjMergeTest();
    }

    private static class TroveIntObjGetTest extends AbstractIntObjGetTest {

        private TIntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<>(keys.length, fillFactor);
            for (int i = 0; i < keys.length; ++i) {
                if (i % oneFailureOutOf != 0) m_map.put(keys[i], new Integer(keys[i]));
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

    private static class TroveIntObjPutTest extends AbstractIntObjPutTest {
        private TIntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<Integer>(keys.length, fillFactor);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], new Integer(j));
            }
            return m_map.size();
        }

    }

    private static class TroveIntObjPutAndUpdateTest extends AbstractIntObjPutTest {
        private TIntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<Integer>(keys.length, fillFactor);
        }

        @Override
        public int test() {
            for (int j = 0; j < m_keys.length; j++) {
                m_map.put(m_keys[j], new Integer(j));
            }
            for (int i = 0; i < m_keys2.length; i++) {
                m_map.put(m_keys2[i], new Integer(i));
            }
            return m_map.size();
        }

    }

    private static class TroveIntObjRemoveTest extends AbstractIntObjGetTest {
        private TIntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<>(keys.length, fillFactor);
            for (int j = 0; j < keys.length; j++) {
                m_map.put(keys[j], new Integer(keys[j]));
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
    private static class TroveIntObjTwoPutOneRemoveTest extends AbstractIntObjGetTest {
        private TIntObjectHashMap<Integer> m_map;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<>(keys.length, fillFactor);
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

    private static class TroveIntObjMergeTest extends AbstractIntObjMergeTest {
        private TIntObjectHashMap<Integer> m_map;
        private TIntObjectHashMap<Integer> m_map2;

        @Override
        public void setup(int[] keys, float fillFactor, int oneFailureOutOf) {
            super.setup(keys, fillFactor, oneFailureOutOf);
            m_map = new TIntObjectHashMap<Integer>(keys.length / 2, fillFactor);
            for (int key : m_keys) m_map.put(key, new Integer(key));
            m_map2 = new TIntObjectHashMap<Integer>(keys.length, fillFactor);
            for (int key : m_keys2) m_map2.put(key, new Integer(key));
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
