package org.leores.ecpt;

public class WrongFormatException extends TException {
	public WrongFormatException(Object obj) {
		super(obj);
		return;
	}

	public WrongFormatException(Object... objs) {
		super(objs);
		return;
	}

	public WrongFormatException(String str, Object... objs) {
		super(str, objs);
		return;
	}
}
