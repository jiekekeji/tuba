#include <stdio.h>
#include "com_jack_tuba_utils_TubaUtils.h"


JNIEXPORT jstring JNICALL Java_com_jack_tuba_utils_TubaUtils_getMainUrl
  (JNIEnv * env, jclass ass, jstring jq, jint jsize, jint jstart){

	return  (*env)->NewStringUTF(env,"https://ajax.googleapis.com/ajax/services/search/images?v=1.0");
}

