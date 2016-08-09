#include "com_wuja6_android_getimageedgeapp_task_OpenCVProcessTask.h"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"

#include <vector>

using namespace cv;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_wuja6_android_getimageedgeapp_task_OpenCVProcessTask_getEdgeByCanny(
        JNIEnv *env, jobject _obj, jstring jinput, jstring joutput,
        jdouble lowth, jdouble highth, jint ksize) {
    Mat src, src_gray;
    Mat dst, detected_edges;

    src = imread(env->GetStringUTFChars(jinput, JNI_FALSE));
    dst.create(src.size(), src.type());
    cvtColor(src, src_gray, CV_BGR2GRAY);
    blur(src_gray, detected_edges, Size(3, 3));
    Canny(detected_edges, detected_edges, lowth, highth, ksize);
    dst = Scalar::all(0);
    src.copyTo(dst, detected_edges);

    std::vector<int> compression_params;
    compression_params.push_back(IMWRITE_JPEG_QUALITY);

    // set quality of jpeg image (0~100)
    compression_params.push_back(95);

    imwrite(env->GetStringUTFChars(joutput, JNI_FALSE), dst,
            compression_params);

    return joutput;
}

#ifdef __cplusplus
}
#endif
