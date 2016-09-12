#coding=utf-8
__author__ = 'jemy'

'''
This snippet query the status of fop operation.
The detailed doc is here at http://developer.qiniu.com/docs/v6/api/reference/fop/pfop/prefop.html
'''

import sys
import qiniu
from qiniu import config
from qiniu import http

accessKey = "<Your Access Key>"
secretKey = "<Your Secret Key>"

def query(persistentId):
    #print(persistentId)
    auth = qiniu.Auth(accessKey, secretKey)
    url = 'http://{0}/status/get/prefop?id={1}'.format(config.API_HOST, persistentId)
    retData, respInfo = http._get(url, params=None, auth=auth)
    if retData != None:
        print("Success: (" + persistentId + ")")
        print("---id:" + retData["id"])
        print("---code:" + str(retData["code"]))
        print("---desc:" + retData["desc"])
        for item in retData["items"]:
            print("---cmd:" + item["cmd"])
            print("------code:" + str(item["code"]))
            print("------desc:" + item["desc"])
            if item.__contains__("error"):
                print("------error:" + item["error"])
            if item.__contains__("hash"):
                print("------hash:" + item["hash"])
            if item.__contains__("key"):
                print("------key:" + item["key"])
    else:
        #print(respInfo)
        print("Error (" + persistentId + "):")
        print("--StatusCode:" + str(respInfo.status_code))
        print("--Reqid:" + respInfo.req_id)
        print("--Message:" + respInfo.error)
    print("\r\n")


def main():
    args = sys.argv
    args = args[1:]
    if len(args) > 0:
        for arg in args:
            query(arg)
    else:
        print("prefop persistentId1 [persistentId2 [persistentId3...]]")


if __name__ == "__main__":
    main()
