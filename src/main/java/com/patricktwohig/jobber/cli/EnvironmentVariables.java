package com.patricktwohig.jobber.cli;

import java.nio.charset.Charset;

public enum EnvironmentVariables {
    
    OPENAI_API_KEY;

    public InputLine getInputLine() {
        return new InputLine(Format.ENV, name(), Charset.defaultCharset());
    }

}
