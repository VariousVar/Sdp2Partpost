package ru.various.sdp2partpost;

import java.awt.*;

public class RedColoredMessage implements ColoredMessage {
    private String message;

    public RedColoredMessage(String message) {
        this.message = message;
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
