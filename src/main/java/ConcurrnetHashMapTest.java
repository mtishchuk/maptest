import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.cayenne.util.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrnetHashMapTest {

    public static class MyClass {

        final int ip;
        final short area;
        final int operator;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyClass myClass = (MyClass) o;

            if (ip != myClass.ip) return false;
            if (area != myClass.area) return false;
            return operator == myClass.operator;
        }

        @Override
        public int hashCode() {
            int result = ip;
            result = 31 * result + (int) area;
            result = 31 * result + operator;
            return result;
        }

        public MyClass(int ip, short area, int operator) {
            this.ip = ip;
            this.area = area;
            this.operator = operator;
        }

    }
    public static class MutableInt{
        private int value;

        public MutableInt(int value) {
            this.value = value;
        }
        public void increment(){++value;}

        public int getValue() {
            return value;
        }
    }



    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(6000);

        ConcurrentLinkedHashMap<MyClass, AtomicInteger> mapToTest = new ConcurrentLinkedHashMap.Builder<MyClass, AtomicInteger>().maximumWeightedCapacity(1400000).build();
        for (int i = 0; i < 7200000; i++) {
            final MyClass key = new MyClass(i % 400000, (short) (i % 200), i % 300);
            mapToTest.compute(key, (k,v)->{
                if (v==null) v = new AtomicInteger(1);
                else v.incrementAndGet();
                return v;
            });
        }
        System.out.println(mapToTest.size());
        System.gc();
        Thread.sleep(6000);
    }
}
