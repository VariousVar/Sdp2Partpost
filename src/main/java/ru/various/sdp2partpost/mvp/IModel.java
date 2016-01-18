package ru.various.sdp2partpost.mvp;

import ru.various.sdp2partpost.FetchResult;
import ru.various.sdp2partpost.addressee.Addressee;
import ru.various.sdp2partpost.enums.Request;

import java.util.*;

public abstract class IModel extends Observable {
	public abstract void loadData(Properties properties);
	public abstract void importData(Properties properties);
	public abstract List<Addressee> getResult();
	public abstract String getErrorMessage();
	public abstract List<FetchResult> getFetchResult(Request request);
}
