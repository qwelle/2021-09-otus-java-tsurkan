package homework;


import java.util.*;

public class CustomerService {

    class CustomerComparator implements Comparator<Customer> {
        @Override
        public int compare(Customer o1, Customer o2) {
            return o1.getScores() == o2.getScores() ? 0 : o1.getScores() < o2.getScores() ? -1 : 1;
        }
    }

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private TreeMap<Customer, String> mapCustomer;
    private List<Map.Entry<Customer, String>> listEntries;

    public CustomerService() {
        this.mapCustomer = new TreeMap<>(new CustomerComparator());
    }

    public static Map.Entry<Customer, String> getDeepCopyMapEntry(Map.Entry<Customer, String> mapEntry) {
        Customer key = mapEntry.getKey();
        return new Map.Entry<>() {
            @Override
            public Customer getKey() {
                return new Customer(key.getId(), key.getName(), key.getScores());
            }

            @Override
            public String getValue() {
                return mapEntry.getValue();
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };
    }

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return CustomerService.getDeepCopyMapEntry(mapCustomer.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = mapCustomer.higherEntry(customer);
        return nextEntry == null ? null : CustomerService.getDeepCopyMapEntry(nextEntry);
    }

    public void add(Customer customer, String data) {
        mapCustomer.put(customer, data);
    }

}
