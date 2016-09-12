<?php
require_once("../../../bootstrap.php");
require_once("../../../utils.php");
use Qiniu\Storage;

//简单无key文件上传，设定mimeType和crc32校验

$auth = new \Qiniu\Auth($QINIU_ACCESS_KEY, $QINIU_SECRET_KEY);
//小文件，触发表单上传
$file_path = "/Users/jemy/Documents/qiniu.png";
$up_token = $auth->uploadToken($QINIU_PUBLIC_BUCKET);
$upload_manager = new \Qiniu\Storage\UploadManager();
$key = null;

try {
    list($ret_data, $error) = $upload_manager->putFile($up_token, $key, $file_path, null, "image/png", true);
    print_upload_result($ret_data, $error);
} catch (Exception $e) {
    print($e->getMessage());
}
