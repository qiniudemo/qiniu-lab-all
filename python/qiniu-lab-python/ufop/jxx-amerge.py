#coding=utf-8
__author__ = 'jemy'
import qiniu

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"


def amerge(ufopCmd, srcBucket, srcKey, dstFormat, dstMime, urlBucket, url, duration="first", usePipeline="",
           notifyUrl=None,
           saveBucket=None, saveKey=None):
    #prepare fop
    fop = "{0}/format/{1}/mime/{2}/bucket/{3}/url/{4}".format(ufopCmd, dstFormat,
                                                              qiniu.urlsafe_base64_encode(dstMime),
                                                              qiniu.urlsafe_base64_encode(urlBucket),
                                                              qiniu.urlsafe_base64_encode(url))
    if saveBucket != None and saveKey != None:
        fop = qiniu.op_save(fop, saveBucket, saveKey)

    #do pfop
    auth = qiniu.Auth(accessKey, secretKey)
    pfop = qiniu.PersistentFop(auth, srcBucket, usePipeline, notifyUrl)
    retData, respInfo = pfop.execute(srcKey, [fop], force=None)
    if retData != None:
        print("PersistentId:" + retData["persistentId"])
    else:
        print("Error:")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)


def main():
    #需要做混音的文件
    srcBucket = "if-pbl"
    srcKey = "004.m4a"

    #ufop实例命令名称
    ufopCmd = "jxx-amerge"

    #目标格式
    dstFormat = "mp3"
    dstMime = "audio/mpeg"
    #混音目标
    urlBucket = "if-pbl"
    url = "http://if-pbl.qiniudn.com/004.mp3"

    amerge(ufopCmd, srcBucket, srcKey, dstFormat, dstMime, urlBucket, url, duration="longest",
           usePipeline="jemy", saveBucket="if-pbl", saveKey="amerge_result.mp3")


if __name__ == "__main__":
    main()