<?php
error_reporting(E_ERROR);
function __autoload($class_name)
{
    if (strstr($class_name, "Qiniu") === false) {
        return;
    }
    $class_name = str_replace("\\", "/", $class_name);
    require_once __DIR__ . "/../library/" . $class_name . ".php";
}

require_once __DIR__ . "/../library/Qiniu/functions.php";

$QINIU_ACCESS_KEY = "<ak>";
$QINIU_SECRET_KEY = "<sk>";
$QINIU_PUBLIC_BUCKET = "if-pbl";
$QINIU_PRIVATE_BUCKET = "if-pri";
