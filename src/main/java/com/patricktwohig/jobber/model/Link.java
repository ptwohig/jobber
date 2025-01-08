package com.patricktwohig.jobber.model;

import java.util.Objects;

public class Link {

    private String url;

    private LinkType linkType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Link{");
        sb.append("url='").append(url).append('\'');
        sb.append(", linkType=").append(linkType);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(getUrl(), link.getUrl()) && getLinkType() == link.getLinkType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getLinkType());
    }

}
