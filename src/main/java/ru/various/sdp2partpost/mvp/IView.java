package ru.various.sdp2partpost.mvp;

import ru.various.sdp2partpost.ColoredMessage;
import ru.various.sdp2partpost.FetchResult;
import ru.various.sdp2partpost.PropertiesHolder;
import ru.various.sdp2partpost.enums.Request;

import java.util.List;
import java.util.Observable;

public abstract class IView extends Observable implements Runnable {
	public abstract void setResult(ColoredMessage coloredMessage);
	public abstract String getInput();
	public abstract void quit(int state);
	public abstract void runSettingsWindow(PropertiesHolder propertiesHolder);
	public abstract void performView(Request request, List<FetchResult> fetchResult);
}
