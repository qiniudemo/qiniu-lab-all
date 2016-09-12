<?php
require_once("../bootstrap.php");

use Qiniu\Auth;
use Qiniu\Http\Client;

/*
@param urls - Array, urls to refresh
@param dirs - Array, dirs to refresh
 */
function CdnRefresh($auth, $urls, $dirs)
{
    $refreshUrl = "http://fusion.qiniuapi.com/refresh";
    $refreshBody = json_encode(array(
        "urls" => $urls,
        "dirs" => $dirs,
    ));

    $headers = $auth->authorization($refreshUrl);
    $headers["Content-Type"] = "application/json";
    $refreshResult = Client::post($refreshUrl, $refreshBody, $headers);
    return $refreshResult;
}

//call example
// ak,sk从 https://portal.qiniu.com/user/key 获取
$accessKey = '';
$secretKey = '';
$auth = new Auth($accessKey, $secretKey);
$urls = array(
    "http://if-pbl.qiniudn.com/qiniu.mp4",
);
/*$dirs=array(
    "http://if-pbl.qiniudn.com/",
);*/
$dirs = null;

$refreshResult = CdnRefresh($auth, $urls, $dirs);
print_r($refreshResult);

/*
Qiniu\Http\Response Object
(
    [statusCode] => 200
    [headers] => Array
        (
            [Server] => nginx
            [Date] => Tue, 12 Jul 2016 04
            [Content-Type] => application/json
            [Content-Length] => 197
            [Connection] => keep-alive
            [X-Log] => fusiondomain;fusionrefresh_v3
            [X-Reqid] => nQkAAALWUHW6cGAU
        )

    [body] => {"code":400037,"error":"url has existed","requestId":"","invalidUrls":["http://if-pbl.qiniudn.com/qiniu.mp4"],"invalidDirs":null,"urlQuotaDay":0,"urlSurplusDay":0,"dirQuotaDay":0,"dirSurplusDay":0}
    [error] =>
    [jsonData:Qiniu\Http\Response:private] => Array
        (
            [code] => 400037
            [error] => url has existed
            [requestId] =>
            [invalidUrls] => Array
                (
                    [0] => http://if-pbl.qiniudn.com/qiniu.mp4
                )

            [invalidDirs] =>
            [urlQuotaDay] => 0
            [urlSurplusDay] => 0
            [dirQuotaDay] => 0
            [dirSurplusDay] => 0
        )

    [duration] => 0.038
)
*/