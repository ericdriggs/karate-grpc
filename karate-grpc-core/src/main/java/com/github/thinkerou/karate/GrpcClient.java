package com.github.thinkerou.karate;

import com.github.thinkerou.karate.service.GrpcCall;
import com.github.thinkerou.karate.service.GrpcList;
import com.github.thinkerou.karate.utils.RedisHelper;
import com.intuit.karate.core.ScenarioBridge;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GrpcClient
 *
 * @author thinkerou
 */
public final class GrpcClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    private GrpcCall callIns;
    private GrpcList listIns;
    private RedisHelper redisHelper;

    GrpcClient(String host, int port) {
        this.callIns = GrpcCall.create(host, port);
    }

    GrpcClient() {
        this.listIns = GrpcList.create();
    }

    public static GrpcClient create() {
        return new GrpcClient();
    }

    public static GrpcClient create(String host, int port) {
        return new GrpcClient(host, port);
    }

    public GrpcClient redis() {
        if (redisHelper == null) {
            redisHelper = new RedisHelper();
        }
        return this;
    }

    public String call(String name, String payload) {
        return call( name, payload, null);
    }

    public String call(String name, String payload, ScenarioBridge scenarioBridge) {
        logRequest(payload, scenarioBridge);
        final String response;
        if (redisHelper != null) {
            response = callIns.invokeByRedis(name, payload, redisHelper);
        } else {
            response = callIns.invoke(name, payload);
        }
        logResponse(response, scenarioBridge);
        return response;
    }

    public String list(String serviceFilter, String methodFilter, Boolean withMessage) {
        return list(serviceFilter, methodFilter, withMessage, null);
    }

    public String list(String serviceFilter, String methodFilter, Boolean withMessage, ScenarioBridge scenarioBridge) {
        logRequest(String.format("serviceFilter=%s, methodFilter=%s, withMessage=%s", serviceFilter, methodFilter, withMessage), scenarioBridge);
        final String response;
        if (redisHelper != null) {
            response = listIns.invokeByRedis(serviceFilter, methodFilter, withMessage, redisHelper);
        } else {
            response = listIns.invoke(serviceFilter, methodFilter, withMessage);
        }
        logResponse(response, scenarioBridge);
        return response;
    }

    public String list(String name, Boolean withMessage) {
        return list(name, withMessage, null);
    }

    public String list(String name, Boolean withMessage, ScenarioBridge scenarioBridge) {
        logRequest( String.format("name=%s, withMessage=%s", name, withMessage), scenarioBridge);
        final String response;
        if (redisHelper != null) {
            response = listIns.invokeByRedis(name, withMessage, redisHelper);
        } else {
            response = listIns.invoke(name, withMessage);
        }
        logResponse( response, scenarioBridge);
        return response;
    }

    protected static void logRequest( String message, ScenarioBridge scenarioBridge) {
        log( "[request] " + message, scenarioBridge);
    }

    protected static void logResponse(String message, ScenarioBridge scenarioBridge) {
        log( "[response] " + message, scenarioBridge);
    }

    protected static void log( String message, ScenarioBridge scenarioBridge) {
        if (scenarioBridge != null) {
            scenarioBridge.log(Value.asValue(message));
        } else {
            log.info(message);
        }
    }

}
