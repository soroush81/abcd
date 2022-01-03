package ir.soodeh.mancala.utils;

import static ir.soodeh.mancala.constants.KalahaConstants.*;

public class ManCalaUtils {
    private ManCalaUtils(){

    }
    public static boolean isCala(Integer i) {
        return i == LAST_IDX/2 || i == LAST_IDX;
    }
}
