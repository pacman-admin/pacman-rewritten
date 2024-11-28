import java.io.*;


class Preferences {
    static boolean autoUpdate = true;
    static boolean debug = true;
    static boolean mute = false;
    static boolean playPauseBeat = true;
    static int scale = 1;

    static void load() {
        //path = System.getProperty("user.home");
        try {
            DataInputStream din = new DataInputStream(new BufferedInputStream(new FileInputStream(path + "/dat")));
            debug = din.readBoolean();
        } catch (IOException e) {
            System.err.println("An Error occurred while loading.");
            save();
        }
    }

    static void save() {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path + "/dat")));
            dos.writeBoolean(debug);
            dos.writeBoolean(playPauseBeat);
            dos.close();
        } catch (IOException ee) {
            System.err.println("An Error occurred while saving settings.");
        }
    }

}