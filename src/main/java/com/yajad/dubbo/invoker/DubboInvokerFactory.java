package com.yajad.dubbo.invoker;

import com.yajad.dubbo.invoker.impl.ApacheDubboInvoker;
import com.yajad.dubbo.invoker.impl.OldDubboInvoker;

public class DubboInvokerFactory {
    public static DubboInvoker get() {
        // 如果存在新的dubbo就使用新的，否则使用老的
        try {
            Class.forName("org.apache.dubbo.config.ReferenceConfig");
            return new ApacheDubboInvoker();
        } catch (ClassNotFoundException ignore) {
            return new OldDubboInvoker();
        }
    }
}
