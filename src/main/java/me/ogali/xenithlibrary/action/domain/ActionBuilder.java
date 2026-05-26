package me.ogali.xenithlibrary.action.domain;

import me.ogali.xenithlibrary.shared.DomainConfig;

@FunctionalInterface
public interface ActionBuilder {
    AbstractAction build(DomainConfig config);
}