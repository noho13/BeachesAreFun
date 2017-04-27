package com.normanhoeller.beachesarefun.network;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public interface BeachAPI {

    String getRequest(String urlAsString, String token);
    String postRequest(String path, String payload);

}
