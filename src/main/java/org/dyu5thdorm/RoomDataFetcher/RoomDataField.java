package org.dyu5thdorm.RoomDataFetcher;

public enum RoomDataField {
    STEP_SIZE(10),
    ROOM_TAG_INDEX(11),
    MAJOR_OFFSET(2),
    STUDENT_ID_OFFSET(3),
    NAME_OFFSET(4),
    SEX_OFFSET(5),
    CITIZENSHIP_OFFSET(6),
    TIME_OFFSET(9);

    private final int value;

    RoomDataField(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
