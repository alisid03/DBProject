package DBManager;
import java.util.Objects;

public class Data {
    
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String gender;
    private String major;
    private String address;

    public Data() {
    }

    public Data(int id, String first_name, String last_name, String email, String gender, String major, String address) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.major = major;
        this.address = address;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Data id(int id) {
        setId(id);
        return this;
    }

    public Data first_name(String first_name) {
        setFirst_name(first_name);
        return this;
    }

    public Data last_name(String last_name) {
        setLast_name(last_name);
        return this;
    }

    public Data email(String email) {
        setEmail(email);
        return this;
    }

    public Data gender(String gender) {
        setGender(gender);
        return this;
    }

    public Data major(String major) {
        setMajor(major);
        return this;
    }

    public Data address(String address) {
        setAddress(address);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Data)) {
            return false;
        }
        Data data = (Data) o;
        return id == data.id && Objects.equals(first_name, data.first_name) && Objects.equals(last_name, data.last_name) && Objects.equals(email, data.email) && Objects.equals(gender, data.gender) && Objects.equals(major, data.major) && Objects.equals(address, data.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, first_name, last_name, email, gender, major, address);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", first_name='" + getFirst_name() + "'" +
            ", last_name='" + getLast_name() + "'" +
            ", email='" + getEmail() + "'" +
            ", gender='" + getGender() + "'" +
            ", major='" + getMajor() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }

}
