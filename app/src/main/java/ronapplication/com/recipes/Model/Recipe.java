package ronapplication.com.recipes.Model;

public class Recipe {
    public int id;
    public String fileName, name, description, category1;
    //public String category2;

    public Recipe() {
    }

    public Recipe(int id, String fileName, String name, String description, String category1) {

        this.id = id;
        this.fileName = fileName;
        this.name = name;
        this.description = description;
        this.category1 = category1;
        //this.category2 = category2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }
    /*
    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }
    */
}
