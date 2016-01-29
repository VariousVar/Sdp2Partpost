package ru.various.sdp2partpost;

public interface LoggingCallback<T> {
    void inform(T argument, boolean valid, String reason);
}
