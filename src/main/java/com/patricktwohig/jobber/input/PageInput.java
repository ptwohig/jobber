package com.patricktwohig.jobber.input;

import java.io.IOException;

public interface PageInput {

    String loadPage(String url) throws IOException;

}
