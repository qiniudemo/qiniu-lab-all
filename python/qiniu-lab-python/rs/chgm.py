#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用chgm接口修改文件的MimeType
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/chgm.html
'''

import qiniu
import sys

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
修改文件的MimeType

bucket 空间名
key 文件名
newMimeType 文件的新mimeType
'''


def change_mimetype(bucket, key, newMimeType):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.change_mime(bucket, key, newMimeType)
    if respInfo.status_code == 200:
        print("Change mime type success!")
    elif respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    else:
        print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 4:
        bucket = sys.argv[1]
        key = sys.argv[2]
        newMimeType = sys.argv[3]
        change_mimetype(bucket, key, newMimeType)
    else:
        print("Usage: chgm.py bucket key newMimeType")


if __name__ == "__main__":
    main()