#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用copy接口复制文件，可以复制到不同的空间
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/copy.html
'''

import qiniu
import sys

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
复制文件

srcBucket 源空间名
srcKey 源文件名
destBucket 目的空间名
destKey 目的文件名
'''

def copy(srcBucket, srcKey, destBucket, destKey):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.copy(srcBucket, srcKey, destBucket, destKey)
    if respInfo.status_code == 200:
        print("Copy success!")
    elif respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    else:
        print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 5:
        srcBucket = sys.argv[1]
        srcKey = sys.argv[2]
        destBucket = sys.argv[3]
        destKey = sys.argv[4]
        copy(srcBucket, srcKey, destBucket, destKey)
    else:
        print("Usage: copy.py srcBucket srcKey destBucket destKey")


if __name__ == "__main__":
    main()