package com.patricktwohig.jobber.input.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.patricktwohig.jobber.input.PageInput;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlUnitPageInput implements PageInput {

    static {
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
    }

    @Override
    public String loadPage(final String url) throws IOException {
        try (final var webClient = new WebClient()) {

            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            final HtmlPage page = webClient.getPage(url);
            return page.getBody().asNormalizedText();

        }
    }
    
}
