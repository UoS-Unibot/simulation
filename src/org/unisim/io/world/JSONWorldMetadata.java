package org.unisim.io.world;

/**
 * POJO representation of metadata in a world JSON file.
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class JSONWorldMetadata {

    private String name;
    private String author;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
