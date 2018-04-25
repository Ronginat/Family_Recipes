package ronapplication.com.recipes.Model;

public class Recipe {
    public int id;
    public String fileName, name, description, holiday, kind, subKind, kashrut;
    //public String category2;

    public Recipe() {
    }

    public Recipe(int id, String fileName, String name, String description, String kind) {

        this.id = id;
        this.fileName = fileName;
        this.name = name;
        this.description = description;
        this.kind = kind;
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

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getSubKind() {
        return subKind;
    }

    public void setSubKind(String subKind) {
        this.subKind = subKind;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getKashrut() {
        return kashrut;
    }

    public void setKashrut(String kashrut) {
        this.kashrut = kashrut;
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
