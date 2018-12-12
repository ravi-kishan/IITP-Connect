package com.iitp.njack.iitp_connect.data.facebook;

import android.arch.lifecycle.LiveData;

import com.iitp.njack.iitp_connect.data.network.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FacebookApi {
    String url = "likes,comments.limit(0),shares,created_time,message,link,from{picture},picture,access_token=EAAJv5j9KaU4BACqtNzt6zSufNg8ZClOo0DNdPNJe2YOkJ7VcW45HHpEM3BK2vFyuBGLl5PBwEQ2DV9LwRKkxDDZAFGpoBePGOtZBEO72uMQ0dQYN9ZClLIh3Qn1pTn2dZCZCwamyN6TPDZBZCNEFMh4gBkp5qLxG0wborkZB4aVLCNlk53VrLAay7sirCAK2Kk3spJvp0OMS9Ksk5lzk5kcSP";
    @GET("/v3.2/studconnect/feed")
    LiveData<ApiResponse<FacebookPostsWrapper>> getFacebookPosts(@Query("fields")String fields ,@Query("access_token") String api);
}
