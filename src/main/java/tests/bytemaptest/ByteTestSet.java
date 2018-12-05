package tests.bytemaptest;

public interface ByteTestSet {

    public ByteMapTest getTest();
    public ByteMapTest putTest();
    public ByteMapTest putAndUpdateTest();
    public ByteMapTest twoPutOneRemoveTest();
    public ByteMapTest removeTest();
    public ByteMapTest mergeTest();
}
