#include "org_selaux_stasm_Stasm.h"
#include "stasm/stasm_lib.h"
#include "opencv2/core.hpp"

void Java_org_selaux_stasm_Stasm_nativeSearch(JNIEnv *env, jobject obj, jlong input, jlong output)
{
    int nLandmarks = stasm_NLANDMARKS;
    int foundface = 0;
    cv::Mat* incv = (cv::Mat*) input;
    cv::Mat* outcv = (cv::Mat*) output;
    float landmarks[2 * stasm_NLANDMARKS];

    outcv->create(nLandmarks, 2, cv::DataType<float>::type);

    jclass clazz = env->FindClass( "org/selaux/stasm/Stasm" );
    jfieldID dirField = env->GetFieldID( clazz, "modelDirectory", "Ljava/lang/String;" );
    jstring dirStr = (jstring)env->GetObjectField(obj, dirField);
    const char* dirChar = env->GetStringUTFChars(dirStr, 0);
    int search_success = stasm_search_single(&foundface, landmarks, (char*)incv->data, incv->cols, incv->rows, "", dirChar);
    env->ReleaseStringUTFChars(dirStr, dirChar);

    if (!search_success) {
        char errorMessage[255];
        sprintf(errorMessage, "Error in stasm_search_single: %s", stasm_lasterr());
        env->ThrowNew(env->FindClass("java/lang/Exception"), errorMessage);
        return;
    }
    if (!foundface) {
        char errorMessage[255];
        sprintf(errorMessage, "No face found: %s", stasm_lasterr());
        env->ThrowNew(env->FindClass("java/lang/Exception"), errorMessage);
        return;
    }

    stasm_force_points_into_image(landmarks, incv->cols, incv->rows);

    for (int i = 0; i < stasm_NLANDMARKS; i++) {
        outcv->row(i).col(0) = landmarks[i*2+1];
        outcv->row(i).col(1) = landmarks[i*2];
    }
}
