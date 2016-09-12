#coding=utf-8
__author__ = 'jemy'
'''
本例演示了一个简单的文件上传。

这个例子里面，sdk根据文件的大小选择是Form方式上传还是分片上传。
'''
import qiniu

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

#输出上传进度（仅在分片上传时有效）
def progress(curLength, maxLength):
    print(str((curLength * 1.0 / maxLength) * 100) + "%")

#解析结果
def parseRet(retData, respInfo):
    if retData != None:
        print("Upload file success!")
        print("Hash: " + retData["hash"])
        print("Key: " + retData["key"])

        #检查扩展参数
        for k, v in retData.items():
            if k[:2] == "x:":
                print(k + ":" + v)

        #检查其他参数
        for k, v in retData.items():
            if k[:2] == "x:" or k == "hash" or k == "key":
                continue
            else:
                print(k + ":" + str(v))
    else:
        print("Upload file failed!")
        print("Error: " + respInfo.text_body)

#无key上传
def upload_without_key():
    bucket = "if-pbl"
    #http请求中不指定key参数
    filePath = "/Users/jemy/Documents/jemy.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=None)
    retData, respInfo = qiniu.put_file(upToken, None, filePath, progress_handler=progress)
    parseRet(retData, respInfo)

#有key上传
def upload_with_key():
    bucket = "if-pbl"
    #http请求中指定key参数
    key = "jemy.png"
    filePath = "/Users/jemy/Documents/jemy.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)

#有key带扩展参数上传
def upload_with_key_and_xparams():
    bucket = "if-pbl"
    key = "qiniu.png"
    filePath = "/Users/jemy/Documents/qiniu.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    xparams = {
        "x:text1": "hello qiniu",
        "x:text2": "qiniu is good",
        "x:text3": "cloud storage"
    }
    retData, respInfo = qiniu.put_file(upToken, key, filePath, params=xparams, progress_handler=progress)
    parseRet(retData, respInfo)

#有key自定义MimeType上传
def upload_with_key_and_mimetype():
    bucket = "if-pbl"
    key = "qiniu.jpg"
    filePath = "/Users/jemy/Documents/qiniu.jpg"
    mimeType = "image/jpeg"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, mime_type=mimeType, progress_handler=progress)
    parseRet(retData, respInfo)

#有key带crc32校验上传
def upload_with_key_and_crc32():
    bucket = "if-pbl"
    key = "jemy_smile.png"
    filePath = "/Users/jemy/Documents/jemy_smile.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, check_crc=True, progress_handler=progress)
    parseRet(retData, respInfo)

#同名文件，不同内容覆盖上传
def upload_overwrite_existing_file():
    bucket = "if-pbl"
    key = "jemy_smile.png"
    filePath = "/Users/jemy/Documents/jemy_smile.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)
    #使用不同文件
    filePath = "/Users/jemy/Documents/qiniu.jpg"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)

#同名文件，不允许不同内容覆盖上传
def upload_not_allow_overwrite_with_different_file_content():
    bucket = "if-pbl"
    key = "jemy_smile.png"
    filePath = "/Users/jemy/Documents/jemy_smile.png"
    auth = qiniu.Auth(accessKey, secretKey)
    upToken = auth.upload_token(bucket, key=key)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)
    #使用不同文件，并设置insertOnly=1
    filePath = "/Users/jemy/Documents/qiniu.jpg"
    auth = qiniu.Auth(accessKey, secretKey)
    #设置insertOnly不为0，表示禁止不同内容覆盖
    policy = {
        "insertOnly": 1
    }
    upToken = auth.upload_token(bucket, key=key, policy=policy)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)

#使用saveKey来指定上传文件名，在上传请求中不指定key的情况下，使用PutPolicy里面的saveKey作为文件保持名
def upload_use_save_key_as_name():
    bucket = "if-pbl"
    filePath = "/Users/jemy/Documents/qiniu.jpg"
    auth = qiniu.Auth(accessKey, secretKey)
    policy = {
        "saveKey": "qiniu_good.jpg"
    }
    upToken = auth.upload_token(bucket, None, policy=policy)
    retData, respInfo = qiniu.put_file(upToken, None, filePath, progress_handler=progress)
    parseRet(retData, respInfo)

#有key上传，限定上传文件的大小和mimeType
def upload_with_key_and_fsizelimit_mimelimt():
    bucket = "if-pbl"
    key = "qiniu.jpg"
    filePath = "/Users/jemy/Documents/qiniu.jpg"
    auth = qiniu.Auth(accessKey, secretKey)
    policy = {
        "fsizeLimit": 100,
        "mimeLimit": "image/png"
    }
    upToken = auth.upload_token(bucket, key=key, policy=policy)
    retData, respInfo = qiniu.put_file(upToken, key, filePath, progress_handler=progress)
    parseRet(retData, respInfo)


def main():
    print("-------upload without key-------")
    upload_without_key()

    print("\r\n")

    print("-------upload with key-------")
    upload_with_key()

    print("\r\n")

    print("-------upload with key and extra params")
    upload_with_key_and_xparams()

    print("\r\n")

    print("-------upload with key and mime type")
    upload_with_key_and_mimetype()

    print("\r\n")

    print("-------upload with key and crc32 check")
    upload_with_key_and_crc32()

    print("\r\n")
    print("-------upload overwrite the existing file-------")
    upload_overwrite_existing_file()

    print("\r\n")
    print("-------upload not allow overwrite with different file content-------")
    upload_not_allow_overwrite_with_different_file_content()

    print("\r\n")
    print("-------upload use save key-------")
    upload_use_save_key_as_name()

    print("\r\n")
    print("-------upload with key and file size limit and mime type limit-------")
    upload_with_key_and_fsizelimit_mimelimt()


if __name__ == "__main__":
    main()