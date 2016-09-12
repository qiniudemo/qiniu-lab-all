<?php
require_once("../bootstrap.php");
$data="hello world";
echo \Qiniu\base64_urlSafeEncode($data);