#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用prefetch接口来从源站更新资源到七牛镜像空间
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/prefetch.html
'''

import qiniu
import sys

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
更新七牛空间里面的镜像资源

bucket 镜像空间名称
key 镜像空间中的文件名
'''

def prefetch(bucket, key):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.prefetch(bucket, key)
    if respInfo.status_code == 200:
        print("Prefetch success!")
    elif respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    else:
        print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 3:
        bucket = sys.argv[1]
        key = sys.argv[2]
        prefetch(bucket, key)
    else:
        print("Usage: prefetch.py bucket key")


if __name__ == "__main__":
    main()