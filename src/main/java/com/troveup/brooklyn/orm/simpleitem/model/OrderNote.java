package com.troveup.brooklyn.orm.simpleitem.model;

import com.troveup.brooklyn.orm.user.model.User;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tim on 4/1/16.
 */
@PersistenceCapable
@Cacheable(value = "false")
public class OrderNote
{
    private Long noteId;
    private String note;
    private Date creationDate;
    private String userFriendlyOrderNoteDate;
    private User userCreator;
    private Boolean active;

    public OrderNote()
    {
        active = true;
    }

    @PrimaryKey
    @Persistent(valueStrategy= IdGeneratorStrategy.IDENTITY)
    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    @Persistent
    @Column(length = 2000)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Persistent
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @NotPersistent
    public String getUserFriendlyOrderNoteDate() {
        return userFriendlyOrderNoteDate;
    }

    public void setUserFriendlyOrderNoteDate(String userFriendlyOrderNoteDate) {
        this.userFriendlyOrderNoteDate = userFriendlyOrderNoteDate;
    }

    @Persistent
    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }

    @Persistent
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public static List<String> getFullFetchGroupFields()
    {
        List<String> rval = new ArrayList<>();
        rval.add("userCreator");

        return rval;
    }
}
