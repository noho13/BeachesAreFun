package com.normanhoeller.beachesarefun.network;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public interface BeachAPI {

    String getBeaches(String path, int page);
    String postCredentials(String path, String payload);

}
