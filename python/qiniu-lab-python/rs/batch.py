#coding=utf-8
__author__ = 'jemy'

'''
本例子演示如何使用batch接口来批量地执行stat，copy，move，delete，chgm操作
文档：http://developer.qiniu.com/docs/v6/api/reference/rs/batch.html

PS：batch接口支持单独指令集的组合，比如都是stat指令，或者是不同指令集的集合，
比如既有stat，也有copy，还有move，delete，chgm等。支持的命令就是stat，copy，
move，delete和chgm，它们之间可以自由组合成batch的指令集。

该例子使用了python sdk里面的方法来构建batch指令集，你也可以使用sdk里面提供的
batch方法。之所以这里自己构建是为了演示batch支持不同空间的处理指令的组合。
'''

import qiniu
import sys
import time
import json

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
批量获取空间中文件的基本信息，提供的参数文件格式如下：
bucket\tkey
bucket\tkey
bucket\tkey
...
文件编码为utf-8

比如：
if-pbl	bdlog.png
if-pbl	mp444.mp4
if-pbl	mm.txt
qiniu-lab   vframe_02.jpg
qiniu	x.txt
'''


def batch_stat(paramFile):
    batchOps = []
    fp = open(paramFile, mode="r")
    for line in fp:
        items = line.split("\t")
        if len(items) == 2:
            bucketPart = items[0].strip()
            keyPart = items[1].strip()
            encodedEntry = qiniu.entry(bucketPart, keyPart)
            batchOps.append("/stat/" + encodedEntry)
        else:
            print("Invalid stat line `" + line.strip() + "'")
    fp.close()
    auth = qiniu.Auth(accessKey, secretKey)
    bm = qiniu.BucketManager(auth)
    _, respInfo = bm.batch(batchOps)
    if respInfo.status_code == 401:
        print("Unauthorized error, please set your access key and secret key")
    elif respInfo.status_code == 200 or respInfo.status_code == 298:
        textBody = respInfo.text_body
        statDataList = json.loads(textBody)
        for statData in statDataList:
            print("Code: " + str(statData["code"]))
            fileInfo = statData["data"]
            if fileInfo.__contains__("error"):
                print("Error: " + fileInfo["error"])
            else:
                print("Stat Result:")
                print("  File Size:\t" + str(fileInfo["fsize"]) + " Bytes")
                print("  Hash:\t\t" + fileInfo["hash"])
                print("  MimeType:\t" + fileInfo["mimeType"])
                print("  PutTime:\t" + time.ctime(fileInfo["putTime"] / 10000000))
            print("---------------------------------------------------------------")
    else:
        print(respInfo)


def batch_copy(paramFile):
    pass


def batch_move(paramFile):
    pass


def batch_delete(paramFile):
    pass


def batch_chgm(paramFile):
    pass


def batch_mix_operations(paramFile):
    pass


def main():
    argv = sys.argv
    argc = len(sys.argv)
    if argc == 3:
        cmd = argv[1]
        paramFile = argv[2]
        if cmd == "stat":
            batch_stat(paramFile)
        elif cmd == "copy":
            batch_copy(paramFile)
        elif cmd == "move":
            batch_move(paramFile)
        elif cmd == "delete":
            batch_delete(paramFile)
        elif cmd == "chgm":
            batch_chgm(paramFile)
        elif cmd == "mix":
            batch_mix_operations(paramFile)
        else:
            print("Unsupported cmd `" + cmd + "'")
    else:
        cmd_help()


def cmd_help():
    print '''Usage: Batch Operation
  batch.py stat param_file
  batch.py copy param_file
  batch.py move param_file
  batch.py delete param_file
  batch.py chgm param_file
  batch.py mix mix_param_file
'''


if __name__ == "__main__":
    main()
