package org.leores.util;

import java.io.InputStream;

public class Demo extends LogUtil {
	public String pathIn = "";
	public String pathOut = "";

	public Demo() {
		pathIn = "";
		pathOut = "";
		setPathIn(U.getPath(this.getClass()));
		setPathOut("./");
		initialize();
	}

	public boolean initialize() {
		return true;
	}

	public void pathUpdated() {

	}

	public void setPathIn(String path) {
		this.pathIn = path;
		pathUpdated();
	}

	public void setPathOut(String path) {
		this.pathOut = path;
		pathUpdated();
	}

	public void setPath(String path) {
		setPathIn(path);
		setPathOut(path);
	}

	public String fi(String sFile) {
		String rtn = sFile;

		if (sFile != null && sFile.indexOf(pathIn) != 0) {
			rtn = pathIn + sFile;
		}

		return rtn;
	}

	public String fo(String sFile) {
		String rtn = sFile;

		if (sFile != null && sFile.indexOf(pathOut) != 0) {
			rtn = pathOut + sFile;
		}

		return rtn;
	}

	public String f(String sFile) {
		return fi(sFile);
	}

	public InputStream isRes(String name) {
		return this.getClass().getResourceAsStream(name);
	}
}
