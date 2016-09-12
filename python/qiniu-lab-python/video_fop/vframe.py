#coding=utf-8
__author__ = 'jemy'
import qiniu

'''
该例子演示如何从一个视频中截取某一时刻的帧。
详细文档：http://developer.qiniu.com/docs/v6/api/reference/fop/av/vframe.html
'''

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"


def vframe(format, offset, width, height, rotate, fromBucket, fromKey, usePipeline="", notifyUrl=None, saveBucket=None,
           saveKey=None):
    #prepare fop
    params = {
        "offset": offset,
        "w": width,
        "h": height,
        "rotate": rotate
    }
    fop = qiniu.build_op("vframe", format, **params)
    if saveBucket != None and saveKey != None:
        fop = qiniu.op_save(fop, saveBucket, saveKey)

    #do pfop
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
    vframe("jpg", 0, 400, 300, "auto", "qiniu-lab", u"七牛云存储视频名片.mp4".encode("utf-8"), usePipeline="fff",
           saveBucket="qiniu-lab",
           saveKey="vframe_01.jpg")
    vframe("jpg", 10, 400, 300, 90, "qiniu-lab", u"七牛云存储视频名片.mp4".encode("utf-8"), usePipeline="fff",
           saveBucket="qiniu-lab",
           saveKey="vframe_02.jpg")
    vframe("png", 0, 400, 300, "270", "qiniu-lab", u"七牛云存储视频名片.mp4".encode("utf-8"), usePipeline="fff",
           saveBucket="qiniu-lab",
           saveKey="vframe_03.png")
    #save to different bucket
    vframe("png", 0, 400, 300, "180", "qiniu-lab", u"七牛云存储视频名片.mp4".encode("utf-8"), usePipeline="fff",
           saveBucket="if-pbl",
           saveKey="vframe_04.png")


if __name__ == "__main__":
    main()