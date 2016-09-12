#coding=utf-8
__author__ = 'jemy'

'''
该例子通过使用avinfo接口来获取视频的长度，然后截取8个等时间段的视频截图。

avinfo文档：http://developer.qiniu.com/docs/v6/api/reference/fop/av/avinfo.html
vframe文档：http://developer.qiniu.com/docs/v6/api/reference/fop/av/vframe.html
'''

import qiniu
from qiniu import http
import vframe

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"


def vframe_interval(videoLink, picCount, picFormat, width, height, fromBucket, fromKey, saveBucket=None,
                    savePicPrefix=None,
                    usePipeline="", notifyUrl=None):
    auth = qiniu.Auth(accessKey, secretKey)
    url = '{0}?avinfo'.format(videoLink)
    retData, respInfo = http._get(url, params=None, auth=auth)
    if retData != None:
        videoData = retData["format"]
        duration = int(float(videoData["duration"]))
        offset_slice = duration / picCount
        for i in range(picCount):
            vframe.vframe(picFormat, offset_slice * i, width, height, "auto", fromBucket, fromKey,
                          saveBucket=saveBucket,
                          saveKey=savePicPrefix + str(i + 1) + "." + picFormat, usePipeline=usePipeline,
                          notifyUrl=notifyUrl)
    else:
        print("Error:" + respInfo["error"])


def main():
    fromBucket = "qiniu-lab"
    fromKey = u"七牛云存储视频名片.mp4".encode("utf-8")
    videoLink = u"http://qiniu-lab.qiniudn.com/七牛云存储视频名片.mp4".encode("utf-8")
    saveBucket = "if-pbl"
    vframe_interval(videoLink, 8, "png", 400, 300, fromBucket, fromKey, saveBucket, "vframe_cnt_8_", usePipeline="fff")


if __name__ == "__main__":
    main()