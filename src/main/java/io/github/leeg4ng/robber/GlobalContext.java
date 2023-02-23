package io.github.leeg4ng.robber;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GlobalContext {

    public static Map<String, Integer> championIdMap;

    public static List<List<Integer>> aramTiers;

    public static Lock connectionLock = new ReentrantLock();

    public static Semaphore connectionSemaphore = new Semaphore(1);
}
