package model;

import java.util.Date;

public abstract class BaseModel implements java.io.Serializable {

    private static final long serialVersionUID = -215738452477748132L;

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract boolean isDeleted();

    public abstract void setDeleted(boolean deleted);

    public abstract Date getDeletedAt();

    public abstract void setDeletedAt(Date deletedAt);

    public abstract Date getCreatedAt();

    public abstract void setCreatedAt(Date createdAt);

    public abstract Date getUpdatedAt();

    public abstract void setUpdatedAt(Date updatedAt);

}
