package pl.kuropatva.server;


// Probably should be singleton
public class SystemLocalisedCommands {

    private final OS os = System.getProperty("os.name").toLowerCase().startsWith("windows") ? OS.WINDOWS : OS.UNIX;

    public enum Command {
        RUN("%s", "./%s");

        private String windows;
        private String unix;

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

        public void setWindows(String s) {
            this.windows = s;
        }

        public void setUnix(String s) {
            this.unix = s;
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
