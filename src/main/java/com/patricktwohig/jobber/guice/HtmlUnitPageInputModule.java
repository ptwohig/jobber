package com.patricktwohig.jobber.guice;

import com.google.inject.PrivateModule;
import com.patricktwohig.jobber.input.PageInput;
import com.patricktwohig.jobber.input.htmlunit.HtmlUnitPageInput;

public class HtmlUnitPageInputModule extends PrivateModule {
    @Override
    protected void configure() {
        expose(PageInput.class);
        bind(PageInput.class).to(HtmlUnitPageInput.class);
    }
}
