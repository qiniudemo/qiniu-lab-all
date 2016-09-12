<?php

require_once("../bootstrap.php");

use Qiniu\Processing\PersistentFop;

$auth = new \Qiniu\Auth($QINIU_ACCESS_KEY, $QINIU_SECRET_KEY);

//原始待处理文件
$bucket = "if-pbl";
$key = "qiniu.jpg";

//结果保存
$saveBucket = "if-pri";
$saveKey = "qiniu_w100.jpg";

$pfop = new PersistentFop($auth, $bucket);
$fops = "imageView2/0/w/100|saveas/" . \Qiniu\base64_urlSafeEncode($saveBucket . ":" . $saveKey);

list($persistentId, $err) = $pfop->execute($key, $fops);

echo "\n====> pfop result: \n";
if ($err != null) {
    var_dump($err);
} else {
    echo "PersistentFop Id: $persistentId";
}

