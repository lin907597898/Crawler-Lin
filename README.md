# LinCrawler
第一个版本爬虫部分还有些问题没修复。可以用用布隆过滤器。

导入jar后。

    	BoolmFilter.setFilePath("布隆过滤器持久化地址txt形式");
		BoolmFilter boolmFilter=BoolmFilter.getBoolmFilter();//默认为100000条样本量，万分之一失误率
		/*自定义布隆过滤器，请在初始化时使用
		 * BoolmFilter boolmFilter2=BoolmFilter.getBoolmFilter(1000000,0.0001)
		 * 参数为样本量和预期失误率
		 */
		
		//添加样本
		boolmFilter.addItem("url1");
		boolmFilter.addItem("url2");
		//验证样本
		boolmFilter.isExist("url1");
		//序列化
		boolmFilter.SerializeBoolmFilter();
		//反序列化
		boolmFilter.DeSerializeBoolmFilter();
    
百度云下载jar链接：https://pan.baidu.com/s/1BVikpx_tj6f_5fphuftQog 密码：t3jb
