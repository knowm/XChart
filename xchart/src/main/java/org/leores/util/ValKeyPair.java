package org.leores.util;

import java.io.Serializable;

public class ValKeyPair implements Comparable<ValKeyPair>, Serializable {
	public Object val;
	public Object key;
	public boolean sortByVal;

	public ValKeyPair(Object val, Object key, boolean sortByVal) {
		this.val = val;
		this.key = key;
		this.sortByVal = sortByVal;
	}

	public ValKeyPair(Object val, Object key) {
		this(val, key, true);
	}

	private static final long serialVersionUID = 3701852618907290365L;

	public int compareTo(ValKeyPair o) {
		int rtn = 0;
		if (sortByVal) {
			rtn = U.compare(val, o.val);
		} else {
			rtn = U.compare(key, o.key);
		}
		return rtn;
	}

}
