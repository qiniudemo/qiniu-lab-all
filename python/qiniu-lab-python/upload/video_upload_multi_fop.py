#coding=utf-8
__author__ = 'jemy'

'''
该例子演示在视频上传完成之后，自动出发一系列的对视频的处理操作。
包括视频转码并加水印，取关键帧，以及取样操作。
'''

import qiniu

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

def progress(curLength, maxLength):
    print(str((curLength * 1.0 / maxLength) * 100) + "%")


def upload(upToken, key, filePath):
    progHandler = progress
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progHandler)
    if retData != None:
        print("Hash:" + retData["hash"])
        print("Key:" + retData["key"])
        print("PersistentId:" + retData["persistentId"])
    else:
        print(respInfo)


def main():
    bucket = "if-pbl"
    key = "qiniu_temp.mp4"
    filePath = "/Users/jemy/Documents/qiniu.mp4"
    auth = qiniu.Auth(accessKey, secretKey)

    wmImage = qiniu.urlsafe_base64_encode("http://if-pbl.qiniudn.com/qiubai.jpg")
    saveAs = qiniu.urlsafe_base64_encode(bucket + ":qiniu_tree.mp4")

    fops = []
    fops.append("avthumb/mp4/wmImage/{0}|saveas/{1}".format(wmImage, saveAs))
    fops.append("vframe/png/offset/10/w/400/h/224/rotate/auto|saveas/{0}".format(
        qiniu.urlsafe_base64_encode(bucket + ":first_tree.png")))
    fops.append("vsample/png/ss/0/t/180/s/400x224/rotate/auto/interval/10/pattern/{0}".format(
        qiniu.urlsafe_base64_encode("sample_$(count).png")))

    policy = {
        "persistentOps": ";".join(fops),
        "persistentPipeline": "fff"
    }
    upToken = auth.upload_token(bucket, key=key, policy=policy)
    upload(upToken, key, filePath)


if __name__ == "__main__":
    main()