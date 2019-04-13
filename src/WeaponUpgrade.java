package src;

public class WeaponUpgrade extends PowerUp {
    private boolean status;

    WeaponUpgrade (boolean stat){
        this.status = stat;
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
