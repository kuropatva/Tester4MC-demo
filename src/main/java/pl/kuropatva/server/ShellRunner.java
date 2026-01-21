package pl.kuropatva.server;

import java.io.IOException;
import java.nio.file.Path;

public class ShellRunner {

    private final SystemLocalisedCommands systemLocalisedCommands = new SystemLocalisedCommands();
    private Path dir = Path.of("/");

    public void setDir(Path dir) {
        System.out.println(dir);
        this.dir = dir;
    }

    public Process run() throws IOException, InterruptedException {
        var process = runWithCommand(systemLocalisedCommands.get(SystemLocalisedCommands.Command.RUN, "run.bat"), dir);
        System.out.println(process.pid());
        return process;
    }


    private Process runWithCommand(String command, Path path) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command.split(" "));
        processBuilder.directory(path.toFile());
        return processBuilder.start();
    }
}
