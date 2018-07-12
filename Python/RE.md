First generate a Re expression
http://www.txt2re.com/


then 
```
import re
import codecs
import base64

def reverse_str(x):
    return x[::-1]

with open("m.txt","r") as s:
    xxx=s.read()
##Re
txt='FltDiC("+VPqjyzpwAKL2Sznv0GMaSJqa5JLfOPqjyzpwAUC")'

re1='(FltDiC)'	# Word 1
re2='(\\()'	# Any Single Character 1
re3='(")'	# Any Single Character 2
re4='(.)'	# Any Single Character 3
re5='((?:[a-z][a-z]*[0-9]+[a-z0-9]*))'	# Alphanum 1
re6='.*?'	# Non-greedy match on filler
re7='(")'	# Any Single Character 4
re8='(\\))'	# Any Single Character 5

rg = re.compile(re1+re2+re3+re4+re5+re6+re7+re8,re.IGNORECASE|re.DOTALL)


ind=0
allkeys={}
for m in rg.finditer(xxx):
    word1 = m.group(1)
    c1 = m.group(2)
    c2 = m.group(3)
    c3 = m.group(4)
    alphanum1 = m.group(5)
    c4 = m.group(6)
    c5 = m.group(7)
    print c3,alphanum1
    bb=reverse_str(c3+alphanum1)
    print bb
    bb=codecs.encode(bb,"rot13")
    print bb
    bb=base64.b64decode(bb)
    ind=ind+1
print ind
for abc in allkeys.keys():
    xxx=xxx.replace(abc,allkeys[abc])
print len(xxx)
##these none sense


```
https://docs.python.org/2/library/re.html