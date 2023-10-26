package dev.alexengrig._7_volatile;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

public class VolatileMain_JCStressTests {

    @JCStressTest
    @Outcome(id = {"0, 1", "1, 0", "1, 1"}, expect = Expect.ACCEPTABLE, desc = "Trivial under sequential consistency")
    @Outcome(id = "0, 0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Violates sequential consistency")
    @State
    public static class Plain {
        int x;
        int y;

        @Actor
        public void actor1(II_Result r) {
            x = 1;
            r.r1 = y;
        }

        @Actor
        public void actor2(II_Result r) {
            y = 1;
            r.r2 = x;
        }
    }

    @JCStressTest
    @Outcome(id = {"0, 1", "1, 0", "1, 1"}, expect = Expect.ACCEPTABLE, desc = "Trivial under sequential consistency")
    @Outcome(id = "0, 0", expect = Expect.FORBIDDEN, desc = "Violates sequential consistency")
    @State
    public static class Volatile {
        volatile int x;
        volatile int y;

        @Actor
        public void actor1(II_Result r) {
            x = 1;
            r.r1 = y;
        }

        @Actor
        public void actor2(II_Result r) {
            y = 1;
            r.r2 = x;
        }
    }

}
