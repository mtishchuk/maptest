package tests;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import tests.bytemaptest.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
Master plan:
1) add following test cases:
1.1) 2 added, 1 (previously inserted) removed - until reached the map size
 */

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ByteMapTestRunner {
    private static final int START_SIZE = Configuration.START_SIZE;
    private static final int TOTAL_SIZE = Configuration.TOTAL_SIZE;
    private final static int[] MAP_SIZES;

    static {
        MAP_SIZES = new int[(int) (Math.log10(TOTAL_SIZE) - Math.log10(START_SIZE) + 1)];
       int start = START_SIZE;
        int p = 0;
        int first = 500 * MAP_SIZES.length;
        while (start <= TOTAL_SIZE) {
            MAP_SIZES[p++] = start;
            start *= 10;
        }
    }

    private static final float FILL_FACTOR = 0.5f;

    private static final int ONE_FAIL_OUT_OF = 2;




    private static final Class[] TESTS_WRAPPER = {
            FastUtilObj2IntCustomTest.class,
            GsObjectIntCustomTest.class,
            TroveObjIntCustomTest.class,
            FastUtilObj2IntCustomWithNullGetTest.class,

    };


    public static void main(String[] args) throws RunnerException, InstantiationException, IllegalAccessException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Byte" + Configuration.FILE_NAME), StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {

            runTestSet("get", writer);
            runTestSet("put", writer);
            runTestSet("remove", writer);
            runTestSet("merge", writer);
            runTestSet("putAndUpdate", writer);
            runTestSet("twoPutOneRemove", writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String runTestSet(final String testSetName, BufferedWriter writer) throws RunnerException, InstantiationException, IllegalAccessException {
        System.out.println("Test " + testSetName + " for byte started." );
        final List<Class> tests = new ArrayList<>();
        tests.addAll(Arrays.asList(TESTS_WRAPPER));

        //first level: test class, second level - map size
        final Map<String, Map<Integer, String>> results = new HashMap<>();

        //pick map size first - we need to generate a set of keys to be used in all tests
        for (final int mapSize : MAP_SIZES) {
            //run tests one after another
            int warmSize = TOTAL_SIZE/mapSize ;
            int mesSize = TOTAL_SIZE/mapSize;

            for (final Class testClass : tests) {
                Options opt = new OptionsBuilder()
                        .include(".*" + ByteMapTestRunner.class.getSimpleName() + ".*")
                        .forks(1)
                        .mode(Configuration.MODE)
                        .timeUnit(TimeUnit.MILLISECONDS)
                        .warmupBatchSize(warmSize)
                        .warmupIterations(Configuration.WARMUP_ITERATIONS)
                        .warmupTime(Configuration.WARMUP_TIME)
                        .measurementBatchSize(mesSize)
                        .measurementTime(Configuration.MEASUREMENT_TIME)
                        .measurementIterations(Configuration.MEASUREMENT_ITERATIONS)
                        .jvmArgsAppend("-Xmx4G")
                        .param("m_mapSize", Integer.toString(mapSize))
                        .param("m_className", testClass.getCanonicalName())
                        .param("m_testType", testSetName)
//                        .shouldDoGC(true)
//                        .verbosity(VerboseMode.SILENT)
                        .build();

                Collection<RunResult> res = new Runner(opt).run();
                for (RunResult rr : res) {
                    results.compute(testClass.getCanonicalName(), (name, map) -> {
                        if (map == null) map = new HashMap<>(4);
                        map.put(mapSize, rr.getAggregatedResult().getPrimaryResult().toString());
                        return map;
                    });
                }
                if (res.isEmpty()) {
                    results.compute(testClass.getCanonicalName(), (name, map) -> {
                        if (map == null)
                            map = new HashMap<>(4);
                        map.put(mapSize, "-1");
                        return map;
                    });
                }
            }
        }
        final String res = formatResults(results, MAP_SIZES, tests);
        try {
            writer.write("Results for '" + testSetName + "': \n" + res + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    private static void testBillion(final List<Class> tests) throws IllegalAccessException, InstantiationException {
        final int mapSize = 1000 * 1000 * 1000;
        for (final Class klass : tests) {
            System.gc();
            final ByteMapTest obj = (ByteMapTest) klass.newInstance();
            System.out.println("Prior to setup for " + klass.getName());
            obj.setup(KeyGenerator.getKeys(mapSize), FILL_FACTOR, ONE_FAIL_OUT_OF);
            System.out.println("After setup for " + klass.getName());
            final long start = System.currentTimeMillis();
            obj.test();
            final long time = System.currentTimeMillis() - start;
            System.out.println(klass.getName() + " : time = " + (time / 1000.0) + " sec");
        }
    }

    private static String formatResults(final Map<String, Map<Integer, String>> results, final int[] mapSizes, final List<Class> tests) {
        final StringBuilder sb = new StringBuilder(2048);
        //format results
        //first line - map sizes, should be sorted
        for (final int size : mapSizes)
            sb.append(size).append(",");
        sb.setLength(sb.length()-1);
        sb.append("\n");
        //following lines - tests in the definition order
        for (final Class test : tests) {
            final Map<Integer, String> res = results.get(test.getCanonicalName());
            sb.append(test.getName());
            for (final int size : mapSizes)
                sb.append(":_").append(res.get(size));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static class KeyGenerator {
        public static int s_mapSize;
        public static byte[][] s_keys;

        public static byte[][] getKeys(final int mapSize) {
            if (mapSize == s_mapSize)
                return s_keys;
            s_mapSize = mapSize;
            byte[][] s_keys = null;
            s_keys = new byte[mapSize][];
            final Random r = new Random(1234);
            for (int i = 0; i < mapSize; ++i) {
                s_keys[i] = new byte[16];
                r.nextBytes(s_keys[i]);
            }
            return s_keys;
        }
    }


    @Param("1")
    public int m_mapSize;

    @Param("dummy")
    public String m_className;

    @Param({"get", "put", "remove", "merge", "putAndUpdate", "twoPutOneRemove"})
    public String m_testType;

    private ByteMapTest m_impl;

    @Setup
    public void setup() {
        try {
            final ByteTestSet byteTestSet = (ByteTestSet) Class.forName(m_className).newInstance();
            switch (m_testType) {
                case "get":
                    m_impl = byteTestSet.getTest();
                    break;
                case "put":
                    m_impl = byteTestSet.putTest();
                    break;
                case "remove":
                    m_impl = byteTestSet.removeTest();
                    break;
                case "merge":
                    m_impl = byteTestSet.mergeTest();
                    break;
                case "putAndUpdate":
                    m_impl = byteTestSet.putAndUpdateTest();
                    break;
                case "twoPutOneRemove":
                    m_impl = byteTestSet.twoPutOneRemoveTest();
                    break;
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //share the same keys for all tests with the same map size
        m_impl.setup(KeyGenerator.getKeys(m_mapSize), FILL_FACTOR, ONE_FAIL_OUT_OF);
    }

    @Benchmark
    public int testRandom() {
        return m_impl.test();
    }
}

