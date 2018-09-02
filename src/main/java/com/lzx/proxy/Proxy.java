package com.lzx.proxy;

import java.io.Serializable;

public class Proxy implements Serializable{
	private static final long serialVersionUID=-5809782578272943999L;
	
	public String IP;
	public String port;
	public String isAnno;//透明,匿名,混淆,高匿
	public String protocol;//http https
	public String country;//代理IP所在国家
	public String city;//未知为XXXX
	public String operator;//运营商
	public String responseTime;//响应时间，单位为s
	public String lastCheckTime;//最后验证时间
	public Proxy(String iP, String port, String isAnno, String protocol, String country, String city, String operator,
			String responseTime, String lastCheckTime) {
		IP = iP;
		this.port = port;
		this.isAnno = isAnno;
		this.protocol = protocol;
		this.country = country;
		this.city = city;
		this.operator = operator;
		this.responseTime = responseTime;
		this.lastCheckTime = lastCheckTime;
	}
	@Override
	public String toString() {
		return "Proxy [IP=" + IP + ", port=" + port + ", isAnno=" + isAnno + ", protocol=" + protocol + ", country="
				+ country + ", city=" + city + ", operator=" + operator + ", responseTime=" + responseTime
				+ ", lastCheckTime=" + lastCheckTime + "]";
	}
}
