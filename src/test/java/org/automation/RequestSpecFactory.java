package org.automation;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class RequestSpecFactory {
    private static final Environment ENV = Environment.getInstance();

    private RequestSpecFactory() {
    }

    private static RequestSpecification getRequestSpecLocal() {
        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ENV.getValue("baseUri"))
                .build();
        return requestSpecification
                .log().method()
                .log().uri()
                .log().params()
                .log().body();
    }

    private static Map<String, Supplier<RequestSpecification>> getRequestSpecMap() {
        Map<String, Supplier<RequestSpecification>> requestSpecMap = new HashMap<>();
        requestSpecMap.put("local", RequestSpecFactory::getRequestSpecLocal);
        return requestSpecMap;
    }

    public static RequestSpecification getRequestSpec(final String serviceName) {
        return getRequestSpecMap().get(serviceName).get();
    }
}
