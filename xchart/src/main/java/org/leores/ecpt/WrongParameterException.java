package org.leores.ecpt;

public class WrongParameterException extends TException {	
	public WrongParameterException(Object obj){
		super(obj);
		return;
	}

	public WrongParameterException(Object... objs) {
		super(objs);
		return;
	}

	public WrongParameterException(String str, Object... objs) {
		super(str, objs);
		return;
	}
}
