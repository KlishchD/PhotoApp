package com.main.photoapp.utils;

import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionMapping {
    private static final Map<String, List<DeskOwnerMapping.Permission>> permissionMapping = new HashMap<>();

    static {
        permissionMapping.put("CREATOR", List.of(DeskOwnerMapping.Permission.CREATOR_PERMISSION));
        permissionMapping.put("FULL", List.of(DeskOwnerMapping.Permission.FULL_PERMISSION));
        permissionMapping.put("VIEW", List.of(DeskOwnerMapping.Permission.VIEW_ONLY_PERMISSION));
        permissionMapping.put("NONE", List.of(DeskOwnerMapping.Permission.NO_PERMISSIONS));
        permissionMapping.put("ALL", List.of(DeskOwnerMapping.Permission.values()));
    }

    public static Map<String, List<DeskOwnerMapping.Permission>> getPermissionMapping() {
        return permissionMapping;
    }
}
