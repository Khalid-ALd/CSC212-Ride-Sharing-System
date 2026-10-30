
package csc212;

public class Rider extends Person implements IRider, Comparable<IRider> {

    private String email;
    private String homeCity;

    public Rider(int id, String name, String email,
                 String phoneNumber, String homeCity) {
        super(id, name, phoneNumber);
        setEmail(email);
        setHomeCity(homeCity);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        this.email = email.trim();
    }

    @Override
    public String getHomeCity() {
        return homeCity;
    }

    @Override
    public void setHomeCity(String homeCity) {
        if (homeCity == null || homeCity.trim().isEmpty()) {
            throw new IllegalArgumentException("Home city cannot be empty");
        }

        this.homeCity = homeCity.trim();
    }

    @Override
    public int compareTo(IRider other) {
        if (other == null) {
            throw new NullPointerException("Rider cannot be null");
        }

        return Integer.compare(getId(), other.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Rider)) {
            return false;
        }

        Rider other = (Rider) o;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rider{ID=" + getId()
                + ", Name='" + getName()
                + "', Email='" + email
                + "', Phone='" + getPhoneNumber()
                + "', Home City='" + homeCity + "'}";
    }
}
