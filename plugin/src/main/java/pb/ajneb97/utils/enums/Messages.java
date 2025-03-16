package pb.ajneb97.utils.enums;

public enum Messages {

    HELP_MESSAGE("MAIN.HELP"),
    NO_PERMISSION("NO_PERMISSION"),
    RELOADED("CONFIG_RELOADED"),

    // COMMAND MANAGER MESSAGES
    COMMAND_MANAGER_HEADER("COMMAND_MANAGER.USAGE.HEADER"),
    COMMAND_MANAGER_PREFIX("COMMAND_MANAGER.USAGE.PREFIX"),
    COMMAND_MANAGER_MESSAGE("COMMAND_MANAGER.USAGE.MESSAGE"),
    COMMAND_MANAGER_PLAYER_NOT_FOUND("COMMAND_MANAGER.PLAYER_N0T_FOUND"),
    COMMAND_MANAGER_ONLY_PLAYER("COMMAND_MANAGER.TRANSLATIONS.ONLY_PLAYERS"),
    COMMAND_MANAGER_NO_PERMISSION("COMMAND_MANAGER.TRANSLATIONS.NO_PERMISSION"),

    // TOP MESSAGES
    TOP_KILLS_NONE("TOP_KILLS_NONE"),

    // INVENTORY MESSAGES

    // EDIT INVENTORY ITEMS
    EDIT_MENU_TITLE("INVENTORIES.EDIT.TITLE"),
    EDIT_MENU_SET_LOBBY_NAME("INVENTORIES.EDIT.SET.LOBBY.NAME"),
    EDIT_MENU_SET_LOBBY_LORE("INVENTORIES.EDIT.SET.LOBBY.LORE"),
    EDIT_MENU_SET_TEAM_SPAWN_NAME("INVENTORIES.EDIT.SET.SPAWN.NAME"),
    EDIT_MENU_SET_TEAM_SPAWN_LORE("INVENTORIES.EDIT.SET.SPAWN.LORE"),
    EDIT_MENU_SET_MIN_PLAYERS_NAME("INVENTORIES.EDIT.SET.MIN_PLAYERS.NAME"),
    EDIT_MENU_SET_MIN_PLAYERS_LORE("INVENTORIES.EDIT.SET.MIN_PLAYERS.LORE"),
    EDIT_MENU_SET_MAX_PLAYERS_NAME("INVENTORIES.EDIT.SET.MAX_PLAYERS.NAME"),
    EDIT_MENU_SET_MAX_PLAYERS_LORE("INVENTORIES.EDIT.SET.MAX_PLAYERS.LORE"),
    EDIT_MENU_SET_MAX_TIME_NAME("INVENTORIES.EDIT.SET.MAX_TIME.NAME"),
    EDIT_MENU_SET_MAX_TIME_LORE("INVENTORIES.EDIT.SET.MAX_TIME.LORE"),
    EDIT_MENU_SET_STARTING_LIVES_NAME("INVENTORIES.EDIT.SET.STARTING_LIVES.NAME"),
    EDIT_MENU_SET_STARTING_LIVES_LORE("INVENTORIES.EDIT.SET.STARTING_LIVES.LORE"),

    // ARENA MESSAGES
    NOT_IN_GAME("NOT_IN_A_GAME"),
    ALREADY_IN_ARENA("ALREADY_IN_ARENA"),
    ARENA_DOES_NOT_EXIST("ARENA_DOES_NOT_EXIST"),
    ARENA_ALREADY_EXISTS("ARENA_ALREADY_EXISTS"),
    ARENA_CREATED("ARENA_CREATED"),
    ARENA_DELETED("ARENA_DELETED"),
    ARENA_CREATED_EXTRA_INFO("ARENA_CREATED_EXTRA_INFO"),
    ARENA_ENABLED("ARENA_ENABLED"),
    ARENA_DISABLED("ARENA_DISABLED"),
    ARENA_DISABLED_ERROR("ARENA_DISABLED_ERROR"),
    ARENA_STARTING_MESSAGE("ARENA_STARTING_MESSAGE"),
    ARENA_STARTING_BROADCAST("ARENA_STARTING_BROADCAST"),
    ARENA_ALREADY_STARTED("ARENA_ALREADY_STARTED"),
    ARENA_IS_FULL("ARENA_IS_FULL"),
    ARENA_ALREADY_ENABLED("ARENA_ALREADY_ENABLED"),
    ARENA_ALREADY_DISABLED("ARENA_ALREADY_DISABLED"),
    ARENA_MUST_BE_DISABLED("ARENA_MUST_BE_DISABLED"),
    ARENA_MODIFYING_ERROR("ARENA_MODIFYING_ERROR"),
    ENABLE_ARENA_LOBBY_ERROR("ENABLE_ARENA_LOBBY_ERROR"),
    ENABLE_ARENA_SPAWN_ERROR("ENABLE_ARENA_SPAWN_ERROR"),

    // LOBBY AND SPAWN MESSAGES
    NO_MAIN_LOBBY("NO_MAIN_LOBBY"),
    LOBBY_DEFINED("LOBBY_DEFINED"),
    SPAWN_TEAM_DEFINED("SPAWN_TEAM_DEFINED"),
    MAIN_LOBBY_DEFINED("MAIN_LOBBY_DEFINED"),
    TYPE_DEFINED("TYPE_DEFINED"),
    MIN_PLAYERS_DEFINED("MIN_PLAYERS_DEFINED"),
    MAX_PLAYERS_DEFINED("MAX_PLAYERS_DEFINED"),
    TIME_DEFINED("TIME_DEFINED"),
    LIVES_DEFINED("LIVES_DEFINED"),

    // GAME MESSAGES
    GAME_STARTED("GAME_STARTED"),
    GAME_FINISHED("GAME_FINISHED"),
    GAME_FINISHED_WINNER_STATUS("GAME_FINISHED_WINNER_STATUS"),
    GAME_FINISHED_TIE_STATUS("GAME_FINISHED_TIE_STATUS"),
    GAME_STARTING_CANCELLED("GAME_STARTING_CANCELLED"),
    PLAYER_JOIN("PLAYER_JOIN"),
    PLAYER_LEAVE("PLAYER_LEAVE"),
    TEAM_INFORMATION("TEAM_INFORMATION"),
    TEAM_SELECTED("TEAM_SELECTED"),
    ERROR_TEAM_SELECTED("ERROR_TEAM_SELECTED"),
    ERROR_TEAM_ALREADY_SELECTED("ERROR_TEAM_ALREADY_SELECTED"),
    TEAM_INFORMATION_NONE("TEAM_INFORMATION_NONE"),
    KILLED_BY("KILLED_BY"),
    KILL("KILL"),
    TEAM_CHOOSE("TEAM_CHOOSE"),

    // SCOREBOARD AND STATUS MESSAGES
    GAME_SCOREBOARD_TITLE("GAME_SCOREBOARD.TITLE"),
    GAME_SCOREBOARD_BODY("GAME_SCOREBOARD.BODY"),
    STATUS_WAITING("STATUS_WAITING"),
    STATUS_STARTING("STATUS_STARTING"),
    STATUS_INGAME("STATUS_INGAME"),
    STATUS_FINISHING("STATUS_FINISHING"),

    // SIGN MESSAGES
    SIGN_FORMAT("SIGN_FORMAT"),
    SIGN_STATUS_WAITING("SIGN_STATUS_WAITING"),
    SIGN_STATUS_STARTING("SIGN_STATUS_STARTING"),
    SIGN_STATUS_INGAME("SIGN_STATUS_INGAME"),
    SIGN_STATUS_DISABLED("SIGN_STATUS_DISABLED"),
    SIGN_STATUS_FINISHING("SIGN_STATUS_FINISHING"),

    // KILLSTREAK MESSAGES
    KILLSTREAK_ACTIVATED("KILLSTREAK_ACTIVATED"),
    KILLSTREAK_ALREADY_ACTIVATED("KILLSTREAK_ALREADY_ACTIVATED"),
    KILLSTREAK_EXPIRED("KILLSTREAK_EXPIRED"),
    KILLSTREAK_CURRENTLY_ACTIVE("KILLSTREAK_CURRENTLY_ACTIVE"),
    KILLSTREAK_ACTIONBAR("KILLSTREAK_ACTIONBAR"),
    KILLSTREAK_ACTIVATED_PLAYER("KILLSTREAK_ACTIVATED_PLAYER"),

    // NUKE MESSAGES
    NUKE_IMPACT("NUKE_IMPACT"),
    NUKE_ERROR("NUKE_ERROR"),
    NUKE_KILL_MESSAGE("NUKE_KILL_MESSAGE"),

    // COINS AND ECONOMY MESSAGES
    NO_SUFFICIENT_COINS("NO_SUFFICIENT_COINS"),
    GIVE_COINS_MESSAGE("GIVE_COINS_MESSAGE"),
    RECEIVE_COINS_MESSAGE("RECEIVE_COINS_MESSAGE"),
    BUY_NO_SUFFICIENT_COINS("BUY_NO_SUFFICIENT_COINS"),

    // PERK AND HAT MESSAGES
    PERK_ERROR_PREVIOUS("PERK_ERROR_PREVIOUS"),
    PERK_ERROR_UNLOCKED("PERK_ERROR_UNLOCKED"),
    PERK_UNLOCKED("PERK_UNLOCKED"),
    HAT_ERROR_BOUGHT("HAT_ERROR_BOUGHT"),
    HAT_BOUGHT("HAT_BOUGHT"),
    HAT_STATUS_SELECTED("HAT_STATUS_SELECTED"),
    HAT_STATUS_NOT_SELECTED("HAT_STATUS_NOT_SELECTED"),
    HAT_SELECTED("HAT_SELECTED"),
    HAT_ALREADY_SELECTED("HAT_ALREADY_SELECTED"),
    HAT_REMOVED("HAT_REMOVED"),
    PRESENT_HAT_RECEIVE("PRESENT_HAT_RECEIVE"),
    PRESENT_HAT_GIVE("PRESENT_HAT_GIVE"),
    HAT_ABILITY_ACTIVATED("HAT_ABILITY_ACTIVATED"),
    HAT_COOLDOWN_ERROR("HAT_COOLDOWN_ERROR"),
    HAT_COOLDOWN_FINISHED("HAT_COOLDOWN_FINISHED"),

    // ERROR MESSAGES
    ERROR_CLEAR_INVENTORY("ERROR_CLEAR_INVENTORY"),
    ERROR_PLAYER_ONLINE("ERROR_PLAYER_ONLINE"),
    NO_ARENAS_AVAILABLE("NO_ARENAS_AVAILABLE"),
    VALID_NUMBER_ERROR("VALID_NUMBER_ERROR"),
    MATERIAL_NAME_ERROR("MATERIAL_NAME_ERROR");

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}