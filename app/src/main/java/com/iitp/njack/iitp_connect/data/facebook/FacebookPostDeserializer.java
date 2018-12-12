package com.iitp.njack.iitp_connect.data.facebook;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.iitp.njack.iitp_connect.data.contest.ContestListWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacebookPostDeserializer extends StdDeserializer<FacebookPostsWrapper> {
    public FacebookPostDeserializer() {
        super(FacebookPostsWrapper.class);
    }


    @Override
    public FacebookPostsWrapper deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {

        Log.i("hi",jsonParser.getCodec().toString());
        List<FacebookPost> facebookPostList = new ArrayList<>();

        FacebookPostsWrapper wrapper = new FacebookPostsWrapper();

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Log.i("json",node.toString());
        JsonNode data = node.get("data");
        Log.i("json",data.toString());


        if (data.isArray()) {
            for (final JsonNode objNode : data) {
                FacebookPost facebookPost = new FacebookPost();
                JsonNode from = objNode.get("from");
                JsonNode picture = from.get("picture");
                JsonNode datum = picture.get("data");

                facebookPost.setTime(objNode.get("created_time").asText());
                Log.i("objnode",objNode.toString());

                facebookPost.setName(from.get("name").asText());
                Log.i("json",from.toString());

                facebookPost.setUrl(objNode.get("permalink_url").asText());
                Log.i("json",data.toString());
                if(objNode.get("full_picture") != null)
                facebookPost.setPostpic(objNode.get("full_picture").asText());
               // Log.i("json",facebookPost.getPostpic());
                facebookPost.setPropic(datum.get("url").asText());
                Log.i("json",datum.toString());
                if(objNode.get("message") != null)
                facebookPost.setMessage(objNode.get("message").asText());
                Log.i("json",data.toString());

                facebookPostList.add(facebookPost);
            }
        } else {
            FacebookPost facebookPost = new FacebookPost();
            JsonNode from = data.get("from");
            JsonNode picture = from.get("picture");
            JsonNode datum = picture.get("data");

            facebookPost.setTime(data.get("created_time").asText());
            facebookPost.setName(from.get("name").asText());
            facebookPost.setUrl(data.get("permalink_url").asText());
            if(data.get("full_picture") != null)
            facebookPost.setPostpic(data.get("full_picture").asText());
            facebookPost.setPropic(datum.get("url").asText());
            if(data.get("message") != null)
            facebookPost.setMessage(data.get("message").asText());

            facebookPostList.add(facebookPost);
        }

        wrapper.setFacebookPosts(facebookPostList);
        return wrapper;

    }

}
