package com.fileencrypter.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum MessageType {
    END_CONNECTION(0),
    USER_PRIVATE_KEY(1),
    USER_PUBLIC_KEY(2),
    SESSION_KEY(3),
    FILE(4);

    private int code;

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MessageType getName(int code) {
        for(MessageType type : MessageType.values()) {
            if(code == type.getCode()) return type;
        }
        return null;
    }
}
