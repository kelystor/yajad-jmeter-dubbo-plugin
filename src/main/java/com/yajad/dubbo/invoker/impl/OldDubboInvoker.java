package com.yajad.dubbo.invoker.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.yajad.dubbo.config.ReferenceConfig;
import com.yajad.dubbo.invoker.DubboInvoker;

@SuppressWarnings("deprecation")
public class OldDubboInvoker implements DubboInvoker {
    private com.alibaba.dubbo.config.ReferenceConfig<GenericService> reference = new com.alibaba.dubbo.config.ReferenceConfig<>();
    private GenericService genericService;

    @Override
    public void config(ReferenceConfig referenceConfig) {
        ApplicationConfig application = new ApplicationConfig();
        application.setName(referenceConfig.getApplication().getName());
        reference.setApplication(application);

        if (referenceConfig.getUrl() != null) {
            reference.setUrl(referenceConfig.getUrl());
        } else {
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol(referenceConfig.getRegistry().getProtocol());
            registry.setGroup(referenceConfig.getRegistry().getGroup());
            registry.setAddress(referenceConfig.getRegistry().getAddress());
            reference.setRegistry(registry);
        }

        reference.setProtocol(referenceConfig.getProtocol());
        reference.setTimeout(referenceConfig.getTimeout());
        reference.setRetries(referenceConfig.getRetries());
        reference.setVersion(referenceConfig.getVersion());
        reference.setCluster(referenceConfig.getCluster());
        reference.setGroup(referenceConfig.getGroup());
        reference.setConnections(referenceConfig.getConnections());
        reference.setLoadbalance(referenceConfig.getLoadBalance());
        reference.setGeneric(referenceConfig.isGeneric());
        reference.setInterface(referenceConfig.getInterface());

        ReferenceConfigCache cache = ReferenceConfigCache.getCache(referenceConfig.getRegistry().getAddress());
        genericService = cache.get(reference);
    }

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] parameterList) {
        return genericService.$invoke(method, parameterTypes, parameterList);
    }
}
