package pl.kuropatva.server;


// Probably should be singleton
public class SystemLocalisedCommands {

    private final OS os = System.getProperty("os.name").toLowerCase().startsWith("windows") ? OS.WINDOWS : OS.UNIX;

    public enum Command {
        RUN("java -Xms2G -Xmx4G -DIReallyKnowWhatIAmDoingISwear -jar Spigot.jar", "./%s");

        private final String windows;
        private final String unix;

        Command(String windows, String unix) {
            this.windows = windows;
            this.unix = unix;
        }

        public String getWindows() {
            return windows;
        }

        public String getUnix() {
            return unix;
        }
    }

    public enum OS {
        WINDOWS, UNIX
    }

    public String get(Command command, Object... objects) {
        return String.format(switch (os) {
            case WINDOWS -> command.getWindows();
            case UNIX -> command.getUnix();
        }, objects);
    }
}
