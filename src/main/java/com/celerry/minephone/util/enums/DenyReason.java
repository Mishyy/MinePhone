package com.celerry.minephone.util.enums;

public enum DenyReason {
    Started("accepted"),
    Denied("denied"),
    Cancelled("cancelled"),
    Disconnect("disconnected");

    public final String label;
    DenyReason(String label) {
        this.label=label;
    }
}
