#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用stat接口获取文件的基本信息
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/stat.html
'''

import qiniu
import sys
import time

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
获取单个文件的基本信息

bucket 空间名
key 文件名
'''


def stat(bucket, key):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.stat(bucket, key)
    if retData != None:
        print("Stat Result:")
        print("  File Size:\t" + str(retData["fsize"]) + " Bytes")
        print("  Hash:\t\t" + retData["hash"])
        print("  MimeType:\t" + retData["mimeType"])
        print("  PutTime:\t" + time.ctime(retData["putTime"] / 10000000))
    else:
        if respInfo.status_code == 401:
            print("Unauthorized error, please set your access key and secret key")
        else:
            print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 3:
        bucket = sys.argv[1]
        key = sys.argv[2]
        stat(bucket, key)
    else:
        print("Usage: stat.py bucket key")


if __name__ == "__main__":
    main()