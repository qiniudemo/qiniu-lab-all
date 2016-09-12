#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用fetch接口抓取网上资源并保存到空间中，目前不支持https的资源
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/fetch.html
'''

import qiniu
import sys

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
抓取网上资源并保存到空间中

remoteUrl 网上资源链接
bucket 保存的空间
key 保存的文件名
'''


def fetch(remoteUrl, bucket, key):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.fetch(remoteUrl, bucket, key)
    if respInfo.status_code == 200:
        print("Fetch success!")
    elif respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    else:
        print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 4:
        remoteUrl = sys.argv[1]
        bucket = sys.argv[2]
        key = sys.argv[3]
        fetch(remoteUrl, bucket, key)
    else:
        print("Usage: fetch.py remoteUrl bucket key")


if __name__ == "__main__":
    main()