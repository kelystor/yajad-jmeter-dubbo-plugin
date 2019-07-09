package com.yajad.dubbo.invoker;

import com.yajad.dubbo.config.ReferenceConfig;

public interface DubboInvoker {
    void config(ReferenceConfig referenceConfig);

    Object $invoke(String method, String[] parameterTypes, Object[] parameterList);
}
