package com.alexkirillov.alitabot.models.appointment;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.time.Duration;
import java.util.List;


public class AlitaAppointment {
    private final double price;
    private final String service_name;
    private final Duration estimated_time;
    private final short service_id;
    private  final List<String> employees;

    public AlitaAppointment(double price, String service_name, Duration estimated_time, short service_id,
                            List<String> employees) {
        this.price = price;
        this.service_name = service_name;
        this.estimated_time = estimated_time;
        this.service_id = service_id;
        this.employees = employees;
    }

    public double getPrice() {
        return price;
    }

    public String getServiceName() {
        return service_name;
    }

    public Duration getEstimatedTime() {
        return estimated_time;
    }
    public Long getEstimatedTimeInLong(){
        return (Long)estimated_time.toNanos();
    }

    public short getServiceId() {
        return service_id;
    }

    public List<String> getEmployeeList(){
        return employees;
    }

    @Override
    public String toString() {
        return service_name+"\nЦена: "+price+"\nПриблизительная длительность: "+estimated_time.toMinutes()+" минут.\n\n";
    }
    
    public DBObject toDBObject(AlitaAppointment alitaService){
        return new BasicDBObject("serviceId", service_id).append("serviceName", service_name)
                                .append("price", price).append("estematedTime", estimated_time.toMinutes())
                                .append("employees", employees);
    }
}
