package `in`.aryomtech.syno.FCM

import `in`.aryomtech.syno.FCM.Constants.Companion.CONTENT_TYPE
import `in`.aryomtech.syno.FCM.Constants.Companion.SERVER_KEY
import `in`.aryomtech.syno.FCM.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY","Content_Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}