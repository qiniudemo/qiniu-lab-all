#coding=utf-8
__author__ = 'jemy'

import qiniu

upToken = '''ELUs327kxVPJrGCXqWae9yioc0xYZyrIpbM6Wh6o:dt1xDHLUrj4AjOK04I7XycVaQcI=:eyJzY29wZSI6ImlmLXBibCIsImRlYWRsaW5lIjoxNDE5NzY4NDM0fQ=='''
parts = upToken.split(":")
print(qiniu.urlsafe_base64_decode(parts[1]))
print(qiniu.urlsafe_base64_decode(parts[2]))