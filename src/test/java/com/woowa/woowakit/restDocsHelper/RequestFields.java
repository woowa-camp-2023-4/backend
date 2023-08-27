package com.woowa.woowakit.restDocsHelper;

import java.util.Map;

public class RequestFields {

    private final Map<String, String> values;

    public RequestFields(final Map<String, String> values) {
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }
}
