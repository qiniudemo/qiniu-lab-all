#coding=utf-8
__author__ = 'jemy'
import qiniu

'''
该例子演示如何在对视频转码的同时对转码后的视频做流媒体切片
详细文档：http://developer.qiniu.com/docs/v6/api/reference/fop/av/segtime.html
'''

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"


def avthumb_m3u8(type, dstFormat):
    #准备命令
    cmd = "avthumb/m3u8"

    if type == "wifi":
        segtime = "10"
        videoBitRate = "440k"
    elif type == "3g":
        segtime = "5"
        videoBitRate = "240k"

    resolution = "400x244"
    cmdParams = {
        "segtime": segtime,
        "vb": videoBitRate,
        "s": resolution,
        "vodec": "libx264",
    }

    #结果保存空间名称
    saveBucket = "qiniu-lab"
    if type == "wifi":
        saveKey = u"七牛云存储视频名片_wifi.m3u8".encode("utf-8")
    elif type == "3g":
        saveKey = u"七牛云存储视频名片_3g.m3u8".encode("utf-8")

    #构建fop指令
    fop = qiniu.build_op(cmd, dstFormat, **cmdParams)
    #指定保存的空间和key，否则将以结果的hash值作为文件名保存
    fop = qiniu.op_save(fop, saveBucket, saveKey)

    #执行fop操作
    fromBucket = "qiniu-lab"
    fromKey = u"七牛云存储视频名片.mp4".encode("utf-8")
    #默认为空，表示使用公共队列
    usePipeline = ""
    #私有队列，根据你实际情况设置
    usePipeline = "fff"
    #fop处理结果通知url
    notifyUrl = None
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, fromBucket, usePipeline, notifyUrl)
    retData, respInfo = pfop.execute(fromKey, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


def main():
    avthumb_m3u8("wifi", "mp4")
    avthumb_m3u8("3g", "mp4")


if __name__ == "__main__":
    main()