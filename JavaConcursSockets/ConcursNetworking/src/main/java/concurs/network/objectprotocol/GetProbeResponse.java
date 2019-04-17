package concurs.network.objectprotocol;

import concurs.model.Proba;

public class GetProbeResponse implements Response {
    private Proba[] probe;

    public GetProbeResponse(Proba[] probe) {
        this.probe = probe;
    }

    public Proba[] getProbe() {
        return probe;
    }
}
