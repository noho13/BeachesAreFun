package com.normanhoeller.beachesarefun.network;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public class BeachRequest {

    private int operationType;
    private String payload;
    private int page;
    private String path;

    static BeachRequest createBeachRequest(int operationType, int page, String path) {
        BeachRequest beachRequest = new BeachRequest(operationType);
        beachRequest.setPage(page);
        beachRequest.setPath(path);
        return beachRequest;
    }

    public static BeachRequest createBeachRequest(int operationType, String path, String payload) {
        BeachRequest beachRequest = new BeachRequest(operationType);
        beachRequest.setPath(path);
        beachRequest.setPayload(payload);
        return beachRequest;
    }

    private BeachRequest(int operationType) {
        this.operationType = operationType;
    }

    int getOperationType() {
        return operationType;
    }

    String getPayload() {
        return payload;
    }

    int getPage() {
        return page;
    }

    String getPath() {
        return path;
    }

    private void setPayload(String payload) {
        this.payload = payload;
    }

    private void setPage(int page) {
        this.page = page;
    }

    private void setPath(String path) {
        this.path = path;
    }
}
