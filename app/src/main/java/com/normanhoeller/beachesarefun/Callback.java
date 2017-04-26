package com.normanhoeller.beachesarefun;

import com.normanhoeller.beachesarefun.beaches.Beach;

import java.util.List;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public interface Callback {

    void setBeachesResult(List<Beach> beaches);

    void setErrorResult(BeachError error);
}
