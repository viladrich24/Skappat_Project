package viladrich.arnau.final_project.Activities.rankingClasses;

public class Contact {
    private int pos;
    private int icon;
    private String name;
    private String phone;
    private String record;

    Contact(int pos, int icon, String name, String phone, String record){
        this.pos = pos;
        this.icon = icon;
        this.name = name;
        this.phone = phone;
        this.record = record;

    }
    Contact(){
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
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
