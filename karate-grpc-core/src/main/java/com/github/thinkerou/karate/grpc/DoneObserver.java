package com.github.thinkerou.karate.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import io.grpc.stub.StreamObserver;

/**
 * DoneObserver
 *
 * A StreamObserver holding a future which completes when the rpc terminates.
 *
 * @author thinkerou
 */
public final class DoneObserver<T> implements StreamObserver<T> {

    private final SettableFuture<Void> doneFuture;

    DoneObserver() {
        this.doneFuture = SettableFuture.create();
    }

    @Override
    public synchronized void onCompleted() {
        doneFuture.set(null);
    }

    /**
     * @param t throwable
     */
    @Override
    public synchronized void onError(Throwable t) {
        doneFuture.setException(t);
    }

    /**
     * @param next next
     */
    @Override
    public void onNext(T next) {}

    /**
     * Returns a future which completes when the rpc finishes.
     * The returned future fails if the rpc fails.
     *
     * @return ListenableFuture
     */
    ListenableFuture<Void> getCompletionFuture() {
        return doneFuture;
    }

}
