package pagination;

import app.user.User;
import lombok.Getter;

@Getter
public abstract class Page {
    private final User owner;

    /**
     * Constructor for Page class.
     *
     * @param owner The user who owns this page.
     */
    public Page(final User owner) {
        this.owner = owner;
    }

    /**
     * Abstract method to display the content of the page.
     *
     * @return A string representing the displayed page content.
     */
    public abstract String displayPage();
}
