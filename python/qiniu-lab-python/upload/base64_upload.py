#coding=utf8
__author__ = 'jemy'
import qiniu
import base64
from qiniu import http

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

b64_data = "aGVsbG8gd29ybGQK"
fsize = len(base64.decodestring(b64_data))

bucket = "if-pbl"
key = "2015/06/10/test.txt"
mime_type = "text/plain"

auth = qiniu.Auth(accessKey, secretKey)
up_token = auth.upload_token(bucket, key)

post_url = "http://upload.qiniu.com/putb64/{0}/key/{1}/mimeType/{2}" \
    .format(fsize, qiniu.urlsafe_base64_encode(key), qiniu.urlsafe_base64_encode(mime_type))

token_auth = http._TokenAuth(up_token)
ret, respInfo = http._post(post_url, b64_data, None, token_auth)
if respInfo.status_code == 200:
    print("upload success")
else:
    print(respInfo.error)
