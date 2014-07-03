package org.leores.util;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.leores.ecpt.TRuntimeException;
import org.leores.ecpt.UnsupportedTypeException;
import org.leores.ecpt.WrongFormatException;
import org.leores.ecpt.WrongParameterException;

public class StrUtil extends LogUtil {
	public static String sBlock = "}"; //Priority high
	public static String sEnumerate = ";";
	public static String sRepeat = "@";
	public static String sStep = ":";//Priority low

	public static String sNoEval = "~";
	public static String de = ",";

	//\\S+? Several non-whitespace characters ? makes quantifier "+" reluctant. It tries to find the smallest match.  
	public static String sVarPat = "\\$(\\S+?)\\$";
	public static String sExpPat = "#(\\S+?)#";

	public static final int EVAL_InvalidIgnore = 0x00000001;
	public static final int EVAL_InvalidException = 0x00000002;
	public static final int EVAL_NullIgnore = 0x00000004;
	public static final int EVAL_NullException = 0x00000008;

	public static List parseList(Class type, String[] tokens, boolean bStepped) {
		List rtnList = null;

		try {
			if (bStepped && tokens.length < 3) {
				throw new WrongFormatException(tokens.toString());
			}

			if (type != null && tokens != null && tokens.length >= 1) {
				rtnList = new ArrayList();
				if (type == Integer.class) {
					if (bStepped) {
						Integer i1, i2, i3;
						i1 = Integer.parseInt(tokens[0]);
						i2 = Integer.parseInt(tokens[1]);
						i3 = Integer.parseInt(tokens[2]);
						for (; i1 <= i3; i1 += i2) {
							rtnList.add(i1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							Integer i1 = Integer.parseInt(tokens[i]);
							rtnList.add(i1);
						}
					}
				} else if (type == Float.class) {
					if (bStepped) {
						BigDecimal i1 = new BigDecimal(tokens[0]);
						BigDecimal i2 = new BigDecimal(tokens[1]);
						BigDecimal i3 = new BigDecimal(tokens[2]);
						for (; i1.compareTo(i3) <= 0; i1 = i1.add(i2)) {
							Float f1 = i1.floatValue();
							rtnList.add(f1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							Float i1 = Float.parseFloat(tokens[i]);
							rtnList.add(i1);
						}
					}
				} else if (type == Long.class) {
					if (bStepped) {
						Long i1, i2, i3;
						i1 = Long.parseLong(tokens[0]);
						i2 = Long.parseLong(tokens[1]);
						i3 = Long.parseLong(tokens[2]);
						for (; i1 <= i3; i1 += i2) {
							rtnList.add(i1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							Long i1 = Long.parseLong(tokens[i]);
							rtnList.add(i1);
						}
					}
				} else if (type == Double.class) {
					if (bStepped) {
						BigDecimal i1 = new BigDecimal(tokens[0]);
						BigDecimal i2 = new BigDecimal(tokens[1]);
						BigDecimal i3 = new BigDecimal(tokens[2]);
						for (; i1.compareTo(i3) <= 0; i1 = i1.add(i2)) {
							Double d1 = i1.doubleValue();
							rtnList.add(d1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							Double i1 = Double.parseDouble(tokens[i]);
							rtnList.add(i1);
						}
					}
				} else if (type == BigDecimal.class) {
					if (bStepped) {
						BigDecimal i1 = new BigDecimal(tokens[0]);
						BigDecimal i2 = new BigDecimal(tokens[1]);
						BigDecimal i3 = new BigDecimal(tokens[2]);
						for (; i1.compareTo(i3) <= 0; i1 = i1.add(i2)) {
							rtnList.add(i1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							BigDecimal i1 = new BigDecimal(tokens[i]);
							rtnList.add(i1);
						}
					}
				} else if (type == String.class) {
					if (bStepped) {
						String i1, i2, i3;
						i1 = tokens[0];
						i2 = tokens[1];
						i3 = tokens[2];
						for (; i1.length() <= i3.length(); i1 += i2) {
							rtnList.add(i1);
						}
					} else {
						for (int i = 0; i < tokens.length; i++) {
							String i1 = tokens[i];
							rtnList.add(i1);
						}
					}
				} else if (type == Boolean.class) {
					if (bStepped) {
						throw new WrongParameterException("Boolean does not supported bStepped!");
					} else {
						for (int i = 0; i < tokens.length; i++) {
							Boolean i1 = Boolean.parseBoolean(tokens[i]);
							rtnList.add(i1);
						}
					}

				} else {
					throw new UnsupportedTypeException(type);
				}
			}
		} catch (Exception e) {
			tLog(e);
		}

		return rtnList;
	}

	public static String[] split(String str, int limit) {
		String[] rtn = null;
		if (str != null) {
			rtn = new String[] { str };
			if (str.indexOf(sBlock) >= 0) {
				rtn = str.split(sBlock, limit);
			} else if (str.indexOf(sEnumerate) >= 0) {
				rtn = str.split(sEnumerate, limit);
			} else if (str.indexOf(de) >= 0) {
				rtn = str.split(de, limit);
			}
		}
		return rtn;
	}

	/**
	 * Trailing empty strings are not included in the resulting array.
	 * 
	 * @param str
	 * @return
	 */
	public static String[] split(String str) {
		return split(str, 0);
	}

	/**
	 * Here only when s == null we return null, when s == "", it returns a size
	 * 0 list. <br>
	 * <br>
	 * spBlock = "/", spJoin = "&", spRepeat = "@", spEnumerate = ";", spStep =
	 * ":".
	 * 
	 * @param type
	 * @param s
	 * @return
	 */

	public static <A> List<A> parseList(Class<A> type, String s) {
		List<A> rtn = null;
		try {
			if (type != null && s != null) {
				String s_ = s.trim();
				if ("".equals(s_)) {
					rtn = new ArrayList<A>();
				} else if (s_.indexOf(sBlock) >= 0) {
					String[] tokens = s_.split(sBlock);
					rtn = parseList(type, tokens, false);
				} else if (s_.indexOf(sEnumerate) >= 0) {
					String[] tokens = s_.split(sEnumerate);
					rtn = new ArrayList<A>();
					for (int i = 0; i < tokens.length; i++) {
						List<A> listi = parseList(type, tokens[i]);
						rtn.addAll(listi);
					}
				} else if (s_.indexOf(sRepeat) >= 0) {
					String[] tokens = s_.split(sRepeat);
					Integer nRepeat = Integer.parseInt(tokens[1]);
					String[] tokens2 = new String[nRepeat];
					for (int i = 0; i < nRepeat; i++) {
						tokens2[i] = tokens[0];
					}
					rtn = parseList(type, tokens2, false);
				} else if (s_.indexOf(sStep) >= 0) {
					String[] tokens = s_.split(sStep);
					if (tokens.length >= 3) {
						rtn = parseList(type, tokens, true);
					} else {
						throw new WrongFormatException(s_);
					}
				} else {
					String[] tokens = { s_ };
					rtn = parseList(type, tokens, false);
				}
			}
		} catch (Exception e) {
			tLog(e);
		}
		return rtn;
	}

	public static List parseList(String sType, String s) {
		List rtnList = null;
		try {
			rtnList = parseList(Class.forName(sType), s);
		} catch (Exception e) {
			tLog(e);
		}
		return rtnList;
	}

	public static boolean valid(String s) {
		boolean rtn = false;
		if (s != null && s.length() > 0) {
			rtn = true;
		}
		return rtn;
	}

	public static String format(Date date, String sFormat) {
		String rtn = null;

		if (date != null && sFormat != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
			rtn = sdf.format(date);
		}

		return rtn;
	}

	public static String insert(String sOrig, int offset, String sToBeInserted) {
		String rtn = null;

		if (sOrig != null && offset >= 0 && sToBeInserted != null) {
			StringBuffer sb = new StringBuffer(sOrig);
			rtn = sb.insert(offset, sToBeInserted).toString();
		}

		return rtn;
	}

	public static String insert(String sOrig, String sBefore, String sToBeInserted) {
		String rtn = null;

		if (sOrig != null && sBefore != null && sToBeInserted != null) {
			int i = sOrig.indexOf(sBefore);
			rtn = insert(sOrig, i, sToBeInserted);
		}

		return rtn;
	}

	public static String insert(String sOrig, String sBefore, Object oToBeInserted) {
		String rtn = null;

		if (oToBeInserted != null) {
			rtn = insert(sOrig, sBefore, oToBeInserted.toString());
		}

		return rtn;
	}

	public static String wrap(String[] strs, String before, String after, String delimiter, Integer iFrom, Integer iTo) {
		String rtn = null;

		if (strs != null && before != null && delimiter != null && after != null && iFrom != null && iTo != null) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = iFrom; i <= iTo && i < strs.length; i++) {
				sBuffer.append(before);
				sBuffer.append(strs[i]);
				sBuffer.append(after);
				if (i < iTo && i < strs.length - 1) {
					sBuffer.append(delimiter);
				}
			}
			rtn = sBuffer.toString();
		}

		return rtn;
	}

	public static String wrap(String[] strs, String before, String after, String delimiter) {
		String rtn = null;
		if (strs != null) {
			rtn = wrap(strs, before, after, delimiter, 0, strs.length - 1);
		}
		return rtn;
	}

	public static String wrapCSVArray(String[] strs) {
		String before = "\"";
		String after = "\"";
		String delimiter = ",";
		return wrap(strs, before, after, delimiter);
	}

	public static String wrapCSV(String... strs) {
		String before = "\"";
		String after = "\"";
		String delimiter = ",";
		return wrap(strs, before, after, delimiter);
	}

	public static String wrap(String before, String after, String delimiter, String... strs) {
		String rtn = null;
		if (strs != null) {
			rtn = wrap(strs, before, after, delimiter, 0, strs.length - 1);
		}
		return rtn;
	}

	public static String[] unWrap(String str, String before, String after, String delimiter) {
		String[] rtn = null;

		if (str != null && before != null && after != null && delimiter != null) {
			String[] tokens = str.split(delimiter, -1);
			rtn = new String[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				int iBegin = 0;
				int iEnd = tokens[i].length();
				if (tokens[i].startsWith(before)) {
					iBegin = before.length();
				}
				if (tokens[i].endsWith(after)) {
					iEnd = iEnd - after.length();
				}
				rtn[i] = tokens[i].substring(iBegin, iEnd);
			}
		}

		return rtn;
	}

	public static String getCmdArg(String argName, String[] args) {
		String rtn = null;

		if (args != null && argName != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] != null) {
					int iArg = args[i].indexOf(argName);
					if (iArg == 0) {
						if (args[i].length() > argName.length()) {
							rtn = args[i].substring(argName.length());
						} else {
							rtn = "";
						}
						break;
					}
				}
			}
		}

		return rtn;
	}

	public static String cap1stLetter(String str) {
		String rtn = null;

		if (str != null) {
			if (str.length() > 0) {
				StringBuffer sBuffer = new StringBuffer();
				char c0 = str.charAt(0);
				sBuffer.append(Character.toUpperCase(c0));
				if (str.length() > 1) {
					sBuffer.append(str.substring(1));
				}
				rtn = sBuffer.toString();
			} else {
				rtn = "";
			}
		}

		return rtn;
	}

	public static String getterMethodName(String sVar) {
		String rtn = null;
		if (sVar != null) {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("get");
			sBuffer.append(cap1stLetter(sVar));
			rtn = sBuffer.toString();
		}
		return rtn;
	}

	public static String setterMethodName(String sVar) {
		String rtn = null;
		if (sVar != null) {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("set");
			sBuffer.append(cap1stLetter(sVar));
			rtn = sBuffer.toString();
		}
		return rtn;
	}

	public static String regexQuote(String str) {
		return Pattern.quote(str);
	}

	public static String[] trim(String[] strs) {
		String[] rtn = null;
		if (strs != null) {
			int i, j, k, l;
			for (i = 0; i < strs.length; i++) {
				if (valid(strs[i])) {
					break;
				}
			}
			for (j = strs.length - 1; j >= 0; j--) {
				if (valid(strs[j])) {
					break;
				}
			}
			if (j - i + 1 > 0) {
				rtn = new String[j - i + 1];
				for (l = 0, k = i; k <= j; k++, l++) {
					rtn[l] = strs[k];
				}
			}
		}
		return rtn;
	}

	public static boolean hasPattern(String str, String regex) {
		boolean rtn = false;

		if (str != null && regex != null) {
			Pattern pat = Pattern.compile(regex);
			Matcher matcher = pat.matcher(str);
			rtn = matcher.find();
		}

		return rtn;
	}

	public static boolean hasVariable(String str) {
		return hasPattern(str, sVarPat);
	}

	public static boolean hasExpression(String str) {
		return hasPattern(str, sExpPat);
	}

	/**
	 * \n does not count as a character represented by "." in regular
	 * expression. So a valid evaluation (Variable or Expression) should be
	 * within a same line.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasEvaluation(String str) {
		return hasPattern(str, "(" + sVarPat + "|" + sExpPat + ")");
	}

	public static BigDecimal eval1Expression(String str) {
		BigDecimal rtn = null;

		if (str != null && Expression.bValid(str)) {
			Expression expression = new Expression(str);
			rtn = expression.eval();
		}

		return rtn;
	}

	public static String evalExpression(String str, int flags) {
		String rtn = str;

		if (hasExpression(str)) {
			rtn = "";
			Pattern pat = Pattern.compile(sExpPat);
			Matcher matcher = pat.matcher(str);
			int iLastTo = 0;
			while (matcher.find()) {
				int iFrom = matcher.start(), iTo = matcher.end();
				rtn += str.substring(iLastTo, iFrom);
				String sExp = matcher.group(1);
				BigDecimal bdVal = eval1Expression(sExp);
				if (bdVal == null) {
					if ((flags & EVAL_InvalidException) > 0) {
						throw new TRuntimeException("evalExpression invalid expression: ", sExp);
					}
					if ((flags & EVAL_InvalidIgnore) > 0) {
						rtn += str.substring(iFrom, iTo);
					}

				} else {
					rtn += bdVal.toPlainString();
				}
				iLastTo = iTo;
			}
			rtn += str.substring(iLastTo);
		}

		return rtn;
	}

	/**
	 * Only support one method in str!
	 * 
	 * @param str
	 * @return
	 */

	public static List<String> parseMethodAsStrList(String str, boolean bSplitPars) {
		List<String> rtn = null;

		if (str != null) {
			int i0 = str.indexOf("("), i1 = str.indexOf(")");
			if (i0 > 0 && i1 > i0) {
				rtn = new ArrayList();
				String sMethod = str.substring(0, i0);
				rtn.add(sMethod);
				if (i1 > i0 + 1) {
					String sPars = str.substring(i0 + 1, i1);
					if (bSplitPars) {
						String[] tokens = split(sPars, -1);
						for (int i = 0; i < tokens.length; i++) {
							rtn.add(tokens[i]);
						}
					} else {
						rtn.add(sPars);
					}
				}
			}
		}

		return rtn;
	}

	public static List<String> parseMethodAsStrList(String str) {
		return parseMethodAsStrList(str, true);
	}

	public static URL getURL(Class tClass) {
		URL rtn = null;
		if (tClass != null) {
			ClassLoader cl = tClass.getClassLoader();
			rtn = cl.getResource(tClass.getName().replace('.', '/') + ".class");
		}
		return rtn;
	}

	public static String getPath(Class tClass) {
		String rtn = null;
		URL url = getURL(tClass);
		if (url != null) {
			rtn = url.getPath();
			rtn = rtn.replace(tClass.getSimpleName() + ".class", "");
		}

		return rtn;
	}

	public static String getWorkingPath() {
		return new File(".").getAbsolutePath();
	}

}
