var page = require('webpage').create(), address;
/* ����ץȡҳ���ַ */
address = "https://jarod2011.github.io/phantomjs-spider/";
/* ��ʼץȡ */
page.open(address, function (status) {
  if (status != 'success') {
    /* δ�ɹ���ȡҳ������ */
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