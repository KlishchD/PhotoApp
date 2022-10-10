package com.main.photoapp.models.Desk.OwnersMapping;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public class DeskOwnerMappingId implements Serializable {
    @Getter @Setter
    private int deskId;
    @Getter @Setter
    private int userId;

    public DeskOwnerMappingId(){
    }
    public DeskOwnerMappingId(int deskId, int userId) {
        this.deskId = deskId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeskOwnerMappingId that = (DeskOwnerMappingId) o;
        return deskId == that.deskId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deskId, userId);
    }
}
