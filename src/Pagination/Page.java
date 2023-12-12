package Pagination;

import lombok.Getter;

@Getter
public abstract class Page {
    private final String owner;

    public Page(String owner) {
        this.owner = owner;
    }

    public abstract String displayPage();
}



