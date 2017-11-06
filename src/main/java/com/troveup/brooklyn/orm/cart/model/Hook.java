package com.troveup.brooklyn.orm.cart.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 11/15/15.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class Hook
{
    public enum HOOK_SPECIFIER
    {
        GIFT_CARD,
        SIMPLE_ITEM
    }

    private Long hookId;
    private HOOK_SPECIFIER hookSpecifier;

    public Hook()
    {

    }

    public Hook(HOOK_SPECIFIER hook)
    {
        this.hookSpecifier = hook;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getHookId() {
        return hookId;
    }

    public void setHookId(Long hookId) {
        this.hookId = hookId;
    }

    @Persistent
    public HOOK_SPECIFIER getHookSpecifier() {
        return hookSpecifier;
    }

    public void setHookSpecifier(HOOK_SPECIFIER hookSpecifier) {
        this.hookSpecifier = hookSpecifier;
    }

}
