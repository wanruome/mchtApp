/**
 *	@copyright wanruome-2017
 * 	@author wanruome
 * 	@create 2017年8月7日 下午3:53:03
 */
package com.zjsj.mchtapp.util;

public class StringMask {
	public static String getMaskBankNo(String source) {
		int strLength = getLength(source);
		if (strLength == 0) {
			return source;
		}
		if (strLength <= 4) {
			return getFillString(4) + source;
		}
		else if (strLength < 10) {
			int fillSize = (strLength - 4) / 3;
			if (fillSize < 1) {
				fillSize = 1;
			}
			int headerSize = strLength - 4 - fillSize;
			if (fillSize < 4) {
				fillSize = 4;
			}
			return source.substring(0, headerSize) + getFillString(fillSize) + source.substring(strLength - 4);

		}
		else {
			int fillSize = strLength - 10;
			if (fillSize < 4) {
				fillSize = 4;
			}
			else if (fillSize > 9) {
				fillSize = 9;
			}
			return source.substring(0, 6) + getFillString(fillSize) + source.substring(strLength - 4);
		}
	}

	public static String getMaskString(String source) {
		int strLength = getLength(source);
		if (strLength == 0) {
			return source;
		}
		if (strLength == 1) {
			return "*";
		}
		if (isIdCard(source)) {
			return source.substring(0, 4) + getFillString(strLength - 7) + source.substring(strLength - 3, strLength);
		}
		else if (isBankCard(source)) {
			return source.substring(0, 6) + getFillString(strLength - 9) + source.substring(strLength - 3, strLength);
		}
		else if (isMoblie(source)) {
			return source.substring(0, 3) + getFillString(strLength - 6) + source.substring(strLength - 3, strLength);
		}
		else if (isTelphone(source)) {
			if (source.contains("-")) {
				StringBuilder sb = new StringBuilder();
				String[] resultData = source.split("-");
				for (int i = 0; i < resultData.length; i++) {
					int dataSize = getLength(resultData[i]);
					if (i == 0) {
						sb.append(resultData[i]);
					}
					else if (i == 1) {
						sb.append("-");
						if (dataSize > 4) {
							sb.append("****").append(resultData[i].substring(dataSize - 4, dataSize));
						}
						else if (dataSize > 0) {
							sb.append("****").append(resultData[i].substring(dataSize - 1, dataSize));
						}
						else {
							sb.append("****");
						}
					}
					else if (i == 2) {
						sb.append("-").append(resultData[i]);
					}
				}
				return new String(sb);
			}
			else {
				return source.substring(0, 4) + getFillString(strLength - 7)
						+ source.substring(strLength - 3, strLength);
			}

		}
		else if (isEmail(source)) {
			int index = source.indexOf("@");
			if (index <= 1) {
				return getFillString(2) + source.substring(1, strLength);
			}
			else if (index < 4) {
				// return source.substring(0, 1)+getFillString(index-1)+source.substring(index,
				// strLength);
				int fillSize = index - 1;
				if (fillSize < 2) {
					fillSize = 2;
				}
				return source.substring(0, 1) + getFillString(fillSize) + source.substring(index, strLength);
			}
			else {
				// return source.substring(0, 2)+getFillString(index-3)+source.substring(index-1,
				// strLength);
				int fillSize = index - 3;
				if (fillSize < 2) {
					fillSize = 2;
				}
				return source.substring(0, 2) + getFillString(fillSize) + source.substring(index, strLength);
			}
		}
		// else if(isENString(source))
		// {
		// int headerSize=strLength/3;
		// if(headerSize<=0)
		// {
		// headerSize=1;
		// }
		// if(headerSize>8)
		// {
		// headerSize=8;
		// }
		// int footerSize=strLength/4;
		// if(footerSize<0)
		// {
		// footerSize=0;
		// }
		// if(footerSize>6)
		// {
		// footerSize=6;
		// }
		// int fillSize=strLength-headerSize-footerSize;
		// StringBuilder sb=new StringBuilder();
		// sb.append(source.substring(0, headerSize)).append(getFillString(fillSize));
		// if(footerSize>0)
		// {
		// sb.append(source.substring(strLength-footerSize, strLength));
		// }
		// return new String(sb);
		//
		// }
		else if (isCNName(source)) {
			int subSize = (strLength + 1) / 3;
			return getFillString(strLength - subSize) + source.substring(strLength - subSize, strLength);
		}
		else {
			int headerSize = strLength / 3;
			if (headerSize <= 0) {
				headerSize = 1;
			}
			if (headerSize > 8) {
				headerSize = 8;
			}
			int footerSize = strLength / 4;
			if (footerSize < 0) {
				footerSize = 0;
			}
			if (footerSize > 6) {
				footerSize = 6;
			}
			int fillSize = strLength - headerSize - footerSize;
			StringBuilder sb = new StringBuilder();
			sb.append(source.substring(0, headerSize)).append(getFillString(fillSize));
			if (footerSize > 0) {
				sb.append(source.substring(strLength - footerSize, strLength));
			}
			return new String(sb);

		}
	}

	private static int getLength(String source) {
		return null == source || source.length() == 0 ? 0 : source.length();
	}

	// /**
	// * 获取字符串长度，一个中文算2个字
	// */
	private static int getStrLength(String source) {
		if (getLength(source) == 0) {
			return 0;
		}
		int length = 0;
		char[] chars = source.toCharArray();
		for (char a : chars) {
			length++;
			if (a > 255) {
				length++;
			}
		}
		return length;
	}

	private static String getFillString(int length) {
		StringBuilder sb = new StringBuilder();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				sb.append("*");
			}
		}
		else {
			sb.append("******");
		}
		return sb.toString();
	}

	private static boolean isIdCard(String source) {
		// int strLength=getLength(source);
		// if(strLength!=15&&strLength!=18)
		// {
		// return false;
		// }
		String regex = "^\\d{6}\\d{2}(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}|"
				+ "\\d{6}(19|20)\\d{2}(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}[0-9xX]{1}$";
		if (source.matches(regex)) {

			return true;
		}
		else {
			return false;
		}

	}

	private static boolean isBankCard(String source) {
		// int strLength=getLength(source);
		// if(strLength<15||strLength>20)
		// {
		// return false;
		// }
		String regex = "^\\d{15,20}$";
		if (source.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isMoblie(String source) {
		// int strLength=getLength(source);
		// if(strLength==0)
		// {
		// return false;
		// }
		String regex = "^1\\d{10}|0861\\d{10}|861\\d{10}$";
		if (source.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isNumber(String source) {
		// int strLength=getLength(source);
		// if(strLength==0)
		// {
		// return false;
		// }
		String regex = "^\\d{1,1000}$";
		if (source.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isTelphone(String source) {
		// int strLength=getLength(source);
		// if(strLength==0)
		// {
		// return false;
		// }
		String regex = "^(0\\d{2,4}-\\d{7,8}(-\\d{1,4})?)|(0\\d{2,4}\\d{7,8})$";

		if (source.matches(regex)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isEmail(String source) {
		// int strLength=getLength(source);
		// if(strLength==0)
		// {
		// return false;
		// }
		int strLength = getLength(source);
		int index = source.indexOf("@");
		if (index <= 0 || index > strLength - 4) {
			return false;
		}
		else if (source.lastIndexOf("@") != index) {
			return false;
		}
		else {
			return true;
		}
	}

	private static boolean isENString(String source) {
		// int strLength=getLength(source);
		// if(strLength==0)
		// {
		// return false;
		// }
		int strLength = getLength(source);
		if (strLength == getStrLength(source)) {
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isCNName(String source) {
		int strLength = getLength(source);
		if (strLength == 0) {
			return false;
		}
		else if (strLength > 5) {
			return false;
		}
		else if (strLength * 2 != getStrLength(source)) {
			return false;
		}
		else {
			return true;
		}
	}

	public static void main(String[] args) {
		System.out.println("19966778899:" + getMaskString("19966778899"));
		System.out.println("62691519900919178x:" + getMaskString("62691519900919178x"));
		System.out.println("95588702317510751:" + getMaskString("95588702317510751"));
		System.out.println("023-17510751-0720:" + getMaskString("023-17510751-0720"));
		System.out.println("057175107519:" + getMaskString("057175107519"));
		System.out.println("a@5.c:" + getMaskString("a@5.c"));
		System.out.println("战狼2电影票@163.com:" + getMaskString("战狼2电影票@163.com"));
		System.out.println("zjsj2017@5c:" + getMaskString("zjsj2017@5c"));
		System.out.println("支:" + getMaskString("支"));
		System.out.println("支付:" + getMaskString("支付"));
		System.out.println("新生支付:" + getMaskString("新生支付"));
		System.out.println("新生支付航:" + getMaskString("新生支付航"));
		System.out.println("海南新生支付:" + getMaskString("海南新生支付"));
		System.out.println("海南新生支f:" + getMaskString("海南新生支f"));
		System.out.println("海航集团海南新生支付杭州分公司:" + getMaskString("海航集团海南新生支付杭州分公司"));
		System.out.println("ne海w航集pay.com团海南新生支付州分http://www.esicash.c杭om公/司:"
				+ getMaskString("ne海w航集pay.com团海南新生支付州分http://www.esicash.c杭om公/司"));
		System.out.println("NULL/EMPTY:" + getMaskString(""));
	}

}
