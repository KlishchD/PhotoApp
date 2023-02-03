package com.main.photoapp.utils;

import com.main.photoapp.models.Desk.Desk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesksTypesMapping {
    private static final Map<String, List<Desk.DeskType>> typesMapping = new HashMap<>();

    static {
        typesMapping.put("PUBLIC", List.of(Desk.DeskType.PUBLIC));
        typesMapping.put("PRIVATE", List.of(Desk.DeskType.PRIVATE));
        typesMapping.put("ALL", List.of(Desk.DeskType.values()));
    }

    public static Map<String, List<Desk.DeskType>> getTypesMapping() {
        return typesMapping;
    }
}
