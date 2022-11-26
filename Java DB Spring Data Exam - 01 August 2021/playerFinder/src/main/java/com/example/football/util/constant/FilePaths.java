package com.example.football.util.constant;

import java.nio.file.Path;

public enum FilePaths {
    ;

    public static final Path TOWNS_IMPORT_JSON_PATH = Path.of("src", "main", "resources", "files", "json", "towns.json");
    public static final Path TEAMS_IMPORT_JSON_PATH = Path.of("src", "main", "resources", "files", "json", "teams.json");
    public static final Path STATS_IMPORT_XML_PATH = Path.of("src", "main", "resources", "files", "xml", "stats.xml");
    public static final Path PLAYERS_IMPORT_XML_PATH = Path.of("src", "main", "resources", "files", "xml", "players.xml");
}
