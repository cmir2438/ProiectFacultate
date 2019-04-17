package concurs.model;

public class Inscriere  implements HasID<Pair<String, String>>{
    private Pair<String, String> ID;

    public Inscriere(Pair<String, String> ID) {
        this.ID = ID;
    }
    public Inscriere (String idParticipant, String idProba){
        ID=new Pair<String,String>(idParticipant,idProba);

    }
    @Override
    public Pair<String, String> getID() {
        return ID;
    }

    @Override
    public void setID(Pair<String, String> id) {
        this.ID = id;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "ID=" + ID +
                '}';
    }


}
