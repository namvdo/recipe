class Customer {

    String firstName;
    String lastName;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public static void main(String[] args) {
        Customer customer = new Customer("Nam", "Do");
        Runnable getFirstName = customer::getFirstName;
    }
}