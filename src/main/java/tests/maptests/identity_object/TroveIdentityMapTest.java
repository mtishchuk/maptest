package tests.maptests.identity_object;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.IdentityHashingStrategy;
import tests.maptests.IMapTest;
import tests.maptests.ITestSet;
import tests.maptests.object_prim.AbstractObjKeyGetTest;

import java.util.Map;

/**
 * Trove IdentityHashMap version
 */
public class TroveIdentityMapTest implements ITestSet
{
    @Override
    public IMapTest getTest() {
        return new TroveIdentityMapGetTest();
    }

    @Override
    public IMapTest putTest() {
        return new TroveIdentityObjMapPutTest();
    }

    @Override
    public IMapTest removeTest() {
        return new TroveIdentityObjMapRemoveTest();
    }

    private static class TroveIdentityMapGetTest extends AbstractObjKeyGetTest {
        private Map<Integer, Integer> m_map;

        @Override
        public void setup(final int[] keys, final float fillFactor, final int oneFailureOutOf ) {
            super.setup( keys, fillFactor, oneFailureOutOf );
            m_map = new TCustomHashMap<>( IdentityHashingStrategy.INSTANCE, keys.length, fillFactor );
            for (Integer key : m_keys)
                m_map.put(key % oneFailureOutOf == 0 ? key + 1 : key, key);
        }

        @Override
        public int test() {
            int res = 0;
            for ( int i = 0; i < m_keys.length; ++i )
                if ( m_map.get( m_keys[ i ] ) != null ) res ^= 1;
            return res;
        }
    }

    private static class TroveIdentityObjMapPutTest extends AbstractObjKeyPutIdentityTest {
        @Override
        public int test() {
            final Map<Integer, Integer> m_map = new TCustomHashMap<>( IdentityHashingStrategy.INSTANCE, m_keys.length, m_fillFactor );
            for ( int i = 0; i < m_keys.length; ++i )
               m_map.put( m_keys[ i ], m_keys[ i ] );
            for ( int i = 0; i < m_keys.length; ++i ) //same key set is used for identity tests
               m_map.put( m_keys[ i ], m_keys[ i ] );
            return m_map.size();
        }
    }

    private static class TroveIdentityObjMapRemoveTest extends AbstractObjKeyPutIdentityTest {
        @Override
        public int test() {
            final Map<Integer, Integer> m_map = new TCustomHashMap<>( IdentityHashingStrategy.INSTANCE, m_keys.length / 2 + 1, m_fillFactor );
            int add = 0, remove = 0;
            while ( add < m_keys.length )
            {
                m_map.put( m_keys[ add ], m_keys[ add ] );
                ++add;
                m_map.put( m_keys[ add ], m_keys[ add ] );
                ++add;
                m_map.remove( m_keys[ remove++ ] );
            }
            return m_map.size();
        }
    }

}

