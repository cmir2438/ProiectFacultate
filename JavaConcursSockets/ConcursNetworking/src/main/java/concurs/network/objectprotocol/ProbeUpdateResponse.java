package concurs.network.objectprotocol;

import concurs.model.Proba;

public class ProbeUpdateResponse implements UpdateResponse {
    private Proba[] probe;

    public ProbeUpdateResponse(Proba[] probe) {
        this.probe = probe;
    }

    public Proba[] getProbe() {
        return probe;
    }
}
