package tw.edu.ncku.csie.javaminesweeper;

public enum Level {
    EASY, MEDIUM, HARD;

    public static final int LEVEL_COUNT = 3;
    
    public static Level currLevel = EASY;

    
    public static void setLevel(int level) {
        switch (level) {
            case 0:
                currLevel = EASY;
                break;
            case 1:
                currLevel = MEDIUM;
                break;
            case 2:
                currLevel = HARD;
                break;
            default:
                currLevel = EASY;
                break;
        }
    }

    public static void setLevel(Level level) {
        currLevel = level;
    }

    public static String getLevelString() {
        switch (currLevel) {
            case EASY:
                return "Easy";
            case MEDIUM:
                return "Medium";
            case HARD:
                return "Hard";
            default:
                return "Easy";
        }
    }

    public static int getLevelInt() {
        switch (currLevel) {
            case EASY:
                return 0;
            case MEDIUM:
                return 1;
            case HARD:
                return 2;
            default:
                return 0;
        }
    }

    public static void changeLevel(int i) {
        currLevel = values()[(getLevelInt() + i + LEVEL_COUNT) % LEVEL_COUNT];
    }
}
