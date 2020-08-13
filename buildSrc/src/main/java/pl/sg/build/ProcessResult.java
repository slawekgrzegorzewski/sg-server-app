package pl.sg.build;

public class ProcessResult {
    private final String output;
    private final String error;
    private final Integer exitCode;

    public ProcessResult(String output, String error, Integer exitCode) {
        this.output = output;
        this.error = error;
        this.exitCode = exitCode;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public Integer getExitCode() {
        return exitCode;
    }
}
