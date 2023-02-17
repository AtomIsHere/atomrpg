package com.github.atomishere.atomrpg.entity.attributes;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;
import com.github.atomishere.atomrpg.attributes.BaseModifier;

import java.util.ArrayList;
import java.util.List;

public class AtomEntityAttributeInstance {
    private final AtomAttribute attribute;

    private double baseValue;
    private final List<BaseModifier> modifiers = new ArrayList<>();

    public AtomEntityAttributeInstance(AtomAttribute attribute, double baseValue) {
        this.attribute = attribute;
        this.baseValue = baseValue;
    }

    public AtomAttribute getAttribute() {
        return attribute;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public double getValue() {
        double value = getBaseValue();
        double scalar = 1.0D;

        for(BaseModifier modifier : modifiers) {
            switch (modifier.operation()) {
                case ADDITION -> value += modifier.value();
                case SUBTRACTION -> value -= modifier.value();
                case MULTIPLICATION -> scalar *= modifier.value();
                case DIVISION -> scalar /= modifier.value();
            }
        }

        return value * scalar;
    }

    public List<BaseModifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(BaseModifier modifier) {
        modifiers.add(modifier);
    }

    public void removeModifier(BaseModifier modifier) {
        modifiers.remove(modifier);
    }
}
