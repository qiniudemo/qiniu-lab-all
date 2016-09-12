#coding=utf-8

__author__ = 'jemy'
import qiniu
import urlparse


def saveas(url_with_fop, save_bucket, save_key, ak, sk):
    encoded_entry = qiniu.urlsafe_base64_encode(save_bucket + ":" + save_key)
    new_url_with_fop = "{0}|saveas/{1}".format(url_with_fop, encoded_entry)
    scheme = urlparse.urlparse(new_url_with_fop).scheme
    new_url_without_scheme = new_url_with_fop.strip(scheme + "://")
    auth = qiniu.Auth(ak, sk)
    encoded_sign = auth.token(new_url_without_scheme)
    final_url = "{0}/sign/{1}".format(new_url_with_fop, encoded_sign)
    return final_url


def main():
    url_with_fop = "http://if-pbl.qiniudn.com/qiniu.jpg?imageView2/0/w/100"
    save_bucket = "if-pbl"
    save_key = "qiniu_w100.jpg"
    ak = "<AccessKey>"
    sk = "<SecretKey>"

    final_url = saveas(url_with_fop, save_bucket, save_key, ak, sk)
    print(final_url)


if __name__ == "__main__":
    main()
