package ru.various.sdp2partpost.mvp;

import ru.various.sdp2partpost.coloredmessage.ColoredMessage;
import ru.various.sdp2partpost.PropertiesHolder;

import java.util.Observable;

public abstract class IView extends Observable implements Runnable {
	public abstract void setResult(ColoredMessage coloredMessage);
	public abstract String getInput();
	public abstract void quit(int state);
	public abstract void runSettingsWindow(PropertiesHolder propertiesHolder);
	public abstract void showLog(String text);
}
