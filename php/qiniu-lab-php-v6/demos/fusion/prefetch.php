<?php
require_once("../bootstrap.php");

use Qiniu\Auth;
use Qiniu\Http\Client;

/*
@param urls - Array, urls to prefetch
prefetch 不支持预取目录
 */
function CdnPrefetch($auth, $urls, $dirs)
{
    $prefetchUrl = "http://fusion.qiniuapi.com/prefetch";
    $prefetchBody = json_encode(array(
        "urls" => $urls,
    ));

    $headers = $auth->authorization($prefetchUrl);
    $headers["Content-Type"] = "application/json";
    $prefetchResult = Client::post($prefetchUrl, $prefetchBody, $headers);
    return $prefetchResult;
}

//call example
// ak,sk从 https://portal.qiniu.com/user/key 获取
$accessKey = '';
$secretKey = '';
$auth = new Auth($accessKey, $secretKey);
$urls = array(
    "http://if-pbl.qiniudn.com/qiniu.mp4",
);

$prefetchResult = CdnPrefetch($auth, $urls, $dirs);
print_r($prefetchResult);

/*
Qiniu\Http\Response Object
(
    [statusCode] => 200
    [headers] => Array
        (
            [Server] => nginx
            [Date] => Tue, 12 Jul 2016 04
            [Content-Type] => application/json
            [Content-Length] => 119
            [Connection] => keep-alive
            [X-Log] => fusiondomain
            [X-Reqid] => nQkAAOGdRREEcWAU
        )

    [body] => {"code":200,"error":"success","requestId":"5784720ce3ab3a099d068606","invalidUrls":null,"quotaDay":100,"surplusDay":99}
    [error] =>
    [jsonData:Qiniu\Http\Response:private] => Array
        (
            [code] => 200
            [error] => success
            [requestId] => 5784720ce3ab3a099d068606
            [invalidUrls] =>
            [quotaDay] => 100
            [surplusDay] => 99
        )

    [duration] => 0.063
)
*/