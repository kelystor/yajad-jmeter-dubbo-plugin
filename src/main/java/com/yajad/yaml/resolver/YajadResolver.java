package com.yajad.yaml.resolver;

import com.yajad.yaml.tag.YajadTag;
import org.snakeyaml.engine.v1.resolver.JsonScalarResolver;

import java.util.regex.Pattern;

public class YajadResolver extends JsonScalarResolver {
    public static final Pattern LONG = Pattern.compile("^(?:-?(?:0|[0-9][0-9]*)[Ll])$");
    public static final Pattern DATE = Pattern.compile("^(?:[0-9]{4}-[0-9]{2}-[0-9]{2}(?: [0-9]{2}:[0-9]{2}:[0-9]{2})?)$");

    protected void addImplicitResolvers() {
        super.addImplicitResolvers();
        this.addImplicitResolver(YajadTag.LONG, LONG, "-0123456789Ll");
        this.addImplicitResolver(YajadTag.DATE, DATE, "-0123456789: ");
    }
}
