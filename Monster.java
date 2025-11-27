class Monster extends Character {
    private int baseDamage;
    private int defense;
    private double baseDodge;

    public Monster(MonsterTemplate t, int level) {
        super(t.getName(), level, 0,0,0);
        this.baseDamage = t.getBaseDamage() + level*2;
        this.defense = t.getDefense() + level;
        this.baseDodge = t.getDodge();
        this.hp = level * 100;
    }

    public double attackDamage() { return baseDamage; }
    public double getDodgeChance() { return baseDodge; }
    public void reduceStatByPercent(double pct) {
        baseDamage *= (1 - pct);
        defense *= (1 - pct);
        baseDodge *= (1 - pct);
    }

    public int getDefense() { return defense; }

    @Override
    public void endRoundRegen() {
        // Monsters do NOT regenerate HP or MP between rounds
    }

}