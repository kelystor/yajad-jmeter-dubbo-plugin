package com.yajad.dubbo.invoker.impl;

import com.yajad.dubbo.config.ReferenceConfig;
import com.yajad.dubbo.invoker.DubboInvoker;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

public class ApacheDubboInvoker implements DubboInvoker {
    private org.apache.dubbo.config.ReferenceConfig<GenericService> reference = new org.apache.dubbo.config.ReferenceConfig<>();
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

        ReferenceConfigCache cache = ReferenceConfigCache.getCache(referenceConfig.getRegistry().getAddress(), org.apache.dubbo.config.ReferenceConfig::toString);
        genericService = cache.get(reference);
    }

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] parameterList) {
        return genericService.$invoke(method, parameterTypes, parameterList);
    }
}
