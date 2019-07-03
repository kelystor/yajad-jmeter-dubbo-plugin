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
    static final String RPC_PROTOCOL = "RPC_PROTOCOL";
    static final String TIMEOUT = "TIMEOUT";
    static final String SERVICE_INTERFACE = "SERVICE_INTERFACE";
    static final String METHOD = "METHOD";
    static final String PARAMETER = "PARAMETER";
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
        DubboParamDto parameters = YamlParamParser.parseParameter(getParameter());

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
    //		registry.setGroup();
            registry.setAddress(getRegistryAddress());
            reference.setRegistry(registry);
        }

        reference.setProtocol(getRpcProtocol());
        reference.setTimeout(getTimeout());

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
            result = genericService.$invoke(getMethod(), parameterTypes, parameterValues);
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
                "RPC Protocol: " + getRpcProtocol() + "\n" +
                "Timeout: " + getTimeout() + "\n" +
                "Service Interface: " + getServiceInterface() + "\n" +
                "Method: " + getMethod() + "\n" +
                "Parameter: \n" + getParameter() + "\n";
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

    private String getRpcProtocol() {
        return getPropertyAsString(RPC_PROTOCOL, "dubbo");
    }

    void setRpcProtocol(String text) {
        setProperty(RPC_PROTOCOL, text);
    }

    private int getTimeout() {
        int timeout = 120000;
        try {
            timeout = Integer.valueOf(getPropertyAsString(TIMEOUT));
        } catch (NumberFormatException ignored) {
        }
        return timeout;
    }

    void setTimeout(String text) {
        setProperty(TIMEOUT, text);
    }

    private String getServiceInterface() {
        return getPropertyAsString(SERVICE_INTERFACE);
    }

    void setServiceInterface(String text) {
        setProperty(SERVICE_INTERFACE, text);
    }

    private String getMethod() {
        return getPropertyAsString(METHOD);
    }

    void setMethod(String text) {
        setProperty(METHOD, text);
    }

    private String getParameter() {
        return getPropertyAsString(PARAMETER);
    }

    void setParameter(String text) {
        setProperty(PARAMETER, text);
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
}
