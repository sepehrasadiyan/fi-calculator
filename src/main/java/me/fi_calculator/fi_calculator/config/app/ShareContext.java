package me.fi_calculator.fi_calculator.config.app;

import me.fi_calculator.fi_calculator.domain.generic.FiRequestContext;

public final class ShareContext {
    private static final ThreadLocal<FiRequestContext> TL = new ThreadLocal<>();

    private ShareContext() {
    }

    public static void set(FiRequestContext ctx) {
        TL.set(ctx);
        org.slf4j.MDC.put("reqId", ctx.requestId().toString());
        org.slf4j.MDC.put("user", ctx.email());
    }
}
