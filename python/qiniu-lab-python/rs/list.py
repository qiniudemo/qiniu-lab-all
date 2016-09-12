#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用list接口来列出一个空间中的所有文件列表

一般列出文件列表的时候，可以给一个前缀，这样可以有效地过滤掉不想要的文件名
'''

import qiniu

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"


def list(bucket, prefix, output):
    auth = qiniu.Auth(accessKey, secretKey)
    bkm = qiniu.BucketManager(auth)
    eof = False
    marker = None
    fp = open(output, "w")
    while not eof:
        ret, eof, info = bkm.list(bucket, prefix=prefix, marker=marker, limit=None, delimiter=None)
        if ret and not eof:
            marker = ret.get("marker")
        if ret:
            items = ret.get("items")
            for item in items:
                key = item["key"]
                putTime = item["putTime"]
                hash = item["hash"]
                fsize = item["fsize"]
                mimeType = item["mimeType"]
                data = "{0}\t{1}\t{2}\t{3}\t{4}".format(key.encode("utf-8"), fsize, putTime, mimeType, hash)
                fp.write(data)
                fp.write("\r\n")
    fp.flush()
    fp.close()


def main():
    bucket = "ktvdaren"
    prefix = "upload_331_335/"
    output = "leike_ktvdaren.txt"
    list(bucket, prefix, output)


if __name__ == "__main__":
    main()
