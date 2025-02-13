package com.patricktwohig.jobber.input.htmlunit;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.patricktwohig.jobber.input.PageInput;

import java.io.IOException;

public class HtmlUnitPageInput implements PageInput {
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
