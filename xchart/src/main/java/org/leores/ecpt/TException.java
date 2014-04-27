package org.leores.ecpt;

import org.leores.util.ObjUtil;

public class TException extends Exception {
	public TException() {
		super();
		return;
	}

	public TException(Object obj) {
		super(ObjUtil.toStr(obj));
		return;
	}

	public TException(Object... objs) {
		this("", objs);
		return;
	}

	public TException(String str, Object... objs) {
		super(str + ObjUtil.toStr(objs));
		return;
	}
}
