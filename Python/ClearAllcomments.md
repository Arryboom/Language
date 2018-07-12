# ClearALLComments

```
with open("withcomments.py","r") as x:
    m,c,a="",0,0
    for line in x.readlines():
        if line.strip()[0:1]=="#":
            print "CommitsLine"
            c=c+1
        else:
            m=m+line
            a=a+1
e=open("nocomments.py","w")
e.write(m)
e.close()
print ("Processed comments line:{}\nEffective code line:{}".format(c,a))
```