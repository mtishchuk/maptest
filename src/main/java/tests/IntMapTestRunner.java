package tests;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import tests.intobjecttest.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
Master plan:
1) add following test cases:
1.1) 2 added, 1 (previously inserted) removed - until reached the map size
 */

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class IntMapTestRunner {
    private static final int START_SIZE = Configuration.START_SIZE;
    private static final int TOTAL_SIZE = Configuration.TOTAL_SIZE;
    private final static int[] MAP_SIZES;
    private static final float FILL_FACTOR = 0.5f;
    //share of unsuccessful get operations (approx)
    //increase this value significantly (Integer.MAX_VALUE is a good candidate) to revert to the original "always successful get" tests
    private static final int ONE_FAIL_OUT_OF = 2;
    private static final Class[] TESTS_WRAPPER = {
            FastUtilIntObjTest.class,
            GsIntObjectTest.class,
            TroveIntObjTest.class,
            HashMapIntToObjTest.class

    };

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

    @Param("1")
    public int m_mapSize;
    @Param("dummy")
    public String m_className;
    @Param({"get", "put", "remove", "merge", "putAndUpdate", "twoPutOneRemove"})
    public String m_testType;
    private IntMapTest m_impl;

    public static void main(String[] args) throws RunnerException, InstantiationException, IllegalAccessException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Int"  + Configuration.FILE_NAME), StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            runTestSet("get", writer);
            runTestSet("put", writer);
            runTestSet("remove", writer);
            runTestSet("merge", writer);
            runTestSet("twoPutOneRemove", writer);
            runTestSet("putAndUpdate", writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String runTestSet(final String testSetName, BufferedWriter writer) throws RunnerException, InstantiationException, IllegalAccessException {
        System.out.println("Test " + testSetName + " for int started." );
        final List<Class> tests = new ArrayList<>();
        tests.addAll(Arrays.asList(TESTS_WRAPPER));

        final Map<String, Map<Integer, String>> results = new HashMap<>();

        for (final int mapSize : MAP_SIZES) {
            //run tests one after another
            int warmSize = TOTAL_SIZE / mapSize;
            int mesSize = TOTAL_SIZE / mapSize;

            for (final Class testClass : tests) {
                Options opt = new OptionsBuilder()
                        .include(".*" + IntMapTestRunner.class.getSimpleName() + ".*")
                        .forks(1)
                        .mode(Configuration.MODE)
                        .timeUnit(TimeUnit.MILLISECONDS)
                        .warmupBatchSize(warmSize)
                        .warmupIterations(Configuration.WARMUP_ITERATIONS)
                        .warmupTime(Configuration.WARMUP_TIME)
                        .measurementBatchSize(mesSize)
                        .measurementTime(Configuration.MEASUREMENT_TIME)
                        .measurementIterations(Configuration.MEASUREMENT_ITERATIONS)
                        .jvmArgsAppend("-Xms4g")
                        .jvmArgsAppend("-Xmx4g")
                        .param("m_mapSize",Integer.toString(mapSize))
                        .param("m_className", testClass.getCanonicalName())
                        .param("m_testType", testSetName)
//                        .shouldDoGC(true)
//                        .verbosity(VerboseMode.SILENT)
                        .build();

                Collection<RunResult> res = new Runner(opt).run();
                for (RunResult rr : res) {
                    System.out.println("\n" + rr.getPrimaryResult().toString() + "\n");
                    System.out.println(testClass.getCanonicalName() + " (" + mapSize + ") "
                            + rr.getAggregatedResult().getPrimaryResult().getLabel() + " = "
                            + rr.getAggregatedResult().getPrimaryResult().getScore()
                            + " "
                            + rr.getAggregatedResult().getPrimaryResult().getScoreUnit() + "\n\n");
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


    private static String formatResults(final Map<String, Map<Integer, String>> results, final int[] mapSizes, final List<Class> tests) {
        final StringBuilder sb = new StringBuilder(2048);
        //format results
        //first line - map sizes, should be sorted
        for (final int size : mapSizes)
            sb.append(size).append(",");
        sb.setLength(sb.length() - 1);
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

    @Setup
    public void setup() {
        try {
            final IntTestSet intTestSet = (IntTestSet) Class.forName(m_className).newInstance();
            switch (m_testType) {
                case "get":
                    m_impl = intTestSet.getTest();
                    break;
                case "put":
                    m_impl = intTestSet.putTest();
                    break;
                case "remove":
                    m_impl = intTestSet.removeTest();
                    break;
                case "merge":
                    m_impl = intTestSet.mergeTest();
                    break;
                case "twoPutOneRemove":
                    m_impl = intTestSet.twoPutOneRemoveTest();
                    break;
                case "putAndUpdate":
                    m_impl = intTestSet.putAndUpdateTest();
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

    public static class KeyGenerator {
        public static int s_mapSize;
        public static int[] s_keys;

        public static int[] getKeys(final int mapSize) {
            if (mapSize == s_mapSize)
                return s_keys;
            s_mapSize = mapSize;
            s_keys = null; //should be done separately so we don't keep 2 arrays in memory
            s_keys = new int[mapSize];
            final Random r = new Random(1234);
            for (int i = 0; i < mapSize; ++i) {
                s_keys[i] = r.nextInt();
            }
            return s_keys;
        }
    }
}

