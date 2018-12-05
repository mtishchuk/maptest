import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMapWithHashingStrategy;
import tests.ByteMapTestRunner;
import tests.bytemaptest.GsObjectIntCustomTest;
import tests.bytemaptest.TroveObjIntCustomTest;

import java.util.Random;

public class MemUsageGsObjIntCustomC {

    public static void main(String[] args) {

        int numb = 10000000;
        ObjectIntHashMapWithHashingStrategy<byte[]> m_map = new ObjectIntHashMapWithHashingStrategy<>(GsObjectIntCustomTest.deviceIdHashing, numb);

        byte[][] keys = new byte[numb][];
        final Random r = new Random(1234);
        for (int i = 0; i < numb; ++i) {
            keys[i] = new byte[16];
            r.nextBytes(keys[i]);
        }

        for (int i = 0; i < numb; i++) {
            m_map.put(keys[i], i);
        }
        System.gc();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
        for (int i = 0; i < numb; i += 2) {
            m_map.remove(keys[i]);
        }
        System.gc();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
    }
}
