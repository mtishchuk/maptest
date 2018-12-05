#!/bin/bash

mvn clean install

java -cp target/benchmarks.jar tests.ByteMapTestRunner > 30_byte10kkAvgLog.txt

java -cp target/benchmarks.jar tests.IntMapTestRunner > 30_int10kkAvgLog.txt

java -cp target/benchmarks.jar tests.LongMapTestRunner > 30_long10kkAvgLog.txt


