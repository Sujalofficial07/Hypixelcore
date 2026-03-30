package com.sujal.skyblockcore.models;

import com.yournetwork.hypixelconnect.models.StatType;

public class StatModifier {
    private final String source;
    private final StatType stat;
    private final double amount;
    private final boolean isMultiplicative;

    public StatModifier(String source, StatType stat, double amount, boolean isMultiplicative) {
        this.source = source;
        this.stat = stat;
        this.amount = amount;
        this.isMultiplicative = isMultiplicative;
    }

    public String getSource() { return source; }
    public StatType getStat() { return stat; }
    public double getAmount() { return amount; }
    public boolean isMultiplicative() { return isMultiplicative; }
}
