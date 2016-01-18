package ru.various.sdp2partpost.mvp;

import ru.various.sdp2partpost.enums.Result;
import ru.various.sdp2partpost.enums.Request;

import java.util.Observer;

public abstract class IPresenter implements Observer {
	public abstract void processRequest(Request request);
	public abstract void processResult(Result result);
}
