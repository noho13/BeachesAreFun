package com.normanhoeller.beachesarefun.network;

import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;

import java.util.List;

/**
 * Created by normanMedicuja on 26/04/17.
 */

class BeachResult {

    private List<Beach> beachList;
    private User user;

    List<Beach> getBeachList() {
        return beachList;
    }

    void setBeachList(List<Beach> beachList) {
        this.beachList = beachList;
    }

    User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }
}
