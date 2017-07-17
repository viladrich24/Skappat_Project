package viladrich.arnau.final_project.Activities.rankingClasses;

public class Contact {

    private String icon;
    private String name;
    private String phone;
    private String record;

    Contact(String icon, String name, String phone, String record){

        this.icon = icon;
        this.name = name;
        this.phone = phone;
        this.record = record;

    }
    Contact(){
    }

    public String getIcon() { return icon; }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
