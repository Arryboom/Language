#行人检测

```
import cv2
from imutils.object_detection import non_max_suppression
import numpy as np
 
img=cv2.imread("img2.jpg")
orig = img.copy()
 
# 定义HOG对象，采用默认参数，或者按照下面的格式自己设置
defaultHog=cv2.HOGDescriptor()
# 设置SVM分类器，用默认分类器
defaultHog.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
 
#这里对整张图片进行裁剪
# # detect people in the image
# (rects, weights) = defaultHog.detectMultiScale(img, winStride=(4, 4),padding=(8, 8), scale=1.05)
# for (x, y, w, h) in rects:
#     cv2.rectangle(orig, (x, y), (x + w, y + h), (0, 0, 255), 2)
# rects = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rects])
# pick = non_max_suppression(rects, probs=None, overlapThresh=0.65)
# for (xA, yA, xB, yB) in pick:
#     cv2.rectangle(img, (xA, yA), (xB, yB), (0, 255, 0), 2)
# cv2.imshow("Before NMS", orig)
# cv2.imshow("After NMS", img)
 
#只对ROI进行裁剪，img[height_begin:height_end,width_begin:width_end]
roi=img[0:200,800:1600]
cv2.imshow("roi",roi)
cv2.imwrite("roi.jpg",roi)
(rects, weights) = defaultHog.detectMultiScale(roi, winStride=(4, 4),padding=(8, 8), scale=1.05)
rects = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rects])
pick = non_max_suppression(rects, probs=None, overlapThresh=0.65)
for (xA, yA, xB, yB) in pick:
    cv2.rectangle(roi, (xA, yA), (xB, yB), (0, 255, 0), 2)
 
cv2.imshow("roi",roi)
cv2.imwrite("roi_out.jpg",roi)
cv2.waitKey(0)
```


c++ with video
>http://datahonor.com/2017/08/17/Pedestrain-Detection/

```
#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include "opencv2/imgcodecs.hpp"
#include <opencv2/highgui.hpp>
#include <opencv2/ml.hpp>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

#define INPUT_VIDEO_FILE "/home/shensir/Documents/MyPrograming/Cpp/MyCV/data/terrace1-c0.avi"
#define WINDOW_NAME "WINDOW"

// Reference: https://github.com/Itseez/opencv/blob/master/samples/cpp/train_HOG.cpp
void draw_locations(Mat &img, const vector<Rect> &locations, const Scalar &color) {
    if (!locations.empty()) {
        vector<Rect>::const_iterator loc = locations.begin();
        vector<Rect>::const_iterator end = locations.end();
        for (; loc != end; ++loc) {
            rectangle(img, *loc, color, 2);
        }
    }
}

int main(int argc, char **argv) {
    Mat mSrc;
    vector<Rect> vDetected;
    HOGDescriptor hog;

    static vector<float> detector = HOGDescriptor::getDefaultPeopleDetector();
    hog.setSVMDetector(detector);

    VideoCapture cap(INPUT_VIDEO_FILE);

    while (true) {

        cap >> mSrc;
        hog.detectMultiScale(mSrc, vDetected, 0, Size(8, 8), Size(32, 32), 1.05, 2);
        draw_locations(mSrc, vDetected, Scalar(0, 255, 0));

        imshow(WINDOW_NAME, mSrc);
        // 加上ESC退出
        char c = waitKey(10);
        if (c == 27)
            break;
    }

    return 0;
}
```




#人脸检测
>https://www.cnblogs.com/gzshan/p/10702752.html


```
import cv2

# 使用人脸识别分类器
classfier = cv2.CascadeClassifier("haarcascade_frontalface_alt2.xml")

# 读取图片
image = cv2.imread("face.jpg")
# 转为灰度图
gray = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)

faces = classfier.detectMultiScale(gray,scaleFactor=1.1,minNeighbors=5,minSize=(3,3))
print("发现{0}个人脸!".format(len(faces)))

for faceRect in faces:
    x,y,w,h=faceRect
    cv2.rectangle(image,(x,y),(x+w,y+w),(0,255,0),2)
cv2.imwrite("./face1.jpg",image)
```



#文本区域检测与识别

>https://blog.csdn.net/xuxunjie147/article/details/87178774

![](/pics/screencapture-blog-csdn-net-xuxunjie147-article-details-87178774-2020-11-24-15_00_47.png)


#OPENCV+PYTHON 文字识别（重点图像透视变换）

>https://www.cnblogs.com/nmucomputer/p/12241439.html

```
# Author：Winter Liu is coming!
import cv2 as cv
import numpy as np
import pytesseract


# 预处理，高斯滤波（用处不大），4次开操作
# 过滤轮廓唯一
def contour_demo(img):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    gray = cv.GaussianBlur(gray, (5, 5), 1)
    ref, thresh = cv.threshold(gray, 127, 255, cv.THRESH_BINARY)
    kernel = np.ones((9, 9), np.uint8)
    thresh = cv.morphologyEx(thresh, cv.MORPH_OPEN, kernel, iterations=4)
    contours, hierachy = cv.findContours(thresh, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)
    print(len(contours))
    return contours


def capture(img):
    contours = contour_demo(img)
    # 轮廓唯一，以后可以扩展
    contour = contours[0]
    # 求周长，可在后面的转换中使用周长和比例
    print(cv.arcLength(contour,True))
    img_copy = img.copy()
    # 使用approxPolyDP，将轮廓转换为直线，22为精度（越高越低），TRUE为闭合
    approx = cv.approxPolyDP(contour, 22, True)
    # print(approx.shape)
    # print(approx)
    # cv.drawContours(img_copy, [approx], -1, (255, 0, 0), 15)
    n = []
    # 生产四个角的坐标点
    for x, y in zip(approx[:, 0, 0], approx[:, 0, 1]):
        n.append((x, y))
    p1 = np.array(n, dtype=np.float32)
    # 对应点
    p2 = np.array([(0, 0), (0, 1500), (1000, 1500), (1000, 0)], dtype=np.float32)
    M = cv.getPerspectiveTransform(p1, p2) # 变换矩阵
    # 使用透视变换
    result = cv.warpPerspective(img_copy, M, (0, 0))
    # 重新截取
    result = result[:1501, :1001]
    cv.imwrite(r"C:\PycharmProjects\OpenCV\pic\ocr.png", result)
    return result


# 图像识别代码，需要预先下载安装开源工具包 pytesseract，配置环境变量
# pip install pytesseract
# 修改“C:\Python\Python37\Lib\site-packages\pytesseract\pytesseract.py”中“cmd”为绝对路径
def ocr_img(img):
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    # 图像清晰度越高结果越精确，时间更长
    text = pytesseract.image_to_string(gray)
    print(text)


src = cv.imread(r"C:\PycharmProjects\OpenCV\pic\page.jpg")
res = capture(src)
ocr_img(res)
cv.waitKey(0)
cv.destroyAllWindows()
```

![](/pics/screencapture-cnblogs-nmucomputer-p-12241439-html-2020-11-24-15_02_48.png)