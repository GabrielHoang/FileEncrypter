package com.fileencrypter.model;

import java.io.Serializable;

public class TransferData implements Serializable {
    MessageType header;
    Object load;

    public TransferData(MessageType header, Object load) {
        this.header = header;
        this.load = load;
    }

    public MessageType getHeader() {
        return header;
    }

    public void setHeader(MessageType header) {
        this.header = header;
    }

    public Object getLoad() {
        return load;
    }

    public void setLoad(Object load) {
        this.load = load;
    }
}

