package org.knowm.xchart;

public interface XcTrans2<T1, T2, N extends Number> {

	N trans(T1 o1, T2 o2);
	
}
