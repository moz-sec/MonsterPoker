public class Monster {
    private String name;
    private int attackPoint;
    private int defensePoint;

    public Monster(String name, int attackPoint, int defensePoint) {
        this.name = name;
        this.attackPoint = attackPoint;
        this.defensePoint = defensePoint;
    }

    public String getName() {
        return this.name;
    }

    public int getAttackPoint() {
        return this.attackPoint;
    }

    public int getDefensePoint() {
        return this.defensePoint;
    }
}
