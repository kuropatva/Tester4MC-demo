package pl.kuropatva.util;

import it.unimi.dsi.fastutil.ints.IntArraySet;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.stream.IntStream;

// Warning: you can "free" port that wasn't in pool
public class IPGenerator {

    private static final HashMap<String, IPPool> globalIPPool = new HashMap<>();

    public static void create(String poolName, String host, int minPort, int maxPort) {
        globalIPPool.compute(poolName, (_, ipPool) -> {
            if (ipPool == null) {
                 ipPool = new IPPool(host, new IntArraySet());
            }
            IntStream.rangeClosed(minPort, maxPort).forEach(ipPool.portPool::add);
            return ipPool;
        });
    }

    public static void delete(String poolName) {
        globalIPPool.remove(poolName);
    }

    public synchronized static InetSocketAddress acquire(String poolName) {
        IPPool ipPool = globalIPPool.get(poolName);
        if (ipPool == null || ipPool.portPool.isEmpty()) {
            return null;
        }

        int port = ipPool.portPool.iterator().nextInt();
        ipPool.portPool.remove(port);

        return new InetSocketAddress(ipPool.hostname, port);
    }


    public synchronized static void free(String poolName, InetSocketAddress address) {
        var ipPool = globalIPPool.get(poolName);
        if (ipPool != null) ipPool.portPool.add(address.getPort());
    }

    public synchronized static void free(String poolName, InstanceConfig instanceConfig) {
        free(poolName, instanceConfig.ip());
    }

    private record IPPool(String hostname, IntArraySet portPool) {

    }
}
