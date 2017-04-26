package com.normanhoeller.beachesarefun.network;

import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normanMedicuja on 26/04/17.
 */

class BeachParser {

    static List<Beach> parseBeaches(String json) {
        List<Beach> beaches = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject beach = jArray.getJSONObject(i);
                String id = beach.getString("_id");
                String name = beach.getString("name");
                String url = beach.getString("url");
                String width = beach.getString("width");
                String height = beach.getString("height");
                beaches.add(new Beach(id, name, url, width, height));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beaches;
    }


    static User parseUser(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString("_id");
            String email = jsonObject.getString("email");
            return new User(id, email);
        } catch (JSONException exception) {
            exception.printStackTrace();
            return new User("Unfortunately, an error has happened. Plese try again");
        }
    }

}
