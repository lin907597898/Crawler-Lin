var page = require('webpage').create(),  
  system = require('system'),  
  t, address;  
//д���ļ����������ԡ���ʽ�汾����ע�͵���������ٶȡ�  
var fs = require("fs");  
//��ȡ�����в�����Ҳ����js�ļ�·����  
if (system.args.length === 1) {  
  console.log('Usage: loadspeed.js <some URL>');  
//���д������Ҫ�����ǽ���������á�����phantomjs����ֹͣ  
  phantom.exit();  
}  
page.settings.loadImages = false;  //Ϊ�����������ٶȣ�������ͼƬ  
page.settings.resourceTimeout = 10000;//����10���������  
//�˴����������ý�ͼ�Ĳ���������ͼûɶ��  
page.viewportSize = {  
  width: 1280,  
  height: 800  
};  
block_urls = ['baidu.com'];//Ϊ�������ٶȣ�����һЩ��Ҫʱ�䳤�ġ�����ٶȹ��  
page.onResourceRequested = function(requestData, request){  
    for(url in block_urls) {  
        if(requestData.url.indexOf(block_urls[url]) !== -1) {  
            request.abort();  
            //console.log(requestData.url + " aborted");  
            return;  
        }  
    }              
}  
t = Date.now();//����������Ҫ��á�  
address = system.args[1];  
page.open(address, function(status) {  
  if (status !== 'success') {  
    console.log('FAIL to load the address');  
  } else {  
    t = Date.now() - t;  
//�˴�ԭ����Ϊ����ȡ��Ӧ��Ԫ�ء�ֻҪ������document�ģ����ǿ������á������Լ����޷���document��ֻ�������ַ��ָ���java�  
    //  var ua = page.evaluate(function() {  
    //   return document.getElementById('companyServiceMod').innerHTML;  
        
    // });  
    // fs.write("qq.html", ua, 'w');  
   // console.log("����qq: "+ua);    
//console.log���Ǵ����ȥ�����ݡ�  
    console.log('Loading time ' + t + ' msec');  
    console.log(page.content);  
    setTimeout(function(){ phantom.exit(); }, 6000);  
  }  
  phantom.exit();  
});  