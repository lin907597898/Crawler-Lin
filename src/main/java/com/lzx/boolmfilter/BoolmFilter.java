package com.lzx.boolmfilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lzx.proxy.Proxy;
import com.lzx.simple.utils.HashUtil;

/*
 * 双重检测机制(DCL)懒汉式单例模式
 * 
 * 局限性：布隆过滤器比特位数不能超过Int最大值的64倍。
 * 其实这也不算是局限性，如果超过，占用容量最少16G
 */
public class BoolmFilter implements Serializable{
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -8597039180690177072L;
	@Override
	public String toString() {
		return boolm_arr.toString();
	}

	private static Logger logger = LogManager.getLogger(BoolmFilter.class);
	private static volatile BoolmFilter boolmFilter = null;
	private static double expected_error_rate;// 预期失误率
	private static double sample_number;// 样本量
	private static long bite_number;// 布隆过滤器比特位数
	private static int hashmethod_number;// 哈希函数个数
	private static double true_error_rate;// 真实失误率
	private static long[] boolm_arr;// 布隆容器
	
	private static String FilePath=".\\doc\\boolmfilter.txt";

	private BoolmFilter() {
		this(100000, 0.0001);
	}

	private BoolmFilter(double sample, double expected_error) {
		sample_number = sample;
		expected_error_rate = expected_error;
		bite_number = (int) Math.ceil(-1 * (sample_number / Math.log(expected_error_rate) / Math.pow(Math.log(2), 2)));
		bite_number = bite_number % 64 != 0 ? (64 * (bite_number / 64) + 64) : bite_number;
		hashmethod_number = (int) Math.ceil(Math.log(2) * bite_number / sample_number);
		true_error_rate = Math.pow(1 - Math.pow(Math.E, -sample_number * hashmethod_number / bite_number),
				hashmethod_number);
		boolm_arr = new long[(int) (bite_number / 64)];
	}

	public static BoolmFilter getBoolmFilter() {
		if (boolmFilter == null) {
			synchronized (BoolmFilter.class) {
				if (boolmFilter == null) {
					boolmFilter = new BoolmFilter();
				}
			}
		}
		return boolmFilter;
	}

	public static BoolmFilter getBoolmFilter(double sample, double expected_error) {
		if (boolmFilter == null) {
			synchronized (BoolmFilter.class) {
				if (boolmFilter == null) {
					if (justTest(sample, expected_error) * 8 > Integer.MAX_VALUE) {
						logger.warn("布隆过滤器容量过大，暂不支持");
						return null;
					}
					boolmFilter = new BoolmFilter(sample, expected_error);
				}
			}
		}
		return boolmFilter;
	}

	/*
	 * 计算布隆过滤器最少占用空间
	 */
	public static long justTest(double sample, double expected_error) {
		long test_bite_number = (int) Math.ceil(-1 * (sample / Math.log(expected_error) / Math.pow(Math.log(2), 2)));
		test_bite_number = test_bite_number % 64 != 0 ? (64 * (test_bite_number / 64) + 64) : test_bite_number;
		return test_bite_number / 8;
	}

	/*
	 * 将一条字符串记录加入布隆过滤器
	 */
	public static void addItem(String url) {
		BigInteger hashnumber;
		for (int i = 1; i <= hashmethod_number; i++) {
			hashnumber = HashUtil.getMD5_BIGINT(url)
					.add(HashUtil.getSHA1_BIGINT(url).multiply(new BigInteger(String.valueOf(i))));
			// hashnumber=HashUtil.getMD5_BIGINT(url)+i*HashUtil.getSHA1_BOGINT(url);
			hashnumber = hashnumber.remainder(new BigInteger(String.valueOf(bite_number)));

			setBoolmByte(Integer.valueOf(hashnumber.toString()));
		}
	}

	/*
	 * 改变布隆过滤器byte位
	 */
	private static void setBoolmByte(int number) {
		int longIndex = number / 64;
		int biteIndex = number % 64;
		boolm_arr[longIndex] = (boolm_arr[longIndex] | (1 << biteIndex));
	}

	/*
	 * 判断字符串是否存在于布隆过滤器中
	 */
	public static boolean isExist(String url) {
		if (url == null || url.trim().equals("")) {
			return false;
		}
		BigInteger hashnumber;
		for (int i = 1; i <= hashmethod_number; i++) {
			hashnumber = HashUtil.getMD5_BIGINT(url)
					.add(HashUtil.getSHA1_BIGINT(url).multiply(new BigInteger(String.valueOf(i))));
			// hashnumber=HashUtil.getMD5_BIGINT(url)+i*HashUtil.getSHA1_BOGINT(url);
			hashnumber = hashnumber.remainder(new BigInteger(String.valueOf(bite_number)));
			if (!CheckBoolmByte(Integer.valueOf(hashnumber.toString()))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * 检查布隆过滤器byte位
	 */
	private static boolean CheckBoolmByte(int index) {
		int longIndex = index / 64;
		int biteIndex = index % 64;
		return ((boolm_arr[longIndex] | (1 << biteIndex)) == boolm_arr[longIndex]) ? true : false;
	}
	
	
	public static void SerializeBoolmFilter(){
		ObjectOutputStream objectOutputStream=null;
		try {
			objectOutputStream=new ObjectOutputStream(new FileOutputStream(new File(FilePath)));
			objectOutputStream.writeObject(BoolmFilter.getBoolmFilter());
		} catch (FileNotFoundException e) {
			logger.error("文件未找到:"+e);
		} catch (IOException e) {
			logger.error("输出流错误:"+e);
		}finally {
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				logger.error("输出流未正常关闭:"+e);
			}
		}
	}
	
	public static BoolmFilter DeSerializeBoolmFilter(){
		ObjectInputStream objectInputStream=null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(new File(FilePath)));
		} catch (FileNotFoundException e1) {
			logger.error("文件未找到"+e1);
		} catch (IOException e1) {
			logger.error("输入流异常:"+e1);
		}
		try {
			BoolmFilter boolmFilter=(BoolmFilter) objectInputStream.readObject();
			return boolmFilter;
		} catch (ClassNotFoundException e) {
			logger.error("类型转换异常:"+e);
		} catch (IOException e) {
			logger.error("输入流异常:"+e);
		}
		return null;
	}
	
	public static String getFilePath() {
		return FilePath;
	}

	public static void setFilePath(String filePath) {
		FilePath = filePath;
	}
}
