package org.leores.ecpt;

public class UnMatchException extends TException {
	public UnMatchException(Object obj) {
		super(obj);
		return;
	}

	public UnMatchException(Object... objs) {
		super(objs);
		return;
	}

	public UnMatchException(String str, Object... objs) {
		super(str, objs);
		return;
	}
}
