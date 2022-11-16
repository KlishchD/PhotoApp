package com.main.photoapp.models.Desk.OwnersMapping;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Table(name = "desks_owners")
@IdClass(DeskOwnerMappingId.class)
@Entity
public class DeskOwnerMapping {

    @Id
    @Column(name = "desk_id", nullable = false)
    @Getter
    @Setter
    private int deskId;

    @Id
    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    private int userId;

    @Column(name = "permission_level", nullable = false)
    @Getter
    @Setter
    private Permission permission;

    public DeskOwnerMapping() {
    }

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

            @Override
            public boolean canAccessPublicDesk() {
                return true;
            }
        },
        VIEW_ONLY_PERMISSION {
            @Override
            public boolean canAccessPublicDesk() {
                return true;
            }

            @Override
            public boolean canAccessPrivateDesk() {
                return true;
            }

            @Override
            public int getLevel() {
                return 1;
            }
        },
        PHOTO_MANAGER_PERMISSION {
            @Override
            public boolean canAccessPublicDesk() {
                return true;
            }

            @Override
            public boolean canAccessPrivateDesk() {
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
            public boolean canAccessPublicDesk() {
                return true;
            }

            @Override
            public boolean canAccessPrivateDesk() {
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

            public boolean canModifyDeskInformation() {
                return true;
            }

        },
        CREATOR_PERMISSION {
            @Override
            public boolean canAccessPublicDesk() {
                return true;
            }

            @Override
            public boolean canAccessPrivateDesk() {
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

            public boolean canModifyDeskInformation() {
                return true;
            }

        };

        public boolean canAccessPublicDesk() {
            return false;
        }

        public static List<Permission> permissionsCanAccessPublicDesk() {
            return Arrays.stream(values()).filter(Permission::canAccessPublicDesk).toList();
        }

        public boolean canAccessPrivateDesk() {
            return false;
        }


        public static List<Permission> permissionsCanAccessPrivateDesk() {
            return Arrays.stream(values()).filter(Permission::canAccessPrivateDesk).toList();
        }

        public boolean canAddOwner() {
            return false;
        }

        public static List<Permission> permissionsCanAddOwner() {
            return Arrays.stream(values()).filter(Permission::canAddOwner).toList();
        }

        public boolean canRemoveOwner() {
            return false;
        }


        public static List<Permission> permissionsCanRemoveOwner() {
            return Arrays.stream(values()).filter(Permission::canRemoveOwner).toList();
        }

        public boolean canDeleteDesk() {
            return false;
        }

        public static List<Permission> permissionsCanDeleteDesk() {
            return Arrays.stream(values()).filter(Permission::canDeleteDesk).toList();
        }

        public abstract int getLevel();

        public boolean canAddPhoto() {
            return false;
        }

        public static List<Permission> permissionsCanAddPhoto() {
            return Arrays.stream(values()).filter(Permission::canAddPhoto).toList();
        }

        public boolean canRemovePhoto() {
            return false;
        }

        public static List<Permission> permissionsCanRemovePhoto() {
            return Arrays.stream(values()).filter(Permission::canRemovePhoto).toList();
        }

        public boolean canModifyDeskInformation() {
            return false;
        }

        public static List<Permission> permissionsCanModifyDeskInformation() {
            return Arrays.stream(values()).filter(Permission::canModifyDeskInformation).toList();
        }

    }
}
