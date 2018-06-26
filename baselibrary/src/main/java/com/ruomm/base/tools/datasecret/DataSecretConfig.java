/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月18日 下午12:28:36 
 */
package com.ruomm.base.tools.datasecret;

/**
 * Base64和Des多重加密解密算法密钥配置
 * 
 * @author Ruby
 */
class DataSecretConfig {

	/**
	 * BASE64密钥：加密和解密的密钥，64个字符(a-zA-z0-9+/)每一个都有唯一一个，字典内顺序随意排列；
	 */
	public static final String SecretKeyForBase64 = "rvzAlgwxyKhiEVjk67stuJLm+/aWXbnYMefPQR01SOUTq45D89opdCFG23BHcINZ";
	/**
	 * DES密钥：加密和解密密钥，8位数字随意排列
	 */
	public static final String SecretKeyForDes = "12009911";
}
