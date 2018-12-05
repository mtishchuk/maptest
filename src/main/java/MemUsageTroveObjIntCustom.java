import gnu.trove.map.custom_hash.TObjectIntCustomHashMap;
import tests.ByteMapTestRunner;
import tests.bytemaptest.TroveObjIntCustomTest;

import java.util.Random;

public class MemUsageTroveObjIntCustom {

    public static void main(String[] args) {

        int numb = 10000000;
        TObjectIntCustomHashMap<byte[]> m_map = new TObjectIntCustomHashMap<>(TroveObjIntCustomTest.deviceIdHashing);
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
        System.out.println("Remove");
        System.gc();
        for (int i = 0; i < numb; i++) {
            m_map.remove(keys[i]);
        }
        System.out.println("Remove");
        System.gc();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
    }
}
