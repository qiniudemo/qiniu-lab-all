<?php
require_once("../bootstrap.php");
require_once("../utils.php");
use Qiniu\Storage;

$auth = new \Qiniu\Auth($QINIU_ACCESS_KEY, $QINIU_SECRET_KEY);
$file_path = "/Users/jemy/Documents/qiniu.mp4";
//视频切片
//256kb,480x360
$m_256k_480x360_bucket = $QINIU_PUBLIC_BUCKET;
$m_256k_480x360_key = "qiniu_256k_480x360.m3u8";
$m_256k_480x360_fop = "avthumb/m3u8/vb/256k/s/480x360|saveas/" .
    \Qiniu\base64_urlSafeEncode($m_256k_480x360_bucket . ":" . $m_256k_480x360_key);

//256k,480x270
$m_256k_480x270_bucket = $QINIU_PUBLIC_BUCKET;
$m_256k_480x270_key = "qiniu_256k_480x270.m3u8";
$m_256k_480x270_fop = "avthumb/m3u8/vb/256k/s/480x270|saveas/" .
    \Qiniu\base64_urlSafeEncode($m_256k_480x270_bucket . ":" . $m_256k_480x270_key);

$persistentOps = $m_256k_480x360_fop . ";" . $m_256k_480x270_fop;
//私有队列名称，没有去七牛后台创建
$persistentPipeline = "jemy";
//构建上传策略
$putPolicy = array(
    'persistentOps' => $persistentOps,
    'persistentPipeline' => $persistentPipeline,
);
//上传凭证有效期，单位秒
$expires = 3600;
$up_token = $auth->uploadToken($QINIU_PUBLIC_BUCKET, $key, $expires, $putPolicy);
$upload_manager = new \Qiniu\Storage\UploadManager();
//存储在七牛空间的文件名
$key = "demo/qiniu.mp4";

try {
    list($ret_data, $error) = $upload_manager->putFile($up_token, $key, $file_path);
    print_upload_result($ret_data, $error);
} catch (Exception $e) {
    print($e->getMessage());
}