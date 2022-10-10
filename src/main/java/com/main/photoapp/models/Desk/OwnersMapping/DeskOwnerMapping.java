package com.main.photoapp.models.Desk.OwnersMapping;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "desks_owners")
@IdClass(DeskOwnerMappingId.class)
@Entity
public class DeskOwnerMapping {

    @Id
    @Column(name = "desk_id", nullable = false)
    @Getter @Setter
    private int deskId;

    @Id
    @Column(name = "user_id", nullable = false)
    @Getter @Setter
    private int userId;

    @Column(name="permission_level", nullable = false)
    @Getter @Setter
    private Permission permission;

    public DeskOwnerMapping() {}

    public DeskOwnerMapping(int deskId, int userId, Permission permission) {
        this.deskId = deskId;
        this.userId = userId;
        this.permission = permission;
    }

    public enum Permission {
        NO_PERMISSIONS {
            @Override
            public int getLevel() {
                return 0;
            }
        },
        VIEW_ONLY_PERMISSION {
            @Override
            public boolean canAccessDesk() {
                return true;
            }

            @Override
            public int getLevel() {
                return 1;
            }
        },
        VIEW_AND_MANAGE_PHOTOS_PERMISSION {
            @Override
            public boolean canAccessDesk() {
                return true;
            }

            @Override
            public int getLevel() {
                return 2;
            }

            public boolean canAddPhoto() {
                return true;
            }

            public boolean canRemovePhoto() {
                return true;
            }
        },
        FULL_PERMISSION {
            @Override
            public boolean canAccessDesk() {
                return true;
            }

            @Override
            public boolean canRemoveOwner() {
                return true;
            }

            @Override
            public int getLevel() {
                return 3;
            }

            @Override
            public boolean canAddOwner() {
                return true;
            }


            public boolean canAddPhoto() {
                return true;
            }

            public boolean canRemovePhoto() {
                return true;
            }
        },
        CREATOR_PERMISSION{
            @Override
            public boolean canAccessDesk() {
                return true;
            }

            @Override
            public boolean canRemoveOwner() {
                return true;
            }

            @Override
            public int getLevel() {
                return 4;
            }

            @Override
            public boolean canAddOwner() {
                return true;
            }

            @Override
            public boolean canDeleteDesk() {
                return true;
            }


            public boolean canAddPhoto() {
                return true;
            }

            public boolean canRemovePhoto() {
                return true;
            }
        };

        public boolean canAccessDesk() {
            return false;
        }

        public boolean canAddOwner() {
            return false;
        }

        public boolean canRemoveOwner() {
            return false;
        }

        public boolean canDeleteDesk() {
            return false;
        }

        public abstract int getLevel();

        public boolean canAddPhoto() {
            return false;
        }

        public boolean canRemovePhoto() {
            return false;
        }
    }
}
