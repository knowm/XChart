package org.leores.ecpt;

import org.leores.util.ObjUtil;

public class TRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1540271937983801068L;

	public TRuntimeException() {
		super();
		return;
	}

	public TRuntimeException(Object obj) {
		super(ObjUtil.toStr(obj));
		return;
	}

	public TRuntimeException(Object... objs) {
		this("", objs);
		return;
	}

	public TRuntimeException(String str, Object... objs) {
		super(str + ObjUtil.toStr(objs));
		return;
	}
}
