package prr.deliveryMethod;

import java.util.ArrayList;

import prr.Notifications.Notifications;

public interface DeliveryMethod {
    public String deliver(ArrayList<Notifications> notifications);
}
