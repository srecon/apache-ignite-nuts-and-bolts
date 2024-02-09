package com.blu.imdg;

import com.blu.dto.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;

/**
 * Use the following JVM options to run on JVM 17 or later
 * java --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -jar ./target/IsolatedCLient-runnable.jar
 * */
public class IsolatedCluster {
    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();

// Explicitly configure TCP discovery SPI to provide list of initial nodes
// from the first cluster.
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();

// Initial local port to listen to.
        discoverySpi.setLocalPort(48500);

// Changing local port range. This is an optional action.
        discoverySpi.setLocalPortRange(20);

        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

// Addresses and port range of the nodes from the first cluster.
// 127.0.0.1 can be replaced with actual IP addresses or host names.
// The port range is optional.
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:48500..48520"));

// Overriding IP finder.
        discoverySpi.setIpFinder(ipFinder);

// Explicitly configure TCP communication SPI by changing local port number for
// the nodes from the first cluster.
        TcpCommunicationSpi commSpi = new TcpCommunicationSpi();

        commSpi.setLocalPort(48100);

// Overriding discovery SPI.
        cfg.setDiscoverySpi(discoverySpi);

// Overriding communication SPI.
        cfg.setCommunicationSpi(commSpi);
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

// Starting a node.
        Ignite ignite = Ignition.start(cfg);

        // get or create cache
        IgniteCache<String, String> cache = ignite.getOrCreateCache("myCacheSource");
        // put some cache elements
        for(int i = 100; i <= 150; i++){
            cache.put("Key:"+i, "Hello World!!: "+Integer.toString(i));
        }
        // get them from the cache and write to the console
//        for(int i =1; i<= 100; i++){
//            System.out.println("Cache get:"+ cache.get(i));
//        }
        ignite.close();

    }
}
