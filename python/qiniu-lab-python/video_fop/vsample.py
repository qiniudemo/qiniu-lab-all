#coding=utf-8
__author__ = 'jemy'

import qiniu

'''
该例子演示如何使用vsample接口来对视频进行批量帧截图。
详细文档：http://developer.qiniu.com/docs/v6/api/reference/fop/av/vsample.html
'''

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

'''
bucket - 要取帧视频所在空间
key    - 要取帧视频的名字
picFormat - 帧截图的格式，可以为png，jpg
startSecond - 帧截图起始点（单位秒）
duration    - 取帧时间长度（单位秒）
resolution - 帧截图的大小（宽x高）
rotate - 帧截图的旋转角度，可以为auto，90，180，270
interval - 帧截图时间间隔
'''


def vsample(bucket, key, picFormat, startSecond, duration, pattern, resolution=None, rotate=None, interval=None):
    #准备命令
    cmd = "vsample"

    #组织参数
    cmdParams = {
        "ss": startSecond,
        "t": duration,
        "pattern": pattern,
    }
    if resolution:
        cmdParams.update({"s": resolution})
    if rotate:
        cmdParams.update({"rotate": rotate})
    if interval:
        cmdParams.update({"interval": interval})

    #执行fop
    fop = qiniu.build_op(cmd, picFormat, **cmdParams)
    #默认为空，表示使用公共队列
    usePipeline = ""
    #fop处理结果通知url
    notifyUrl = None
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, bucket, usePipeline, notifyUrl)
    retData, respInfo = pfop.execute(key, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


def main():
    bucket = "if-pbl"
    key = "qiniu.mp4"
    pattern = "qiniu_vsample_$(count).png"
    picFormat = "png"
    startSecond = 0
    duration = 180
    resolution = "400x300"
    rotate = "auto"
    interval = 10
    #注意pattern需要URL安全的Base64编码
    pattern = qiniu.urlsafe_base64_encode(pattern)
    vsample(bucket, key, picFormat, startSecond, duration, pattern, resolution, rotate,
            interval)


if __name__ == "__main__":
    main()