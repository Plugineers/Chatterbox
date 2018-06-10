package com.patrickanker.chatterbox.permissions;

public class PermissionResponse {

    public static final String ALL_OK = "okeedokee"; // Why not.

    private final boolean allowed;
    private final String message;

    public PermissionResponse(boolean allow, String msg) {
        allowed = allow;
        message = msg;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }
}
