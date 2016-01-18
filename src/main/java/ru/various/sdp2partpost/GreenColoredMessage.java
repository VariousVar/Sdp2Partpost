package ru.various.sdp2partpost;

import java.awt.*;

public class GreenColoredMessage implements ColoredMessage {
    private String message;

    public GreenColoredMessage(String message) {
        this.message = message;
    }

    @Override
    public Color getColor() {
	    return new Color(16, 108, 35);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
