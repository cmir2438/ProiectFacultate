package concurs.network.objectprotocol;

import concurs.model.DTOInscriere;

public class GetInscrieriResponse implements Response {
    private DTOInscriere[] inscrieri;

    public GetInscrieriResponse(DTOInscriere[] inscrieri) {
        this.inscrieri = inscrieri;
    }

    public DTOInscriere[] getInscrieri() {
        return inscrieri;
    }
}
