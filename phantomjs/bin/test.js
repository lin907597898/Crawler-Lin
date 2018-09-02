var page = require('webpage').create(), address;
/* 定义抓取页面地址 */
address = "https://jarod2011.github.io/phantomjs-spider/";
/* 开始抓取 */
page.open(address, function (status) {
  if (status != 'success') {
    /* 未成功获取页面内容 */
    console.log('Fail to load [' + address + '] with status ' + status);
    phantom.exit(1);
} else {
    var body = page.evaluate(function () {
      return document.body;
    });
    console.log(body.innerHTML);
    phantom.exit(1);
  }
});