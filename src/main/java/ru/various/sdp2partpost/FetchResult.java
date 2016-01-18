package ru.various.sdp2partpost;

public class FetchResult {
	private Object object;
	private boolean valid;
	private String reason;

	public FetchResult(Object object, boolean valid, String reason) {
		this.object = object;
		this.valid = valid;
		this.reason = reason;
	}

	public FetchResult(Object object) {
		this.object = object;
		this.valid = true;
		this.reason = "";
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
