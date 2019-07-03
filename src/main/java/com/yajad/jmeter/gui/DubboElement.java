package com.yajad.jmeter.gui;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.yajad.jmeter.dto.DubboParamDto;
import com.yajad.jmeter.parse.YamlParamParser;
import com.yajad.jmeter.util.JsonUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DubboElement implements Serializable {
    static final String REGISTRY_PROTOCOL = "REGISTRY_PROTOCOL";
    static final String REGISTRY_ADDRESS = "REGISTRY_ADDRESS";
    static final String REGISTRY_GROUP = "REGISTRY_GROUP";
    static final String RPC_PROTOCOL = "RPC_PROTOCOL";
    static final String SERVICE_TIMEOUT = "SERVICE_TIMEOUT";
    static final String SERVICE_RETRIES = "SERVICE_RETRIES";
    static final String SERVICE_VERSION = "SERVICE_VERSION";
    static final String SERVICE_CLUSTER = "SERVICE_CLUSTER";
    static final String SERVICE_GROUP = "SERVICE_GROUP";
    static final String SERVICE_CONNECTIONS = "SERVICE_CONNECTIONS";
    static final String SERVICE_LOAD_BALANCE = "SERVICE_LOAD_BALANCE";
    static final String SERVICE_INTERFACE = "SERVICE_INTERFACE";
    static final String SERVICE_METHOD = "SERVICE_METHOD";
    static final String SERVICE_PARAMETER = "SERVICE_PARAMETER";
    private AbstractTestElement model;

    public DubboElement(AbstractTestElement model) {
        this.model = model;
    }

    public SampleResult sample() {
        SampleResult sampleResult = new SampleResult();

        sampleResult.setSampleLabel(model.getName());
        dubboInvoke(sampleResult);
        sampleResult.setDataType(SampleResult.TEXT);
        sampleResult.setResponseCodeOK();
        sampleResult.setResponseMessageOK();
        sampleResult.sampleEnd();

        return sampleResult;
    }

    private void dubboInvoke(SampleResult sampleResult) {
        DubboParamDto parameters = YamlParamParser.parseParameter(getServiceParameter());

        ApplicationConfig application = new ApplicationConfig();
        application.setName("Yajad-Dubbo-Sample");

        ReferenceConfig reference = new ReferenceConfig();
        reference.setApplication(application);

        // direct invoke provider
        if ("none".equalsIgnoreCase(getRegistryProtocol())) {
            reference.setUrl(getRpcProtocol() + "://" + getRegistryAddress() + "/" + getServiceInterface());
        } else {
            RegistryConfig registry = new RegistryConfig();
            registry.setProtocol(getRegistryProtocol());
            registry.setGroup(getRegistryGroup());
            registry.setAddress(getRegistryAddress());
            reference.setRegistry(registry);
        }

        reference.setProtocol(getRpcProtocol());
        reference.setTimeout(getServiceTimeout());
        reference.setRetries(getServiceRetries());
        reference.setVersion(getServiceVersion());
        reference.setCluster(getServiceCluster());
        reference.setGroup(getServiceGroup());
        reference.setConnections(getServiceConnections());
        reference.setLoadbalance(getServiceLoadBalance());

        reference.setGeneric(true);
        reference.setInterface(getServiceInterface());

        GenericService genericService = (GenericService) reference.get();

        String[] parameterTypes = parameters.getTypes().toArray(new String[]{});
        Object[] parameterValues = parameters.getValues().toArray();

        String samplerData = getSampleData();
        samplerData += "parameterTypes: " + Arrays.toString(parameterTypes) + "\n";
        samplerData += "parameterValues: " + Arrays.toString(parameterValues) + "\n";
        samplerData += "parameterClasses: " + Arrays.stream(parameterValues)
                .map(value -> value.getClass().getName())
                .collect(Collectors.toList());
        sampleResult.setSamplerData(samplerData);

        sampleResult.sampleStart();
        Object result;
        try {
            result = genericService.$invoke(getServiceMethod(), parameterTypes, parameterValues);
            sampleResult.setSuccessful(true);
        } catch (Exception e) {
            sampleResult.setSuccessful(true);
            result = e;
        }
        sampleResult.setResponseData(JsonUtils.toJson(result), StandardCharsets.UTF_8.name());
    }

    /**
     * Construct request data
     */
    private String getSampleData() {
        return "Registry Protocol: " + getRegistryProtocol() + "\n" +
                "Registry Address: " + getRegistryAddress() + "\n" +
                "Registry Group: " + getRegistryGroup() + "\n" +
                "RPC Protocol: " + getRpcProtocol() + "\n" +
                "Service Timeout: " + getServiceTimeout() + "\n" +
                "Service Retries: " + getServiceRetries() + "\n" +
                "Service Version: " + getServiceVersion() + "\n" +
                "Service Cluster: " + getServiceCluster() + "\n" +
                "Service Group: " + getServiceGroup() + "\n" +
                "Service Connections: " + getServiceConnections() + "\n" +
                "Service Load Balance: " + getServiceLoadBalance() + "\n" +
                "Interface: " + getServiceInterface() + "\n" +
                "Method: " + getServiceMethod() + "\n" +
                "Parameter: \n" + getServiceParameter() + "\n";
    }

    private String getRegistryProtocol() {
        return getPropertyAsString(REGISTRY_PROTOCOL);
    }

    void setRegistryProtocol(String text) {
        setProperty(REGISTRY_PROTOCOL, text);
    }

    private String getRegistryAddress() {
        return getPropertyAsString(REGISTRY_ADDRESS, "zookeeper");
    }

    void setRegistryAddress(String text) {
        setProperty(REGISTRY_ADDRESS, text);
    }

    private String getRegistryGroup() {
        return getPropertyAsString(REGISTRY_GROUP, "");
    }

    void setRegistryGroup(String text) {
        setProperty(REGISTRY_GROUP, text);
    }

    private String getRpcProtocol() {
        return getPropertyAsString(RPC_PROTOCOL, "dubbo");
    }

    void setRpcProtocol(String text) {
        setProperty(RPC_PROTOCOL, text);
    }

    private int getServiceTimeout() {
        return getPropertyAsInteger(SERVICE_TIMEOUT, 120000);
    }

    void setServiceTimeout(String text) {
        setProperty(SERVICE_TIMEOUT, text);
    }

    private int getServiceRetries() {
        return getPropertyAsInteger(SERVICE_RETRIES, 0);
    }

    void setServiceRetries(String text) {
        setProperty(SERVICE_RETRIES, text);
    }

    private String getServiceVersion() {
        return getPropertyAsString(SERVICE_VERSION, "");
    }

    void setServiceVersion(String text) {
        setProperty(SERVICE_VERSION, text);
    }

    private String getServiceCluster() {
        return getPropertyAsString(SERVICE_CLUSTER, "failfast");
    }

    void setServiceCluster(String text) {
        setProperty(SERVICE_CLUSTER, text);
    }

    private String getServiceGroup() {
        return getPropertyAsString(SERVICE_GROUP, "");
    }

    void setServiceGroup(String text) {
        setProperty(SERVICE_GROUP, text);
    }

    private Integer getServiceConnections() {
        return getPropertyAsInteger(SERVICE_CONNECTIONS, 100);
    }

    void setServiceConnections(String text) {
        setProperty(SERVICE_CONNECTIONS, text);
    }

    private String getServiceLoadBalance() {
        return getPropertyAsString(SERVICE_LOAD_BALANCE, "random");
    }

    void setServiceLoadBalance(String text) {
        setProperty(SERVICE_LOAD_BALANCE, text);
    }

    private String getServiceInterface() {
        return getPropertyAsString(SERVICE_INTERFACE);
    }

    void setServiceInterface(String text) {
        setProperty(SERVICE_INTERFACE, text);
    }

    private String getServiceMethod() {
        return getPropertyAsString(SERVICE_METHOD);
    }

    void setServiceMethod(String text) {
        setProperty(SERVICE_METHOD, text);
    }

    private String getServiceParameter() {
        return getPropertyAsString(SERVICE_PARAMETER);
    }

    void setServiceParameter(String text) {
        setProperty(SERVICE_PARAMETER, text);
    }

    private void setProperty(String key, String val) {
        model.setProperty(key, val);
    }

    private String getPropertyAsString(String key) {
        return model.getPropertyAsString(key);
    }

    private String getPropertyAsString(String key, String defaultValue) {
        return model.getPropertyAsString(key, defaultValue);
    }

    private Integer getPropertyAsInteger(String key, Integer defaultValue) {
        Integer value = defaultValue;
        try {
            value = Integer.valueOf(model.getPropertyAsString(key));
        } catch (NumberFormatException ignored) {
        }
        return value;
    }
}
