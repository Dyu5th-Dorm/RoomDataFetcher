package org.dyu5thdorm.RoomDataFetcher;

/**
 * Dormitory id.
 */
public enum Dormitory {
    DA_YEH('1'), FOUR_WILLING('2'), DILIGENT('5'), LE_CHUN('6');
    private final char id;
    Dormitory(char id) {
        this.id = id;
    }
    public char getId() {
        return id;
    }
}
