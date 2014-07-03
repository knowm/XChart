package org.leores.ecpt;

public class UnsupportedTypeException extends TException {
	public UnsupportedTypeException(Object obj) {
		super(obj);
		return;
	}

	public UnsupportedTypeException(Object... objs) {
		super(objs);
		return;
	}

	public UnsupportedTypeException(String str, Object... objs) {
		super(str, objs);
		return;
	}
}
