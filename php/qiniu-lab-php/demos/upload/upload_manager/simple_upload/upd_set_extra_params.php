<?php
require_once("../../../bootstrap.php");
require_once("../../../utils.php");
use Qiniu\Storage;

//简单无key数据上传，设定扩展参数

$data = "最牛, 七牛云存储!";
$auth = new \Qiniu\Auth($QINIU_ACCESS_KEY, $QINIU_SECRET_KEY);
$up_token = $auth->uploadToken($QINIU_PUBLIC_BUCKET);
$upload_manager = new \Qiniu\Storage\UploadManager();
$key = null;
//$key="no1_qiniu_cloud_storage.txt";

$extra_params = array(
    "x:device" => "pc",
    "x:date" => "2015/02/14",
    "x:name" => "jemy",
    "x:empty" => "",//值为空，将被忽略
    "hobby" => "programming" //名称不符合规范，将被忽略
);

try {
    list($ret_data, $error) = $upload_manager->put($up_token, $key, $data, $extra_params);
    print_upload_result($ret_data, $error);
} catch (Exception $e) {
    print($e->getMessage());
}
