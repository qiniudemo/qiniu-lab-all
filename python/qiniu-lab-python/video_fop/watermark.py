#coding=utf-8
__author__ = 'jemy'

'''
这个例子演示如何为一个视频添加文字或图片水印。

详细的文档参考：http://developer.qiniu.com/docs/v6/api/reference/fop/av/video-watermark.html

由于加水印接口现在使用的是avthumb接口，所以你可以在做视频转码的同时加上水印，并且得到最终既转码又加了
水印的视频文件。
'''

import qiniu
from qiniu import config
from qiniu import http

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

#为视频加上文字水印，并使用saveas接口保存结果
def watermark_with_text(srcBucket, srcKey, destFormat,
                        wmText, wmGravityText="NorthEast",
                        wmFont=None, wmFontColor=None, wmFontSize=None,
                        saveBucket=None, saveKey=None,
                        usePipeline=None, notifyUrl=None):
    cmd = "avthumb"
    params = {
        "wmText": qiniu.urlsafe_base64_encode(wmText),
        "wmGravityText": wmGravityText,
    }
    if wmFont != None:
        params.update({"wmFont": qiniu.urlsafe_base64_encode(wmFont)})
    if wmFontColor != None:
        params.update({"wmFontColor": qiniu.urlsafe_base64_encode(wmFontColor)})
    if wmFontSize != None:
        params.update({"wmFontSize": wmFontSize})

    fop = qiniu.build_op(cmd, destFormat, **params)
    #saveas
    if saveBucket != None and saveKey != None:
        fop = qiniu.op_save(fop, saveBucket, saveKey)

    #pfop
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, srcBucket, pipeline=usePipeline, notify_url=notifyUrl)
    retData, respInfo = pfop.execute(srcKey, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


#为视频加上图片水印，并且使用saveas接口保存结果
def watermark_with_image(srcBucket, srcKey, destFormat,
                         wmImage, wmGravity="NorthEast",
                         saveBucket=None, saveKey=None,
                         usePipeline=None, notifyUrl=None):
    cmd = "avthumb"
    params = {
        "wmImage": qiniu.urlsafe_base64_encode(wmImage),
        "wmGravity": wmGravity,
    }

    fop = qiniu.build_op(cmd, destFormat, **params)
    #saveas
    if saveBucket != None and saveKey != None:
        fop = qiniu.op_save(fop, saveBucket, saveKey)

    #pfop
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, srcBucket, pipeline=usePipeline, notify_url=notifyUrl)
    retData, respInfo = pfop.execute(srcKey, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


#为视频加上图片水印和文字水印，并使用saveas接口保存结果
def watermark_with_image_and_text(srcBucket, srcKey, destFormat,
                                  wmText, wmImage,
                                  wmGravity="NorthWest", wmGravityText="NorthEast",
                                  wmFont=None, wmFontColor=None, wmFontSize=None,
                                  saveBucket=None, saveKey=None,
                                  usePipeline=None, notifyUrl=None
):
    cmd = "avthumb"
    params = {
        "wmText": qiniu.urlsafe_base64_encode(wmText),
        "wmGravityText": wmGravityText,
        "wmImage": qiniu.urlsafe_base64_encode(wmImage),
        "wmGravity": wmGravity,
    }
    if wmFont != None:
        params.update({"wmFont": qiniu.urlsafe_base64_encode(wmFont)})
    if wmFontColor != None:
        params.update({"wmFontColor": qiniu.urlsafe_base64_encode(wmFontColor)})
    if wmFontSize != None:
        params.update({"wmFontSize": wmFontSize})

    fop = qiniu.build_op(cmd, destFormat, **params)
    #saveas
    if saveBucket != None and saveKey != None:
        fop = qiniu.op_save(fop, saveBucket, saveKey)

    #pfop
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, srcBucket, pipeline=usePipeline, notify_url=notifyUrl)
    retData, respInfo = pfop.execute(srcKey, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


def main():
    srcBucket = "qiniu-lab"
    srcKey = u"七牛云存储视频名片.mp4".encode("utf-8")
    saveBucket = "if-pbl"
    saveKey = u"七牛云存储视频名片_文字水印.mp4".encode("utf-8")
    usePipeline = "fff"
    wmText = u"七牛云存储例子".encode("utf-8")
    wmFont = u"微软雅黑".encode("utf-8")
    wmFontColor = "#ff0000"
    wmFontSize = 200
    watermark_with_text(srcBucket, srcKey, "mp4", wmText,
                        wmFont=wmFont, wmFontColor=wmFontColor, wmFontSize=wmFontSize,
                        saveBucket=saveBucket, saveKey=saveKey, usePipeline=usePipeline)
    wmImage = u"http://if-pbl.qiniudn.com/qiniu-blue-195x105.png".encode("utf-8")
    saveKey = u"七牛云存储视频名片_图片水印.mp4".encode("utf-8")
    watermark_with_image(srcBucket, srcKey, "mp4", wmImage,
                         saveBucket=saveBucket, saveKey=saveKey,
                         usePipeline=usePipeline)
    saveKey = u"七牛云存储视频名片_图片_文字水印.mp4".encode("utf-8")
    watermark_with_image_and_text(srcBucket, srcKey, "mp4", wmText, wmImage,
                                  wmGravity="NorthWest", wmGravityText="NorthEast",
                                  wmFont=wmFont, wmFontColor=wmFontColor, wmFontSize=wmFontSize,
                                  saveBucket=saveBucket, saveKey=saveKey, usePipeline=usePipeline)


if __name__ == "__main__":
    main()