package src;

public class Shield extends PowerUp {
    private boolean status;

    Shield(boolean stat){
        this.status = false;
    }
    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public boolean setStatus(boolean toSet) {
        return this.status = toSet;
    }
}