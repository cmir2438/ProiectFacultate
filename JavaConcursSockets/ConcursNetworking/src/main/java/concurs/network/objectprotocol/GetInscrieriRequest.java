package concurs.network.objectprotocol;

import concurs.model.Inscriere;

public class GetInscrieriRequest implements  Request {
    private String inscriere;

    public GetInscrieriRequest(String inscriere) {
        this.inscriere = inscriere;
    }

    public String getInscriere() {
        return inscriere;
    }
}
