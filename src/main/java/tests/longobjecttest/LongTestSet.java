package tests.longobjecttest;

public interface LongTestSet {

    public LongMapTest getTest();
    public LongMapTest putTest();
    public LongMapTest putAndUpdateTest();
    public LongMapTest removeTest();
    public LongMapTest twoPutOneRemoveTest();
    public LongMapTest mergeTest();
}
