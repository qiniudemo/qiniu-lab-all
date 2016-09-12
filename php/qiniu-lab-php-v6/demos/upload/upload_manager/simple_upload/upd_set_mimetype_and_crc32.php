<?php
require_once("../../../bootstrap.php");
require_once("../../../utils.php");
use Qiniu\Storage;

//简单无key数据上传，设定mimeType和crc32校验

$data = "新年快乐, 七牛云存储!";
$auth = new \Qiniu\Auth($QINIU_ACCESS_KEY, $QINIU_SECRET_KEY);
$up_token = $auth->uploadToken($QINIU_PUBLIC_BUCKET);
$upload_manager = new \Qiniu\Storage\UploadManager();

$key = null;
//$key="happy_new_year_qiniu_cloud_storage.txt";

try {
    list($ret_data, $error) = $upload_manager->put($up_token, $key, $data, null, "text/plain", true);
    print_upload_result($ret_data, $error);
} catch (Exception $e) {
    print($e->getMessage());
}
