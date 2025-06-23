package org.ikigaidigital.domain.constant;

public enum Sources {
    SERVICE_LAYER("Service Layer"),
    CONTROLLER_LAYER("Controller Layer"),
    SCHEDULED_SERVICE_LAYER("Scheduled Services Layer");

    public final String source;

    Sources(final String source) {
        this.source = source;
    }
}
