#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用delete接口删除文件，记得没有回收站哦！
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/delete.html
'''

import qiniu
import sys

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
删除文件

bucket 空间名
key 文件名
'''


def delete(bucket, key):
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    retData, respInfo = bm.delete(bucket, key)
    if respInfo.status_code == 200:
        print("Delete success!")
    elif respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    else:
        print("Error: " + respInfo.error)


def main():
    if len(sys.argv) == 3:
        bucket = sys.argv[1]
        key = sys.argv[2]
        delete(bucket, key)
    else:
        print("Usage: delete.py bucket key")


if __name__ == "__main__":
    main()