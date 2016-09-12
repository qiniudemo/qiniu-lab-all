<?php
require_once("../bootstrap.php");

use Qiniu\Auth;
use Qiniu\Http\Client;

function GetDomainBandwidth($auth, $domain, $startTime, $endTime)
{
    $bandwidthUrl = sprintf("http://fusion.qiniuapi.com/domain/bandwidth?domain=%s&startTime=%s&endTime=%s",
        $domain, urlencode($startTime), urlencode($endTime));

    $headers = $auth->authorization($bandwidthUrl);
    $bandwidthData = Client::get($bandwidthUrl, $headers);
    return $bandwidthData;
}

//call example
// ak,sk从 https://portal.qiniu.com/user/key 获取
$accessKey = '';
$secretKey = '';
$auth = new Auth($accessKey, $secretKey);
$domain = "if-pbl.qiniudn.com";
$startTime = "2016-07-11 12:00:00";
$endTime = "2016-07-11 13:00:00";

//$bandwidthData is an object
$bandwidthData = GetDomainBandwidth($auth, $domain, $startTime, $endTime);
//print_r($bandwidthData);
print_r($bandwidthData->json());
print_r($bandwidthData->statusCode);

/*
Array
(
    [code] => 200
    [error] => success
    [data] => Array
        (
            [0] => Array
                (
                    [time] => 1468209600
                    [bandwidth] => 0
                )

            [1] => Array
                (
                    [time] => 1468209900
                    [bandwidth] => 0
                )

            [2] => Array
                (
                    [time] => 1468210200
                    [bandwidth] => 0
                )

            [3] => Array
                (
                    [time] => 1468210500
                    [bandwidth] => 0
                )

            [4] => Array
                (
                    [time] => 1468210800
                    [bandwidth] => 0
                )

            [5] => Array
                (
                    [time] => 1468211100
                    [bandwidth] => 0
                )

            [6] => Array
                (
                    [time] => 1468211400
                    [bandwidth] => 0
                )

            [7] => Array
                (
                    [time] => 1468211700
                    [bandwidth] => 0
                )

            [8] => Array
                (
                    [time] => 1468212000
                    [bandwidth] => 0
                )

            [9] => Array
                (
                    [time] => 1468212300
                    [bandwidth] => 0
                )

            [10] => Array
                (
                    [time] => 1468212600
                    [bandwidth] => 0
                )

            [11] => Array
                (
                    [time] => 1468212900
                    [bandwidth] => 24
                )

        )

)
*/