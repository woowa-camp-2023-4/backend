package com.woowa.woowakit.restDocsHelper;

import java.util.Map;

public class ResponseFields {

    private final Map<String, String> values;

    public ResponseFields(final Map<String, String> values) {
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }
}
