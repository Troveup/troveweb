package com.troveup.brooklyn.orm.simpleitem.model;

import javax.jdo.annotations.*;

/**
 * Created by tim on 3/30/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class UserControlSelection
{
    private Long userControlSelectionId;
    private Long simpleItemControlId;
    private Long simpleItemControlOptionId;
    private String simpleItemControlTitle;
    private String simpleItemControlOptionLabel;

    public UserControlSelection()
    {

    }

    public UserControlSelection(UserControlSelection selection)
    {
        this.simpleItemControlId = selection.getSimpleItemControlId();
        this.simpleItemControlOptionId = selection.getSimpleItemControlOptionId();
        this.simpleItemControlTitle = selection.getSimpleItemControlTitle();
        this.simpleItemControlOptionLabel = selection.getSimpleItemControlOptionLabel();
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getUserControlSelectionId() {
        return userControlSelectionId;
    }

    public void setUserControlSelectionId(Long userControlSelectionId) {
        this.userControlSelectionId = userControlSelectionId;
    }

    @Persistent
    public Long getSimpleItemControlId() {
        return simpleItemControlId;
    }

    public void setSimpleItemControlId(Long simpleItemControlId) {
        this.simpleItemControlId = simpleItemControlId;
    }

    @Persistent
    public Long getSimpleItemControlOptionId() {
        return simpleItemControlOptionId;
    }

    public void setSimpleItemControlOptionId(Long simpleItemControlOptionId) {
        this.simpleItemControlOptionId = simpleItemControlOptionId;
    }

    @Persistent
    public String getSimpleItemControlTitle() {
        return simpleItemControlTitle;
    }

    public void setSimpleItemControlTitle(String simpleItemControlTitle) {
        this.simpleItemControlTitle = simpleItemControlTitle;
    }

    @Persistent
    public String getSimpleItemControlOptionLabel() {
        return simpleItemControlOptionLabel;
    }

    public void setSimpleItemControlOptionLabel(String simpleItemControlOptionLabel) {
        this.simpleItemControlOptionLabel = simpleItemControlOptionLabel;
    }
}
