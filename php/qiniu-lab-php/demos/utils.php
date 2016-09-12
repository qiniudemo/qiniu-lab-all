<?php
function print_upload_result($ret_data, $error)
{
    if ($error != null) {
        print_r($error);
    } else {
        print_r($ret_data);
    }
}