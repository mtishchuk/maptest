package tests;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.options.TimeValue;

public class Configuration {

    public static final String FILE_NAME= "_MapSingle10kk.txt";
    public static final Mode MODE = Mode.AverageTime;
    public static final int WARMUP_ITERATIONS = 6;
    public static final int MEASUREMENT_ITERATIONS = 20;
    public static final int START_SIZE = 1_000;
    public static final int TOTAL_SIZE = 10_000_000;
    public static final TimeValue WARMUP_TIME = TimeValue.seconds(10);
    public static final TimeValue MEASUREMENT_TIME = TimeValue.seconds(25);
}
