package com.github.thinkerou.karate.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * ChannelFactory
 *
 * @author thinkerou
 */
public final class ChannelFactory {

    /**
     * @param host grpc server host
     * @param port grpc server port
     * @return managed channel
     */
    public static ManagedChannel create(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

}
