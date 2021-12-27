package ir.soodeh.mancala.utils;

import ir.soodeh.mancala.models.Board;

public class manCalaUtils {
    public static boolean isCala(Integer i) {
        return i == Board.LAST_IDX/2 || i == Board.LAST_IDX;
    }
}
