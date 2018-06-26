/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午2:22:40 
 */
package com.ruomm.base.tools.regextool;

import android.text.TextUtils;
import android.widget.TextView;
/**
 * 正则验证Text文本是否正确的表达式，以RegexCallBack返回是否正确的信息
 * @author Ruby
 *
 */
public class RegexText {
	private boolean isRegex = false;
	private RegexCallBack regexCallBack = null;

	public static RegexText with(RegexCallBack regexCallBack) {
		return new RegexText(regexCallBack);
	}

	private RegexText(RegexCallBack regexCallBack) {
		super();
		this.regexCallBack = regexCallBack;
		this.isRegex = true;
	}

	public RegexText doRegex(TextView regexTV, String regularExpression) {
		return doRegex(regexTV, regularExpression, null, null);
	}

	public RegexText doRegex(TextView regexTV, String regularExpression, String errorInfo) {
		return doRegex(regexTV, regularExpression, errorInfo, null);
	}

	public RegexText doRegex(TextView regexTV, String regularExpression, String errorInfo, String emptyInfo) {
		if (!this.isRegex) {
			return this;
		}
		String value = regexTV.getText().toString();
		this.isRegex = RegexUtil.doRegex(value, regularExpression);
		doErrorInfo(regexTV, value, errorInfo, emptyInfo);
		return this;
	}

	public RegexText doEqual(TextView regexTV, String regularExpression) {
		return doEqual(regexTV, regularExpression, null, null);
	}

	public RegexText doEqual(TextView regexTV, String regularExpression, String errorInfo) {
		return doEqual(regexTV, regularExpression, errorInfo, null);
	}

	public RegexText doEqual(TextView regexTV, String regularExpression, String errorInfo, String emptyInfo) {
		if (!this.isRegex) {
			return this;
		}
		String value = regexTV.getText().toString();
		if (TextUtils.isEmpty(value)) {
			if (TextUtils.isEmpty(regularExpression)) {
				this.isRegex = true;
			}
			else {
				this.isRegex = false;
			}
		}
		this.isRegex = value.equals(regularExpression);
		doErrorInfo(regexTV, value, errorInfo, emptyInfo);
		return this;
	}

	public RegexText doRegexSize(TextView regexTV, int min, int max) {
		return doRegexSize(regexTV, min, max, null, null);
	}

	public RegexText doRegexSize(TextView regexTV, int min, int max, String errorInfo) {
		return doRegexSize(regexTV, min, max, errorInfo, null);
	}

	public RegexText doRegexSize(TextView regexTV, int min, int max, String errorInfo, String emptyInfo) {
		if (!this.isRegex) {
			return this;
		}
		String value = regexTV.getText().toString();
		if (max < min) {
			if (null == value) {
				this.isRegex = false;
			}
			else if (value.length() >= min) {
				this.isRegex = true;
			}
			else {
				this.isRegex = false;
			}
		}
		else {
			if (null == value) {
				this.isRegex = false;
			}
			else if (value.length() >= min && value.length() <= max) {
				this.isRegex = true;
			}
			else {
				this.isRegex = false;
			}

		}
		doErrorInfo(regexTV, value, errorInfo, emptyInfo);
		return this;
	}

	private void doErrorInfo(TextView regexTV, String vaule, String errorInfo, String emptyInfo) {
		if (!this.isRegex) {
			if (null != regexCallBack) {
				if (TextUtils.isEmpty(vaule)) {
					if (TextUtils.isEmpty(emptyInfo)) {
						regexCallBack.errorRegex(regexTV, vaule, errorInfo);
					}
					else {
						regexCallBack.errorRegex(regexTV, vaule, emptyInfo);
					}
				}
				else {
					regexCallBack.errorRegex(regexTV, vaule, errorInfo);
				}

			}
		}
	}

	public boolean builder() {
		return this.isRegex;
	}

}
