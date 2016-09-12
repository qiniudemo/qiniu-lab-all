<?php
require_once("../bootstrap.php");

use Qiniu\Auth;
use Qiniu\Http\Client;

function GetDomainTraffic($auth, $domain, $startTime, $endTime)
{
    $trafficUrl = sprintf("http://fusion.qiniuapi.com/domain/traffic?domain=%s&startTime=%s&endTime=%s",
        $domain, urlencode($startTime), urlencode($endTime));

    $headers = $auth->authorization($trafficUrl);
    $trafficData = Client::get($trafficUrl, $headers);
    return $trafficData;
}

//call example
// ak,sk从 https://portal.qiniu.com/user/key 获取
$accessKey = '';
$secretKey = '';
$auth = new Auth($accessKey, $secretKey);
$domain = "if-pbl.qiniudn.com";
$startTime = "2016-07-11 12:00:00";
$endTime = "2016-07-11 13:00:00";

//$trafficData is an object
$trafficData = GetDomainTraffic($auth, $domain, $startTime, $endTime);
//print_r($trafficData);
print_r($trafficData->json());
print_r($trafficData->statusCode);

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
                    [flow] => 0
                )

            [1] => Array
                (
                    [time] => 1468209900
                    [flow] => 0
                )

            [2] => Array
                (
                    [time] => 1468210200
                    [flow] => 0
                )

            [3] => Array
                (
                    [time] => 1468210500
                    [flow] => 0
                )

            [4] => Array
                (
                    [time] => 1468210800
                    [flow] => 0
                )

            [5] => Array
                (
                    [time] => 1468211100
                    [flow] => 0
                )

            [6] => Array
                (
                    [time] => 1468211400
                    [flow] => 0
                )

            [7] => Array
                (
                    [time] => 1468211700
                    [flow] => 0
                )

            [8] => Array
                (
                    [time] => 1468212000
                    [flow] => 0
                )

            [9] => Array
                (
                    [time] => 1468212300
                    [flow] => 0
                )

            [10] => Array
                (
                    [time] => 1468212600
                    [flow] => 0
                )

            [11] => Array
                (
                    [time] => 1468212900
                    [flow] => 900
                )

        )

)
*/