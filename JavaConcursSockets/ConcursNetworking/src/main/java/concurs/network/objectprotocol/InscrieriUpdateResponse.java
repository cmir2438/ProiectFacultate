package concurs.network.objectprotocol;

import concurs.model.DTOInscriere;

public class InscrieriUpdateResponse implements  UpdateResponse {
    private DTOInscriere[] inscrieri;

    public InscrieriUpdateResponse(DTOInscriere[] inscrieri) {
        this.inscrieri = inscrieri;
    }

    public DTOInscriere[] getInscrieri() {
        return inscrieri;
    }
}
