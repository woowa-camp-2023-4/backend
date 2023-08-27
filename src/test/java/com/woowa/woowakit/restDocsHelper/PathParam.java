package com.woowa.woowakit.restDocsHelper;

public class PathParam {

    private final String name;
    private final String description;

    public PathParam(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
