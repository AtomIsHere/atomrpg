package com.github.atomishere.atomrpg.entity.attributes;

import com.github.atomishere.atomrpg.attributes.AtomAttribute;

import java.util.Optional;

public interface AtomEntityAttributable {
    Optional<AtomEntityAttributeInstance> getAttribute(AtomAttribute attribute);
}
