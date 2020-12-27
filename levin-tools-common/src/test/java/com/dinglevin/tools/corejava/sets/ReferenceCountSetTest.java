package com.dinglevin.tools.corejava.sets;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dinglevin.tools.corejava.sets.utils.MultiThreadTester;
import com.dinglevin.tools.corejava.sets.utils.MultiThreadTester.TestTask;

public class ReferenceCountSetTest {
    private static final long sleepBefore = 100;
    private static final int threadCount = 100;
    private static MultiThreadTester multiThreadTester;

    private ReferenceCountSet<String> refCountSet;

    @BeforeClass
    public static void beforeClass() {
        multiThreadTester = new MultiThreadTester(sleepBefore, threadCount);
    }

    @Before
    public void before() {
        refCountSet = new ReferenceCountSet<String>();
    }

    @Test
    public void testBasicAdd() {
        refCountSet.add("test1");
        refCountSet.add("test2");
        refCountSet.add("test1");
        refCountSet.add("test2");

        assertEquals(2, refCountSet.getRefCount("test1"));
        assertEquals(2, refCountSet.getRefCount("test2"));
    }

    @Test
    public void testBasicRemove() {
        refCountSet.add("test1");
        refCountSet.add("test2");
        refCountSet.add("test1");
        refCountSet.add("test2");

        refCountSet.remove("test1");
        refCountSet.remove("test2");
        refCountSet.remove("test1");

        assertEquals(1, refCountSet.getRefCount("test2"));
        assertFalse(refCountSet.contains("test1"));
    }

    @Test
    public void testMultipleThreadAdd() throws Exception {
        multiThreadTester.execute(new TestTask() {
            public void run(int threadIndex) {
                if (threadIndex % 2 == 0) {
                    refCountSet.add("test1");
                } else {
                    refCountSet.add("test2");
                }
            }
        });

        assertEquals(threadCount / 2, refCountSet.getRefCount("test1"));
        assertEquals(threadCount / 2, refCountSet.getRefCount("test2"));
    }

    @Test
    public void testMultipleThreadRemove() throws Exception {
        multiThreadTester.execute(new TestTask() {
            public void run(int threadIndex) {
                if (threadIndex % 2 == 0) {
                    refCountSet.add("test1");
                } else {
                    refCountSet.add("test2");
                }
            }
        });

        multiThreadTester.execute(new TestTask() {
            public void run(int threadIndex) {
                if (threadIndex % 2 == 0) {
                    refCountSet.remove("test1");
                } else {
                    if (threadIndex != 3) {
                        refCountSet.remove("test2");
                    }
                }
            }
        });

        assertEquals(1, refCountSet.getRefCount("test2"));
        assertFalse(refCountSet.contains("test1"));
    }
}
