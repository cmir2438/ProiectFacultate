package concurs.network.objectprotocol;

import concurs.model.DTOInscrie;

public class InscriereRequest implements Request {
    private DTOInscrie inscriere;

    public InscriereRequest(DTOInscrie inscriere) {
        this.inscriere = inscriere;
    }

    public DTOInscrie getInscriere() {
        return inscriere;
    }
}
