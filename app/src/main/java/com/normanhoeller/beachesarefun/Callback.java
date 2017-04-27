package com.normanhoeller.beachesarefun;

import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;

import java.util.List;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public interface Callback {

    void setBeachesResult(List<Beach> beaches);

    void setUserResult(User user);

    void handleError(BeachError error);
}
