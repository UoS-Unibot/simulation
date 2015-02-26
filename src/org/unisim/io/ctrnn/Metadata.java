package org.unisim.io.ctrnn;

/**
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public class Metadata {

        private String author;
        private String title;
        private String description;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Metadata{" + "author=" + author + ", title=" + title + ", description=" + description + '}';
        }
        
        
    }